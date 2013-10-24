package websiteschema.mpsegment.tools.ner

import websiteschema.mpsegment.tools.accurary.SegmentResultCompareHook
import websiteschema.mpsegment.core.{WordAtom, SegmentResult}
import websiteschema.mpsegment.filter.ner.NeuralNetworkNameRecognizer
import websiteschema.mpsegment.dict.POSUtil

class PRFCorpusToSerialLabelCompareHooker(expect: SegmentResult, actual: SegmentResult) extends SegmentResultCompareHook {

  var serialLabels = List[(String, String)]()
  var isContainError = false
  var isOtherError = false
  var isError = false

  override def compeleted {
    serialLabels = scanTheSerialLabels
  }


  def scanTheSerialLabels: List[(String, String)] = {
    (for (i <- 0 until serialLabels.length) yield {
      val nextIsXing = i < serialLabels.length - 1 && serialLabels(i + 1)._2 == "B"
      val nextIsName = i < serialLabels.length - 1 && isLabeledAsName(serialLabels(i + 1)._2)
      if (nextIsXing && serialLabels(i)._2 == "A") {
        (serialLabels(i)._1, "K")
      } else if (nextIsName && serialLabels(i)._2 == "L") {
        (serialLabels(i)._1, "M")
      } else {
        serialLabels(i)
      }
    }).toList
  }

  override def correctWordHook(expectWord: WordAtom, actualWord: WordAtom, expectWordIndex: Int, actualWordIndex: Int) {
    isContainError = false
    isOtherError = false
    isError = false
    val label = getLabel(expectWord, actualWord, expectWordIndex, actualWordIndex)
    add(actualWord.word, label)
  }

  private def analysisError(expectWord: WordAtom, actualWord: WordAtom, expectWordIndex: Int, actualWordIndex: Int) {
    isContainError = startWith(actualWord, expectWord) && expectWordIndex < expect.length() - 1 && endWith(actualWord, expect(expectWordIndex + 1))
    isOtherError = startWith(actualWord, expectWord) && !isContainError
  }

  override def foundError(expectWord: WordAtom, actualWord: WordAtom, expectWordIndex: Int, actualWordIndex: Int) {
    isError = true
    analysisError(expectWord, actualWord, expectWordIndex, actualWordIndex)
    val label = getLabel(expectWord, actualWord, expectWordIndex, actualWordIndex)
    add(actualWord.word, label)
  }

  private def getLabel(expectWord: WordAtom, actualWord: WordAtom, expectWordIndex: Int, matchedWordIndex: Int): String = {
//    println(expectWord.word + " " + actualWord.word)
    var label = if (isName(expectWord) && isXing(expectWord)) "B" else "A"

    if (isName(expectWord) && isXing(actualWord) && lastLabel == "F" && endWith(expectWord, actualWord))
      label = "B"

    if (isName(expectWord) && isXing(actualWord) && startWith(expectWord, actualWord))
      label = "B"

    if (isName(expectWord) && isSingleWord(expectWord) && lastLabel == "B")
      label = "E"

    if (isName(expectWord) && !isSingleWord(expectWord) && isSingleWord(actualWord) && (lastLabel == "C" || lastLabel == "X"))
      label = "D"

    if (isName(expectWord) && (expectWordIndex + 1 < expect.length() && isName(expect(expectWordIndex + 1)) )&& !isSingleWord(expectWord) && !isSingleWord(actualWord) && overlap(expectWord, actualWord))
      label = "U"

    if (isContainError && isName(expect(expectWordIndex + 1)) && isXing(expect(expectWordIndex + 1)))
      label = "U"

    if (isName(expectWord) && (expectWordIndex + 1 < expect.length() && !isName(expect(expectWordIndex + 1)) )&& !isSingleWord(expectWord) && !isSingleWord(actualWord) && overlap(expectWord, actualWord))
      label = "V"

    if (isContainError && isName(expectWord) && !isName(expect(expectWordIndex + 1)) && isSingleWord(expectWord) && !isSingleWord(actualWord))
      label = "V"

    if (isOtherError && isName(expectWord) && isXing(expectWord) && isName(expect(expectWordIndex + 1)) && isSingleWord(expectWord) && !isSingleWord(actualWord))
      label = "X"

    if (isContainError && isName(expectWord) && isName(expect(expectWordIndex + 1)) && isSingleWord(expectWord) && isXing(expectWord))
      label = "Y"

    if ((lastLabel == "B" || lastLabel == "U") && !isError && actualWord.word.length > 1 && isName(expectWord))
      label = "Z"

    if (isName(expectWord) && !isSingleWord(expectWord) && isSingleWord(actualWord) && (lastLabel == "B" || lastLabel == "U")
        && startWith(expectWord, actualWord))
      label = "C"

    if (isName(expectWord) && !isSingleWord(expectWord) && isSingleWord(actualWord) && !isXing(actualWord)
        && startWith(expectWord, actualWord) && (lastLabel == "A" || lastLabel == "" || !isXing(lastExpectWord(expectWordIndex))))
      label = "F"

    if (isName(expectWord) && !isSingleWord(expectWord) && isSingleWord(actualWord) && !isXing(actualWord)
        && endWith(expectWord, actualWord) && lastLabel == "B")
      label = "G"

    if (!isName(expectWord) && label == "A" && isLabeledAsName(lastLabel))
      label = "L"

    label
  }


  private def isLabeledAsName(label: String): Boolean = !label.isEmpty && "BCDEYZ".contains(label)

  private def lastLabel = if(serialLabels.length > 0) serialLabels.last._2 else ""
  private def lastExpectWord(expectWordIndex: Int) = if(expectWordIndex > 0) expect(expectWordIndex - 1) else new WordAtom

  private def isXing(word: WordAtom): Boolean = NeuralNetworkNameRecognizer.nameDistribution.xingSet.contains(word.word)

  private def isName(word: WordAtom) = word.pos == POSUtil.POS_NR
  private def isSingleWord(word: WordAtom) = word.length == 1
  private def startWith(expectWord: WordAtom, actualWord: WordAtom) = expectWord.word.startsWith(actualWord.word)
  private def endWith(expectWord: WordAtom, actualWord: WordAtom) = expectWord.word.endsWith(actualWord.word)

  private def overlap(expectWord: WordAtom, actualWord: WordAtom) = expectWord.word.last == actualWord.word.head

  private def add(word: String, label: String) {
    serialLabels = serialLabels :+(word, label)
  }
}
