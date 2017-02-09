package me.yingrui.segment.filter.disambiguation

import me.yingrui.segment.crf.CRFClassifier
import me.yingrui.segment.filter.AbstractSegmentFilter

class CRFDisambiguationFilter(classfier: CRFClassifier) extends AbstractSegmentFilter {

  override def doFilter(): Unit = {
    val labels = classfier.findBestLabels(currentSegmentResult)
    labels
  }

  private def currentSegmentResult = this.segmentResult.map(_.name)
}
