package websiteschema.mpsegment.tools

import websiteschema.mpsegment.dict.DictionaryFactory
import websiteschema.mpsegment.dict.IDictionary

import java.io._

class DictionaryWriter(file: File) {

  private val writer: BufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"))

  def write(dictionary: IDictionary) {
    val iterator = dictionary.iterator()
    if (null != dictionary) {
      for (word <- iterator) {
        val converter = new WordStringConverter(word)
        writer.write(converter.convertToString() + "\n")
      }
    }
  }

  def close() {
    writer.close()
  }

}

object DictionaryWriter extends App {
  var file = "dict.txt"
  if (null != args && args.length > 0) {
    file = args(0)
  }

  println("All words will output to file: " + file)
  val writer1 = new DictionaryWriter(new File(file))
  DictionaryFactory().loadDictionary()
  writer1.write(DictionaryFactory().getCoreDictionary())
  writer1.close()
}