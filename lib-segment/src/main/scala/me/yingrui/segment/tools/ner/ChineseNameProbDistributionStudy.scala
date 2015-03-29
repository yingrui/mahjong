package me.yingrui.segment.tools.ner

import io.Source
import java.io.{File, InputStream}
import me.yingrui.segment.pinyin.WordToPinyinClassfierFactory
import me.yingrui.segment.util.FileUtil._
import me.yingrui.segment.util.CharCheckUtil._
import me.yingrui.segment.util.SerializeHandler
import java.util

object ChineseNameProbDistributionStudy extends App {
  var wordCount = 0
  val pinyinFreq = new java.util.HashMap[String, Int]()
  val wordFreq = new java.util.HashMap[String, Int]()
  val xingSet = new java.util.HashSet[String]()
  val nameLabels = new java.util.HashMap[String, java.util.HashMap[String, Int]]()
  val namePattern = "([FM]) (.+)".r
  val fuxingSet = Set("欧阳", "上官", "诸葛", "皇甫", "司徒", "申屠", "慕容", "司马", "令狐", "尉迟", "宇文", "夏侯", "东方", "轩辕", "淳于", "赫连", "公孙", "澹台", "公冶", "司空", "长孙", "闻人", "万俟", "濮阳", "公羊", "宗政", "徐离", "单于", "仲孙", "太叔", "澹台")

  val resourceCnNames: InputStream = getResourceAsStream("chinese_names_all.txt")
  Source.fromInputStream(resourceCnNames).getLines().foreach(str => {
    namePattern findFirstIn str match {
      case Some(namePattern(gender, name)) => statistic(name, gender)
      case None =>
    }
  })

  println(pinyinFreq)
  println(wordFreq)

  val dumper = SerializeHandler(new File("ner_cn.dat"), SerializeHandler.WRITE_ONLY)

  dumper.serializeInt(wordCount)
  dumper.serializeMapStringInt(pinyinFreq)
  dumper.serializeMapStringInt(wordFreq)
  dumper.serializeArrayString(xingSet.toArray(new Array[String](0)))

  dumper.serializeInt(nameLabels.keySet().size())
  for(word <- nameLabels.keySet().toArray(Array[String]())) {
    dumper.serializeString(word)
    dumper.serializeMapStringInt(nameLabels.get(word))
  }

  private def statistic(name: String, gender: String) {
    wordCount += name.length()
    statisticPinyin(name)
    val xing = if(fuxingSet.contains(name.substring(0, 2))) name.substring(0, 2) else name.substring(0, 1)
    val ming = name.substring(xing.length)
    xingSet.add(xing)
    markLabel(xing, "B")
    mapPlusOne(wordFreq, xing)

    if(ming.length > 1) {
      markLabel(ming.substring(0, 1), "C")
      mapPlusOne(wordFreq, ming.substring(0, 1))
      markLabel(ming.substring(1, 2), "D")
      mapPlusOne(wordFreq, ming.substring(1, 2))
    } else if(ming.length == 1) {
      markLabel(ming, "E")
      mapPlusOne(wordFreq, ming)
    }
  }

  private def statisticPinyin(name: String) {
    val pinyinList = WordToPinyinClassfierFactory().getClassifier().classify(name)
    pinyinList.foreach(pinyin => {
      if (pinyin.matches("[a-z1-9]+")) {
        mapPlusOne(pinyinFreq, pinyin)
      } else if (isChinese(pinyin)) {
        mapPlusOne(pinyinFreq, "unknown")
      }
    })
  }

  private def mapPlusOne(map: java.util.Map[String, Int], word: String) {
    val value = if (map.containsKey(word)) map.get(word) else 0
    map.put(word, value + 1)
  }

  private def markLabel(word: String, label: String) {
    if (!nameLabels.containsKey(word)) {
      val m = new util.HashMap[String, Int]()
      nameLabels.put(word, m)
    }
    mapPlusOne(nameLabels.get(word), label)
  }
}