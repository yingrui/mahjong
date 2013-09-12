package websiteschema.mpsegment.tools.ner

import io.Source
import java.io.{File, InputStream}
import websiteschema.mpsegment.pinyin.WordToPinyinClassfierFactory
import websiteschema.mpsegment.util.FileUtil._
import websiteschema.mpsegment.util.CharCheckUtil._
import websiteschema.mpsegment.util.SerializeHandler

object ChineseNameSpellStudy extends App {
  var wordCount = 0
  val pinyinFreq = new java.util.HashMap[String,Int]()
  val wordFreq = new java.util.HashMap[String,Int]()
  val namePattern = "([FM]) (.+)".r

  val resourceCnNames: InputStream = getResourceAsStream("chinese_names_all.txt")
  Source.fromInputStream(resourceCnNames).getLines().foreach(str => {
    namePattern findFirstIn str match {
      case Some(namePattern(gender, name)) => statistic(name, gender)
      case None =>
    }
  })

  println(pinyinFreq)
  println(wordFreq)

  val dumper = SerializeHandler(new File("ner_cn.dat"), SerializeHandler.MODE_WRITE_ONLY)

  dumper.serializeInt(wordCount)
  dumper.serializeMapStringInt(pinyinFreq)
  dumper.serializeMapStringInt(wordFreq)

  def statistic(name: String, gender: String) {
    wordCount += name.length()
    statisticPinyin(name)
    name.foreach { ch =>
      mapPlusOne(wordFreq, ch.toString)
    }
  }

  def statisticPinyin(name: String) {
    val pinyinList = WordToPinyinClassfierFactory().getClassifier().classify(name)
    pinyinList.foreach(pinyin => {
      if (pinyin.matches("[a-z1-9]+")) {
        mapPlusOne(pinyinFreq, pinyin)
      } else if (isChinese(pinyin)) {
        mapPlusOne(pinyinFreq, "unknown")
      }
    })
  }

  def mapPlusOne(map: java.util.Map[String, Int], key: String) {
    val value = if (map.containsKey(key)) map.get(key) else 0
    map.put(key, value + 1)
  }
}