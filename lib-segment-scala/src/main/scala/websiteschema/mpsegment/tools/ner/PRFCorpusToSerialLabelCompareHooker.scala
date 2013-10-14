package websiteschema.mpsegment.tools.ner

import websiteschema.mpsegment.tools.accurary.SegmentResultCompareHook
import websiteschema.mpsegment.core.{WordAtom, SegmentResult}
import websiteschema.mpsegment.filter.ner.NeuralNetworkNameRecognizer
import websiteschema.mpsegment.dict.POSUtil

class PRFCorpusToSerialLabelCompareHooker(expect: SegmentResult, actual: SegmentResult) extends SegmentResultCompareHook {

  var serialLabels = List[(String, String)]()

  override def correctWordHook(expectWord: WordAtom, actualWord: WordAtom, expectWordIndex: Int, actualWordIndex: Int) {
    val label = getLabel(expectWord, actualWord, expectWordIndex, actualWordIndex)
    add(expectWord.word, label)
  }

  override def foundError(expectWord: WordAtom, actualWord: WordAtom, expectWordIndex: Int, actualWordIndex: Int) {
    val label = getLabel(expectWord, actualWord, expectWordIndex, actualWordIndex)
    add(expectWord.word, label)
  }

  private def getLabel(expectWord: WordAtom, actualWord: WordAtom, expectWordIndex: Int, matchedWordIndex: Int): String = {
    var label = if (isXing(expectWord)) "B" else "A"

    if (isName(expectWord) && isSingleWord(expectWord) && lastLabel == "B")
      label = "E"

    label
  }

  private def lastLabel = if(serialLabels.length > 0) serialLabels.last._2 else ""

  private def isXing(word: WordAtom): Boolean = isName(word) && NeuralNetworkNameRecognizer.nameDistribution.xingSet.contains(word.word)

  private def isName(word: WordAtom) = word.pos == POSUtil.POS_NR
  private def isSingleWord(word: WordAtom) = word.length == 1

  private def add(word: String, label: String) {
    serialLabels = serialLabels :+(word, label)
  }
}
