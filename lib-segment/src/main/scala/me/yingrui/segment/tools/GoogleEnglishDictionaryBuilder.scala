package me.yingrui.segment.tools

import java.io._
import java.util.zip.GZIPInputStream
import me.yingrui.segment.dict._

object GoogleEnglishDictionaryBuilder extends App {

  var dictionary: TrieDictionary = null

  val directory = if (args.length > 0) args(0) else "/Users/twer/workspace/nlp/google-ngram/en"
  val dir = new File(directory)
  if (dir.isDirectory()) {
    scanDirectory(dir)
  } else {
    System.exit(1)
  }

  def scanDirectory(dir: File) {
    val files = dir.listFiles()
    for (file <- files) {
      println(file.getName())
      val pattern = """googlebooks-eng-all-1gram-20120701-(o).gz""".r
      try {
        val pattern(alphabet) = file.getName()
        dictionary = new TrieDictionary()
        loadFile(file)
        save("dict-en-"+alphabet+".dat")
      } catch {
        case _: Throwable =>
      }
    }
  }

  def loadFile(file: File) {
    val reader = getBufferedReader(file)
    var line = reader.readLine()
    while (line != null) {
      parseLine(line)
      line = reader.readLine()
    }
    reader.close()
  }

  def getBufferedReader(file: File): BufferedReader = {
    val inputStream = getInputStream(file)
    new BufferedReader(new InputStreamReader(inputStream, "utf-8"))
  }

  def getInputStream(file: File): InputStream = {
    if (file.getName().endsWith(".gz")) {
      return new GZIPInputStream(new FileInputStream(file))
    }
    return new FileInputStream(file)
  }

  def parseLine(line: String) {
    val elements = line.split("\\s+")
    if (elements.length == 4) {
      val wordPosString = elements(0)
      val wordPos = """([A-z]+[0-9]?[A-z]*)_([A-Z]+)""".r
      try {
        val wordPos(word, pos) = wordPosString
        val year = elements(1).toInt
        val count = elements(2).toInt
        val documentCount = elements(3).toInt
        statisticBigram(word.toLowerCase, pos, count)
      } catch {
        case ex: scala.MatchError =>
        case ex: Throwable => ex.printStackTrace()
      }
    }
  }

  def statisticBigram(wordName: String, partOfSpeech: String, count: Int) {
    var word = dictionary.getWord(wordName)
    if (null == word) {
      val wordImpl = new WordImpl(wordName)
      wordImpl.setPosArray(new POSArray())
      word = wordImpl
      dictionary.addWord(word)
    }
    val posArray = word.getPOSArray()
    posArray.add(POS(partOfSpeech, count))
  }

  def getWordFirstTwoChars(word: String): String = {
    if (word.length() > 2) word.substring(word.length() - 2) else word
  }

  def save(filename: String) {
    val writer: DictionaryWriter = new DictionaryWriter(new File(filename))
    writer.write(dictionary)
    writer.close()
  }
}
