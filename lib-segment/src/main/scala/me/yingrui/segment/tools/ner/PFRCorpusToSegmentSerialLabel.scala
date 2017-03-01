package me.yingrui.segment.tools.ner

import java.io.{File, PrintWriter}

import me.yingrui.segment.tools.CorpusLoader
import me.yingrui.segment.util.FileUtil._
import me.yingrui.segment.util._

object PFRCorpusToSegmentSerialLabel extends App {

  val resource = if (args.indexOf("--train-file") >= 0) args(args.indexOf("--train-file") + 1) else "lib-segment/src/test/resources/PFR-199801-utf-8.txt"
  val loader = CorpusLoader(getResourceAsStream(resource))

  val saveFile = if (args.indexOf("--save-file") >= 0) args(args.indexOf("--save-file") + 1) else "training.txt"

  val printer = new PrintWriter(new File(saveFile), "utf-8")

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
