package me.yingrui.segment.pinyin

import me.yingrui.segment.core.SegmentResult
import me.yingrui.segment.hmm.{HmmClassifier, HmmModel}
import me.yingrui.segment.util.CharCheckUtil
import me.yingrui.segment.util.FileUtil._

import scala.io.Source

class WordToPinyinClassifier(val model: HmmModel) {

  private val classifier = new HmmClassifier(model)

  def classify(result: SegmentResult) {
    try {
      val originalStr = result.toOriginalString()
      val pinyinList = classify(originalStr)
      var pos = 0
      for (i <- 0 until result.length) {
        val wordLength = result.getWord(i).length()
        val pinyin = join(subList(pinyinList, pos, pos + wordLength), "'", result.getWord(i))
        pos += wordLength
        result.setPinyin(i, pinyin)
      }
    } catch {
      case ex: Throwable =>
        System.err.println(ex)
    }
  }

  private def subList(list: List[String], start: Int, end: Int): List[String] = {
    list.drop(start).dropRight(list.size - end)
  }

  private def join(list: List[String], sep: String, word: String): String = {
    val stringBuilder = new StringBuilder()
    for (i <- 0 until list.size) {
      val str = list(i)
      stringBuilder.append(str)
      if (null != sep && i < list.size - 1 && CharCheckUtil.isChinese(word.charAt(i + 1))) {
        stringBuilder.append(sep)
      }
    }
    stringBuilder.toString()
  }

  def classify(o: String) = {
    var observeList = List[String]()
    for (i <- 0 until o.length) {
      observeList = observeList ++ List[String](o.charAt(i).toString)
    }
    classifier.classify(observeList)
  }

  def loadDictionary(dictFile : String) {
    val source = Source.fromInputStream(getResourceAsStream(dictFile))
    for (line <- source.getLines()) {
      val regex = "([^ ]+) *= *([^ ]+)".r
      regex findFirstIn line match {
        case Some(regex(han, pinyin)) => model.add(han, pinyin)
        case None =>
      }
    }
  }
}

