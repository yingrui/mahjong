package me.yingrui.segment.filter.disambiguation

import me.yingrui.segment.core.disambiguation.DisambiguationToSerialLabels._
import me.yingrui.segment.crf.CRFClassifier
import me.yingrui.segment.dict.POSUtil._
import me.yingrui.segment.filter.AbstractSegmentFilter

import scala.collection.mutable.ListBuffer

class CRFDisambiguationFilter(classfier: CRFClassifier) extends AbstractSegmentFilter {

  override def doFilter(): Unit = {
    val words = currentSegmentResult()
    val labels = findDisambiguationLabels(words)
    filter(labels, words)
  }

  private def findDisambiguationLabels(observeList: Seq[String]): Seq[String] = {
    val labels = ListBuffer[String]()
    val array = ListBuffer[String]()
    observeList.zipWithIndex.foreach(_ match {
      case (word, index) => {
        if (classfier.isFeatureExists(word)) {
          array += word
        } else {
          if (!array.isEmpty) {
            labels ++= classfier.findBestLabels(array)
            array.clear()
          }
          labels += LABEL_A
        }
      }
    })
    if (!array.isEmpty) {
      labels ++= classfier.findBestLabels(array)
      array.clear()
    }
    assert(labels.length == observeList.length)
    labels
  }

  private def filter(labels: Seq[String], words: Seq[String]): Unit = {
    labels.zipWithIndex.foreach(_ match {
      case (label, index) => process(label, index, labels, words)
      case _ =>
    })
  }

  private def process(label: String, index: Int, labels: Seq[String], words: Seq[String]): Unit = {
    label match {
      case LABEL_U => {
        if (words(index).length == 4) {
          val pos = segmentResult.getPOS(index)
          separateWordAt(index, pos, pos, 2)
        } else if (words(index).length > 1) {
          val pos = segmentResult.getPOS(index)
          separateWordAt(index, pos, pos)
        }
      }
      case LABEL_SE => {
        if (previousLabel(index, labels) == LABEL_SB) {
          setWordIndexesAndPOSForMerge(index - 1, index, previousWordPOS(index))
        } else {
          val previousLabels = findPreviousLabels(index, labels)
          if (!previousLabels.isEmpty) {
            val startOffset = previousLabels.size
            if (previousLabels.head == LABEL_SB) {
              setWordIndexesAndPOSForMerge(index - startOffset, index, segmentResult.getPOS(index - startOffset))
            } else if (previousLabels.head == LABEL_LC && segmentResult.getWord(index - startOffset).length > 1) {
              setWordIndexesAndPOSForMerge(index - startOffset, index, segmentResult.getPOS(index - startOffset))
            }
          }
        }
      }
      case LABEL_LC => {
        if (words(index).length > 1 && (nextLabel(index, labels) == LABEL_SE || nextLabel(index, labels) == LABEL_SM)) {
          val pos = segmentResult.getPOS(index)
          separateWordAt(index, pos, POS_UNKOWN, segmentResult.getWord(index).length - 1)
        }
      }
      case LABEL_A =>
      case _ =>
    }
  }

  private def previousWordPOS(index: Int): Int = segmentResult.getPOS(index - 1)

  private def previousLabel(index: Int, labels: Seq[String]): String = if (index > 0) labels(index - 1) else LABEL_A

  private def nextLabel(index: Int, labels: Seq[String]): String = if (labels.length - index > 1) labels(index + 1) else LABEL_A

  private def findPreviousLabels(index: Int, labels: Seq[String]): Array[String] = {
    val lastLabel = previousLabel(index, labels)
    if (lastLabel == LABEL_SM)
      findPreviousLabels(index - 1, labels) ++ Array(lastLabel)
    else if (lastLabel == LABEL_SB || lastLabel == LABEL_LC)
      Array(lastLabel)
    else
      Array()
  }

  private def currentSegmentResult() = this.segmentResult.map(_.name)
}
