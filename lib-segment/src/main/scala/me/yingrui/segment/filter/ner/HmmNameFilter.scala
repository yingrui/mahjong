package me.yingrui.segment.filter.ner

import me.yingrui.segment.concept.Concept
import me.yingrui.segment.conf.SegmentConfiguration
import me.yingrui.segment.dict.POSUtil
import me.yingrui.segment.filter.AbstractSegmentFilter
import me.yingrui.segment.hmm.{Emission, HmmClassifier, HmmModel}
import me.yingrui.segment.util.FileUtil

class HmmNameFilter(config: SegmentConfiguration, classifier: HmmClassifier) extends AbstractSegmentFilter {

  import HmmNameFilter._

  private var nameStartIndex = -1
  private var nameEndIndex = -1
  private var numOfNameWordItem = -1
  private var shouldSeparateXing = false
  private var labels: Seq[String] = null
  private var curIndex = 0

  override def doFilter() {
    val words = segmentResult.getWords().map(word => word.name)
    labels = classifier.classify(words)
    for (index <- 0 until labels.length; label = labels(index)) {
      curIndex = index
      if (label != "A") {
        if (label == Xing && (nextLabel == "E" || nextLabel == "Z")) {
          processXingAndSingleName
        } else if (label == Xing && nextLabel == "C" && nextNextLabel == "D") {
          processXingAndDoubleName
        } else if (label == Xing && nextLabel == "G") {
          processXingAndSuffix
        } else if (label == "F" && nextLabel == Xing) {
          processXingAndPrefix
        } else if (label == "X" && nextLabel == "D") {
          processWordContainsXingAndOtherName
        } else if (label == "Y") {
          separateWordAt(curIndex, POSUtil.POS_NR, POSUtil.POS_NR)
        } else if (label == "U" && (nextLabel == "E" || nextLabel == "Z")) {
          separateWordAt(curIndex, POSUtil.POS_UNKOWN, POSUtil.POS_NR)
          processXingAndSingleName
        } else if (label == "U" && nextLabel == "C" && nextNextLabel == "D") {
          separateWordAt(curIndex, POSUtil.POS_UNKOWN, POSUtil.POS_NR)
          processXingAndDoubleName
        } else if (label == Xing && nextLabel == "C" && nextNextLabel == "V") {
          processXingAndSingleName
          separateWordAt(curIndex + 2, POSUtil.POS_NR, POSUtil.POS_UNKOWN)
          setWordIndexesAndPOSForMerge(curIndex, curIndex + 1, POSUtil.POS_NR)
        } else if (label == Xing && nextLabel == "V") {
          processXingAndSingleName
          separateWordAt(curIndex + 1, POSUtil.POS_NR, POSUtil.POS_UNKOWN)
        } else if (label == "H" && "IJ".contains(nextLabel)) {
          processForeignName
        }
      }
    }
  }

  def processForeignName {
    nameStartIndex = curIndex
    nameEndIndex = labels.indexWhere(l => !"IJ".contains(l), curIndex + 1) - 1
    nameEndIndex = if (nameEndIndex > 0) nameEndIndex else segmentResult.length - 1
    shouldSeparateXing = false
    processPotentialName
  }

  private def processWordContainsXingAndOtherName {
    nameStartIndex = curIndex
    nameEndIndex = curIndex + 1
    shouldSeparateXing = true

    segmentResult.adjustAdjacentWords(nameStartIndex, 1)

    processPotentialName
  }

  private def processXingAndSuffix {
    nameStartIndex = curIndex
    nameEndIndex = curIndex + 1
    shouldSeparateXing = false
    processPotentialName
  }

  private def processXingAndPrefix {
    nameStartIndex = curIndex
    nameEndIndex = curIndex + 1
    shouldSeparateXing = false
    processPotentialName
  }

  private def processXingAndDoubleName {
    nameStartIndex = curIndex
    nameEndIndex = curIndex + 2
    shouldSeparateXing = true
    processPotentialName
  }

  private def processXingAndSingleName {
    nameStartIndex = curIndex
    nameEndIndex = curIndex + 1
    shouldSeparateXing = true
    processPotentialName
  }

  private def nextLabel = if (labels.length > curIndex + 1) labels(curIndex + 1) else ""

  private def nextNextLabel = if (labels.length > curIndex + 2) labels(curIndex + 2) else ""

  private def processPotentialName {
    if (nameEndIndex - nameStartIndex >= 1) {
      recognizeNameWord
    }
    resetStatus()
  }

  private def resetStatus() {
    shouldSeparateXing = false
    nameStartIndex = -1
    nameEndIndex = -1
  }

  private def recognizeNameWord: Int = {
    numOfNameWordItem = nameEndIndex - nameStartIndex + 1
    if (numOfNameWordItem > 0) {
      if (config.isXingMingSeparate()) {
        separateXingMing
      } else if (numOfNameWordItem >= 2) {
        setWordIndexesAndPOSForMerge(nameStartIndex, nameEndIndex, POSUtil.POS_NR)
        segmentResult.setConcept(nameStartIndex, Concept.UNKNOWN.getName())
      }
    }
    numOfNameWordItem
  }

  private def separateXingMing {
    val isChineseName = numOfNameWordItem <= 3
    if (isChineseName && shouldSeparateXing) {
      if (numOfNameWordItem >= 2) {
        val numOfMingWord = numOfNameWordItem - 1
        setWordIndexesAndPOSForMerge(nameStartIndex + 1, nameStartIndex + numOfMingWord, POSUtil.POS_NR)
      }
      segmentResult.setPOS(nameStartIndex, POSUtil.POS_NR)
      segmentResult.setConcept(nameStartIndex, Concept.UNKNOWN.getName())
      segmentResult.setConcept(nameStartIndex + 1, Concept.UNKNOWN.getName())
    } else {
      setWordIndexesAndPOSForMerge(nameStartIndex, (nameStartIndex + numOfNameWordItem) - 1, POSUtil.POS_NR)
      segmentResult.setConcept(nameStartIndex, Concept.UNKNOWN.getName())
    }
  }

}

object HmmNameFilter {

  val Xing = "B"

  val nameDistribution = new NameProbDistribution()
  val model = new HmmModel
  model.load(FileUtil.getResourceAsStream("ner-hmm.m"))
  model.buildViterbi(Emission(model.getEmission, () => getDefaultState, (stateIndex: Int) => getAppendixStates(stateIndex)))

  def apply(config: SegmentConfiguration): HmmNameFilter = {
    val classifier = new HmmClassifier(model)
    new HmmNameFilter(config, classifier)
  }

  private def getAppendixStates(stateIndex: Int): java.util.Collection[Int] = {
    val ret = new java.util.ArrayList[Int]()
    val name = model.getObserveBank.get(stateIndex).getName()
    if (nameDistribution.nameLabels.containsKey(name)) {
      nameDistribution.nameLabels.get(name).keySet().toArray(Array[String]())
        .foreach(state => ret.add(model.getStateBank.get(state).getIndex()))
    }
    ret
  }

  private def getDefaultState(): java.util.Collection[Int] = {
    val ret = new java.util.ArrayList[Int]()
    ret.add(model.getStateBank.get("A").getIndex())
    ret
  }
}
