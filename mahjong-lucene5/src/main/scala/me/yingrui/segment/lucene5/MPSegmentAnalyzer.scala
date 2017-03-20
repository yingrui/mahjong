package me.yingrui.segment.lucene5

import java.io.{BufferedReader, Reader}

import org.apache.lucene.analysis.Analyzer.TokenStreamComponents
import org.apache.lucene.analysis.tokenattributes._
import org.apache.lucene.analysis.util.CharArraySet
import org.apache.lucene.analysis.{Analyzer, Tokenizer}
import org.apache.lucene.util.Version
import me.yingrui.segment.core.{SegmentResult, SegmentWorker, Word}
import me.yingrui.segment.dict.POSUtil

final class MPSegmentAnalyzer extends Analyzer {

  @Override
  def createComponents(fieldName: String): TokenStreamComponents = {
    val tokenizer = new MPSegmentTokenizer(SegmentWorker(
      "load.englishdictionary" -> "false",
      "load.domaindictionary" -> "false",
      "recognize.partOfSpeech" -> "false",
      "separate.xingming" -> "true",
      "convert.touppercase" -> "true"))
    new TokenStreamComponents(tokenizer)
  }


  class MPSegmentTokenizer(worker: SegmentWorker) extends Tokenizer {

    val termAtt = addAttribute(classOf[CharTermAttribute])
    val positionAttr = addAttribute(classOf[PositionIncrementAttribute])
    val offsetAtt = addAttribute(classOf[OffsetAttribute])
    var index = 0
    var offset = 0
    var result: SegmentResult = null

    @Override
    final def incrementToken(): Boolean = {
      clearAttributes()

      if (index == 0) {
        result = worker.segment(readInputText())
      }

      if (index >= result.length()) {
        index = 0
        offset = 0
        false
      } else {
        addWord(result(index))

        offset += result(index).length
        index += 1
        true
      }
    }

    private def readInputText(): String = {
      val buffer = new StringBuilder
      val br = new BufferedReader(input)
      var line = br.readLine()
      while (null != line) {
        buffer.append(line)
        line = br.readLine()
      }
      buffer.toString()
    }


    def addWord(word: Word) {
      termAtt.append(word.name)
      termAtt.setLength(word.length)
      offsetAtt.setOffset(word.start, word.end)
      positionAttr.setPositionIncrement(index + 1)
    }

    override def reset() {
      super.reset()
    }

    override def end(): Unit = {
      super.end()
    }
  }

}
