package me.yingrui.segment.hmm

import collection.mutable.ListBuffer

class HmmClassifier(val model: HmmModel) {

  def classify(o: Seq[String]): List[String] = {
    assert(null != o && o.size > 0)
    val sections = findSectionsByUnknownCharacter(o)
    classifySectionList(sections)
  }

  private def classifySectionList(sections: Seq[Section]): List[String] = {
    val result = ListBuffer[String]()
    for (section <- sections) {
      if (section.hasKnownCharacters()) {
        val observeCharacters = section.characters
        result ++= convert(classifyOberveList(observeCharacters))
      }
      if (section.hasUnknwonCharacter()) {
        result += (section.unknownChar)
      }
    }
    result.toList
  }

  private def classifyOberveList(observeCharacters: Seq[String]): Seq[Node] = {
    model.getViterbi.calculateWithLog(observeCharacters)
  }

  private def findSectionsByUnknownCharacter(o: Seq[String]): List[Section] = {
    var lastSectorPos = 0
    val sections = ListBuffer[Section]()
    for (i <- 0 until o.size) {
      val ch = o(i)
      val knownObserveNode = model.containsObserve(ch)
      if (!knownObserveNode) {
        val section = new Section(o, lastSectorPos, i)
        sections += (section)
        lastSectorPos = i + 1
      }
    }
    val sec = new Section(o, lastSectorPos, o.size)
    sections += (sec)
    sections.toList
  }

  private def convert(nodeList: Seq[Node]): List[String] = {
    val result = ListBuffer[String]()
    for (node <- nodeList) {
      result += (node.getName())
    }
    result.toList
  }

  class Section(o: Seq[String], start: Int, end: Int) {
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
