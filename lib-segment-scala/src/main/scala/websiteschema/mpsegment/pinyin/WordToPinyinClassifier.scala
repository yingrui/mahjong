package websiteschema.mpsegment.pinyin

import websiteschema.mpsegment.core.SegmentResult
import websiteschema.mpsegment.hmm.Node
import websiteschema.mpsegment.util.CharCheckUtil

import collection.mutable.ListBuffer

class WordToPinyinClassifier {

  var model: WordToPinyinModel = null

  def setModel(model: WordToPinyinModel) {
    this.model = model
  }

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
    return list.drop(start).dropRight(list.size - end)
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
    return stringBuilder.toString()
  }

  def classify(o: String): List[String] = {
    var observeList = List[String]()
    for (i <- 0 until o.length) {
      observeList = observeList ++ List[String](o.charAt(i).toString)
    }
    return classify(observeList)
  }

  private def classify(o: List[String]): List[String] = {
    assert(null != o && o.size > 0)
    val sections = findSectionsByUnknownCharacter(o)
    return classifySectionList(sections)
  }

  private def classifySectionList(sections: List[Section]): List[String] = {
    val result = ListBuffer[String]()
    for (section <- sections) {
      if (section.hasKnownCharacters()) {
        val observeCharacters = section.characters.toList
        result ++= convert(classifyOberveList(observeCharacters))
      }
      if (section.hasUnknwonCharacter()) {
        result += (section.unknownChar)
      }
    }
    return result.toList
  }

  private def classifyOberveList(observeCharacters: List[String]): List[Node] = {
    return model.getViterbi().calculateWithLog(observeCharacters)
  }

  private def findSectionsByUnknownCharacter(o: List[String]): List[Section] = {
    var lastSectorPos = 0
    val sections = ListBuffer[Section]()
    for (i <- 0 until o.size) {
      val ch = o(i)
      val knownObserveNode = model.containsObserve(ch)
      if (!knownObserveNode) {
        println(ch)
        val section = new Section(o, lastSectorPos, i)
        sections += (section)
        lastSectorPos = i + 1
      }
    }
    val sec = new Section(o, lastSectorPos, o.size)
    sections += (sec)
    return sections.toList
  }

  private def convert(nodeList: List[Node]): List[String] = {
    val result = ListBuffer[String]()
    for (node <- nodeList) {
      result += (node.getName())
    }
    return result.toList
  }

  class Section(o: List[String], start: Int, end: Int) {
    var characters = ListBuffer[String]()
    var unknownChar: String = null

    if (end > start && start < o.size) {
      characters = ListBuffer[String]()
      for (i <- 0 until end - start) {
        characters += o(i + start)
      }
      unknownChar = if (end < o.size) o(end) else null
    } else if (end == start) {
      unknownChar = if (end < o.size) o(end) else null
    }

    def hasKnownCharacters(): Boolean = {
      null != characters
    }

    def hasUnknwonCharacter(): Boolean = {
      null != unknownChar
    }
  }

}

