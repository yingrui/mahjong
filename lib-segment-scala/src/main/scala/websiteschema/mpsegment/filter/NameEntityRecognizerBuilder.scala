package websiteschema.mpsegment.filter

import websiteschema.mpsegment.tools.PFRCorpusLoader
import websiteschema.mpsegment.util.FileUtil._
import websiteschema.mpsegment.util.CharCheckUtil._
import websiteschema.mpsegment.core.{WordAtom, SegmentResult}
import websiteschema.mpsegment.dict.POSUtil

class NameEntityRecognizerBuilder {
  var loader: PFRCorpusLoader = null
  val result = new NameEntityRecognizerStatisticResult()

  def load(resource: String) = {
    loader = PFRCorpusLoader(getResourceAsStream(resource))
  }

  def analysis: NameEntityRecognizerStatisticResult = {
    loader.load(statisticFrequency)
    result
  }

  private def statisticFrequency(sentence: SegmentResult) {
    for (i <- 0 until sentence.length()) {
      val word = sentence.getWord(i)

      result.wordOccurTotal += 1.0D
      if (sentence.getPOS(i) == POSUtil.POS_NR) result.nameOccurTotal += 1.0D

      statisticBoundary(i, sentence)
      for (ch <- word) {
        if (sentence.getPOS(i) == POSUtil.POS_NR) {
          result.nameWordFreqPlusOne(ch.toString)
        } else {
          result.normalWordFreqPlusOne(ch.toString)
        }
      }
    }

    val sentenceString = sentence.toOriginalString()
    for (i <- 0 until sentenceString.length) {
      val ch = sentenceString.charAt(i)
      if(isChinese(ch)) {
        result.charOccurTotal += 1.0D
        if (i < sentenceString.length - 1) {
          val bigram = sentenceString.substring(i, i + 2)
          val wordIndex = getWordIndex(sentence, i)

          if(isChinese(bigram) && !isNameEntity(sentence, wordIndex) && !isNameEntity(sentence, wordIndex + 1)) {
            result.charBigramPlusOne(bigram)
          }
        }
      }
    }
  }

  private def isNameEntity(sentence: SegmentResult, index: Int): Boolean = {
    index < sentence.length() && sentence.getPOS(index) == POSUtil.POS_NR
  }

  private def getWordIndex(sentence: SegmentResult, index: Int): Int = {
    var i = 0
    var charCount = 0
    while(charCount < index) {
      charCount += sentence.getWord(i).length
      i += 1
    }
    if (charCount > index) {
      i -= 1
    }
    i
  }

  private def isWordName(sentence: SegmentResult, index: Int) = {
    (isWordPosNR(sentence(index)) || isWordPosR(sentence(index)))
  }

  private def statisticBoundary(i: Int, sentence: SegmentResult) {
    val word = sentence(i)
    result.wordFreqPlusOne(word.word)
    if (i > 0 && isWordName(sentence, i - 1) && !isWordName(sentence, i)) {
      result.wordRightBoundaryFreqPlusOne(word.word)
    }

    if (i < sentence.length() - 1 && (isWordName(sentence, i + 1)) && !isWordName(sentence, i)) {
      result.wordLeftBoundaryFreqPlusOne(word.word)
    }

    if(i == 0 && (isWordPosNR(word) || isWordPosR(word))) {
      result.wordLeftBoundaryFreqPlusOne("\0");
      result.wordFreqPlusOne("\0")
    }

    if(i == sentence.length() - 1 && (isWordPosNR(word) || isWordPosR(word))) {
      result.wordRightBoundaryFreqPlusOne("\0");
    }
  }

  private def isWordPosNR(word: WordAtom): Boolean = word.pos == POSUtil.POS_NR

  private def isWordPosR(word: WordAtom): Boolean = word.pos == POSUtil.POS_R

}
