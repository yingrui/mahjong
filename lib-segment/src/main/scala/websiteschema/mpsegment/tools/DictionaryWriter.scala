package websiteschema.mpsegment.tools

import websiteschema.mpsegment.dict.DictionaryFactory
import websiteschema.mpsegment.dict.IDictionary

import java.io._

class DictionaryWriter(file: File) {

  private val writer: BufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"))

  def write(dictionary: IDictionary) {
    val iterator = dictionary.iterator()
    if (null != dictionary) {
      writer.write("[")
      for (i <- 0 until iterator.size) {
        val word = iterator(i)
        val converter = new WordStringConverter(word)
        writer.write(converter.convertToString())
        if(i < (iterator.size - 1)) writer.write(",\n")
      }
      writer.write("]")
    }
  }

  def close() {
    writer.close()
  }

}

object DictionaryWriter extends App {

  def writeDictionary(dict: IDictionary, file: String) {
    println("Writing dictionary to file: " + file)
    val dictionaryWriter = new DictionaryWriter(new File(file))
    dictionaryWriter.write(dict)
    dictionaryWriter.close()
  }

  DictionaryFactory().loadDictionary()
  DictionaryFactory().loadEnglishDictionary()
  writeDictionary(DictionaryFactory().getCoreDictionary(), "dict-1.txt")
  writeDictionary(DictionaryFactory().getEnglishDictionary, "dict-en-1.txt")

}