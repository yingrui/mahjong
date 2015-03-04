package websiteschema.mpsegment.tools.ner

import java.io.{File, PrintWriter}

import websiteschema.mpsegment.tools.PFRCorpusLoader
import websiteschema.mpsegment.util.FileUtil._
import websiteschema.mpsegment.util._

object PFRCorpusToSegmentSerialLabel extends App {

  val resource = "./src/test/resources/PFR-199801-utf-8.txt"
  val loader = PFRCorpusLoader(getResourceAsStream(resource))

  val printer = new PrintWriter(new File("training.txt"), "utf-8")

  loader.load { sentence =>

    var i = 0
    while (i < sentence.length()) {
      val word = sentence(i).name
      if(WordUtil.isAlphaNumericWithUnderScore_Slash_Colon(word)) {
        printer.println(word + "\t" + "S")
      } else if(word.length == 1) {
        printer.println(word + "\t" + "S")
      } else if(word.length == 2) {
        printer.println(word(0) + "\t" + "B")
        printer.println(word(1) + "\t" + "E")
      } else {
        printer.println(word(0) + "\t" + "B")
        for(ch <- word.substring(1, word.length - 1)) {
          printer.println(ch + "\t" + "M")
        }
        printer.println(word.last + "\t" + "E")
      }

      i += 1
    }
    printer.println()
    printer.flush()
  }
}
