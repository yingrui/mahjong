package me.yingrui.segment.filter.disambiguation

import me.yingrui.segment.crf.CRFClassifier
import me.yingrui.segment.filter.AbstractSegmentFilter

class CRFDisambiguationFilter(classfier: CRFClassifier) extends AbstractSegmentFilter {

  override def doFilter(): Unit = {
    val words = currentSegmentResult()
    val labels = classfier.findBestLabels(words)
    labels.zipWithIndex.foreach(_ match {
      case (label, index) => process(label, index, labels, words)
      case _ =>
    })
  }

  private def process(label: String, index: Int, labels: Seq[String], words: Seq[String]): Unit = {
    label match {
      case "U" => {
        val pos = segmentResult.getPOS(index)
        separateWordAt(index, pos, pos)
      }
      case "A" =>
      case _ =>
    }
  }

  private def currentSegmentResult() = this.segmentResult.map(_.name)
}
