package me.yingrui.segment.tools

import me.yingrui.segment.concept.Concept
import me.yingrui.segment.concept.ConceptRepository
import me.yingrui.segment.concept.ConceptTree
import me.yingrui.segment.dict.{POSArray, POS, WordImpl, DictionaryFactory}

import java.io._
import collection.mutable.ListBuffer

object ConceptLoader extends App {
  DictionaryFactory().loadDictionary()
  DictionaryFactory().loadDomainDictionary()
  DictionaryFactory().loadUserDictionary()

  val fileNames = Array[String](
    "/Users/twer/workspace/nlp/ccd/noun-list.txt",
    "/Users/twer/workspace/nlp/ccd/verb-list.txt",
    "/Users/twer/workspace/nlp/ccd/adj-list.txt")
  var file1 = new File(fileNames(0))
  load(file1, ConceptRepository().getNounConceptTree(), "N")

  var file2 = new File(fileNames(1))
  load(file2, ConceptRepository().getVerbConceptTree(), "V")

  var file3 = new File(fileNames(2))
  load(file3, ConceptRepository().getAdjConceptTree(), "A")

  var writer = new DictionaryWriter(new File("dict.txt"))
  writer.write(DictionaryFactory().getCoreDictionary())
  writer.close()

  def load(file: File ,  nounConceptTree: ConceptTree, pos: String) {
    val reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"))
    var line = reader.readLine()
    while (null != line) {
      val array = line.split(" ")
      if (null != array && array.length == 2) {
        val wordName = array(0)
        val concepts = array(1).split("\\|")
        var word = DictionaryFactory().getCoreDictionary().getWord(wordName).asInstanceOf[WordImpl]
        if (word == null) {
          println("missing word: " + wordName + " " + array(1))
          word = new WordImpl(wordName)
          val posArray = new POSArray()
          posArray.add(POS(pos, 50))
          word.setPosArray(posArray)
          DictionaryFactory().getCoreDictionary().addWord(word)
        }
        if (word != null) {
          val conceptList = ListBuffer[Concept]()
          for (conceptStr <- concepts) {
            if (!conceptStr.equals("null")) {
              val concept = nounConceptTree.getConceptByDescription(conceptStr)
              if (null != concept) {
                conceptList += (concept)
              }
            }
          }
          word.setConcepts(conceptList.toArray)
        }
      }
      line = reader.readLine()
    }
    reader.close()
  }
}