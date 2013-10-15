package websiteschema.mpsegment.tools.ner

import websiteschema.mpsegment.tools.accurary.SegmentResultCompareHook
import websiteschema.mpsegment.core.{WordAtom, SegmentResult}
import websiteschema.mpsegment.filter.ner.NeuralNetworkNameRecognizer
import websiteschema.mpsegment.dict.POSUtil

class PRFCorpusToSerialLabelCompareHooker(expect: SegmentResult, actual: SegmentResult) extends SegmentResultCompareHook {

  var serialLabels = List[(String, String)]()

  override def compeleted {
    serialLabels = scanTheSerialLabels
  }


  def scanTheSerialLabels: List[(String, String)] = {
    (for (i <- 0 until serialLabels.length) yield {
      val nextIsXing = i < serialLabels.length - 1 && serialLabels(i + 1)._2 == "B"
      if (nextIsXing && serialLabels(i)._2 == "A") {
        (serialLabels(i)._1, "K")
      } else {
        serialLabels(i)
      }
    }).toList
  }

  override def correctWordHook(expectWord: WordAtom, actualWord: WordAtom, expectWordIndex: Int, actualWordIndex: Int) {
    val label = getLabel(expectWord, actualWord, expectWordIndex, actualWordIndex)
    add(actualWord.word, label)
  }

  override def foundError(expectWord: WordAtom, actualWord: WordAtom, expectWordIndex: Int, actualWordIndex: Int) {
    val label = getLabel(expectWord, actualWord, expectWordIndex, actualWordIndex)
    add(actualWord.word, label)
  }

  private def getLabel(expectWord: WordAtom, actualWord: WordAtom, expectWordIndex: Int, matchedWordIndex: Int): String = {
//    println(expectWord.word + " " + actualWord.word)
    var label = if (isName(expectWord) && isXing(expectWord)) "B" else "A"

    if (isName(expectWord) && isXing(actualWord) && lastLabel == "F" && expectWordEndWith(expectWord, actualWord))
      label = "B"

    if (isName(expectWord) && isXing(actualWord) && expectWordStartWith(expectWord, actualWord))
      label = "B"

    if (isName(expectWord) && isSingleWord(expectWord) && lastLabel == "B")
      label = "E"

    if (isName(expectWord) && !isSingleWord(expectWord) && isSingleWord(actualWord) && lastLabel == "C")
      label = "D"

    if (isName(expectWord) && !isSingleWord(expectWord) && !isSingleWord(actualWord) && overlap(expectWord, actualWord))
      label = "U"

    if (isName(expectWord) && !isSingleWord(expectWord) && isSingleWord(actualWord) && (lastLabel == "B" || lastLabel == "U")
        && expectWordStartWith(expectWord, actualWord))
      label = "C"

    if (isName(expectWord) && !isSingleWord(expectWord) && isSingleWord(actualWord) && !isXing(actualWord)
        && expectWordStartWith(expectWord, actualWord) && (lastLabel == "A" || lastLabel == "" || !isXing(lastExpectWord(expectWordIndex))))
      label = "F"

    if (isName(expectWord) && !isSingleWord(expectWord) && isSingleWord(actualWord) && !isXing(actualWord)
        && expectWordEndWith(expectWord, actualWord) && lastLabel == "B")
      label = "G"

    label
  }

  private def lastLabel = if(serialLabels.length > 0) serialLabels.last._2 else ""
  private def lastExpectWord(expectWordIndex: Int) = if(expectWordIndex > 0) expect(expectWordIndex - 1) else new WordAtom

  private def isXing(word: WordAtom): Boolean = NeuralNetworkNameRecognizer.nameDistribution.xingSet.contains(word.word)

  private def isName(word: WordAtom) = word.pos == POSUtil.POS_NR
  private def isSingleWord(word: WordAtom) = word.length == 1
  private def expectWordStartWith(expectWord: WordAtom, actualWord: WordAtom) = expectWord.word.startsWith(actualWord.word)
  private def expectWordEndWith(expectWord: WordAtom, actualWord: WordAtom) = expectWord.word.endsWith(actualWord.word)

  private def overlap(expectWord: WordAtom, actualWord: WordAtom) = expectWord.word.last == actualWord.word.head

  private def add(word: String, label: String) {
    serialLabels = serialLabels :+(word, label)
  }
}
