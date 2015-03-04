package websiteschema.mpsegment.core

import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.filter.SegmentResultFilter

class MPSegmentWorker(config: MPSegmentConfiguration) extends SegmentWorker {

  private var unKnownFilter: SegmentResultFilter = null
  private val maxSegStrLength = 400000
  private var mpSegment: MPSegment = null
  private var recognizePOS: Boolean = true

  mpSegment = new MPSegment(config)
  unKnownFilter = new SegmentResultFilter(config)

  def isUseDomainDictionary(): Boolean = {
    config.isLoadDomainDictionary()
  }

  def segment(sen: String): SegmentResult = {
    var sentence = sen
    var result: SegmentResult = null
    if (sentence != null && sentence.length() > 0) {
      if (sentence.length() > maxSegStrLength) {
        sentence = sentence.substring(0, maxSegStrLength)
      }
      result = mpSegment.segmentMP(sentence, recognizePOS)
      if (recognizePOS) {
        unKnownFilter.filter(result)
      }
    } else {
      result = new SegmentResult(0)
    }
    result
  }

  def isUseContextFreqSegment() = config.isUseContextFreqSegment()

  def isRecognizePOS() = recognizePOS

  def setRecognizePOS(recognizePOS: Boolean) {
    this.recognizePOS = recognizePOS
  }
}
