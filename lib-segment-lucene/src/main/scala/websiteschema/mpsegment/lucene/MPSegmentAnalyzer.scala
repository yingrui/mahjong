package websiteschema.mpsegment.lucene

import org.apache.lucene.analysis.{Tokenizer, Analyzer}
import org.apache.lucene.analysis.tokenattributes._
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents
import java.io.{BufferedReader, Reader}
import websiteschema.mpsegment.core.{WordAtom, SegmentResult, SegmentWorker}
import websiteschema.mpsegment.dict.POSUtil
import org.apache.lucene.analysis.core.StopFilter
import org.apache.lucene.util.Version
import org.apache.lucene.analysis.util.CharArraySet

final class MPSegmentAnalyzer extends Analyzer {

  val charArraySet = new CharArraySet(Version.LUCENE_46, 1, true)
  charArraySet.add(" ")

  @Override
  def createComponents(fieldName: String, reader: Reader): TokenStreamComponents = {
    val tokenizer = new MPSegmentTokenizer(reader, SegmentWorker("segment.lang.en = true", "convert.touppercase = true"))
    new TokenStreamComponents(tokenizer)
  }


  class MPSegmentTokenizer(reader: Reader, worker: SegmentWorker) extends Tokenizer(reader) {

    val termAtt = addAttribute(classOf[CharTermAttribute])
    val positionAttr = addAttribute(classOf[PositionIncrementAttribute])
    val offsetAtt = addAttribute(classOf[OffsetAttribute])
    val typeAtt = addAttribute(classOf[TypeAttribute])
    var index = 0
    var offset = 0
    var result: SegmentResult = null

    @Override
    final def incrementToken(): Boolean = {
      clearAttributes()

      if (index == 0) {
        result = worker.segment(readInputText(reader))
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

    private def readInputText(reader: Reader): String = {
      val buffer = new StringBuilder
      val br = new BufferedReader(reader)
      var line = br.readLine()
      while (null != line) {
        buffer.append(line)
        line = br.readLine()
      }
      buffer.toString()
    }


    def addWord(word: WordAtom) {
      termAtt.append(word.word)
      termAtt.setLength(word.length)
      offsetAtt.setOffset(word.start, word.end)
      typeAtt.setType(POSUtil.getPOSString(word.pos))
      positionAttr.setPositionIncrement(index + 1)
    }
  }

}
