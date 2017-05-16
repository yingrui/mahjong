package me.yingrui.segment.tools

import java.io.InputStream

import me.yingrui.segment.concept.ConceptRepository
import me.yingrui.segment.dict.{HashDictionary, IWord, POSUtil}
import me.yingrui.segment.util.FileUtil.getResourceAsStream

import scala.io.Source

object DictionarySelectionApp extends App {

  val tic = System.currentTimeMillis()
  try {
    loadDictionary(getResourceAsStream("me/yingrui/segment/dict.txt"))
  } catch {
    case e: Throwable =>
      e.printStackTrace()
  } finally {
    val timeSpan = System.currentTimeMillis() - tic
    System.err.println(s"loading dictionary time used(ms): $timeSpan")
  }

  def loadDictionary(inputStream: InputStream): Unit = {
    val dict = new HashDictionary()
    loadWords(inputStream) { words =>
      words
        .filter(word => {
          word.getConcepts().length > 0 || word.getWordLength() == 1 || isIdiom(word)
        })
        .foreach(dict.addWord)
    }
    DictionaryWriter.writeDictionary(dict, "test.dict")
  }

  private def isIdiom(word: IWord): Boolean = word.getWordPOSTable().map(_.apply(0)).contains(POSUtil.POS_I)

  private def loadWords(inputStream: InputStream)(convert: (Iterator[IWord]) => Unit) {
    val source = Source.fromInputStream(inputStream, "utf-8")
    val converter = new StringWordConverter()
    converter.setConceptRepository(ConceptRepository())
    val words = source.getLines()
      .map(wordStr => wordStr.replaceAll("(^\\[)|(,$)|(\\]$)", ""))
      .map(converter.convert)

    convert(words)

    source.close()
  }

}
