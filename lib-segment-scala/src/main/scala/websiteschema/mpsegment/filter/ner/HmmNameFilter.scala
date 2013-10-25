package websiteschema.mpsegment.filter.ner

import websiteschema.mpsegment.hmm.{HmmClassifier, HmmModel}
import websiteschema.mpsegment.util.FileUtil
import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.filter.AbstractSegmentFilter
import websiteschema.mpsegment.dict.POSUtil
import websiteschema.mpsegment.concept.Concept

class HmmNameFilter(config: MPSegmentConfiguration, classifier: HmmClassifier) extends AbstractSegmentFilter {

  private var nameStartIndex = -1
  private var nameEndIndex = -1
  private var numOfNameWordItem = -1
  private var startWithXing = false

  override def doFilter() {
    val words = segmentResult.getWordAtoms().map(wordAtom => wordAtom.word)
    val result = classifier.classify(words)
    for (index <- 0 until result.length; label = result(index)) {
      if (label == "B") {
        startWithXing = true
        nameStartIndex = index
      }
      if (label == "D") {
        nameEndIndex = index
        processPotentialName
      }
    }
  }

  private def processPotentialName {
    if (nameEndIndex - nameStartIndex >= 1) {
      recognizeNameWord
    }
    resetStatus()
  }

  private def resetStatus() {
    startWithXing = false
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
    if (isChineseName && startWithXing) {
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
  val model = new HmmModel()
  model.load(FileUtil.getResourceAsStream("ner.m"))

  def apply(config: MPSegmentConfiguration): HmmNameFilter = {
    val classifier = new HmmClassifier
    classifier.setModel(model)
    new HmmNameFilter(config, classifier)
  }
}