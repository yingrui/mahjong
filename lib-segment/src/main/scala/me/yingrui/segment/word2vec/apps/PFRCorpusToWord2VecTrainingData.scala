package me.yingrui.segment.word2vec.apps

import java.io.{File, PrintWriter}

import me.yingrui.segment.tools.PFRCorpusLoader
import me.yingrui.segment.util.CharCheckUtil._
import me.yingrui.segment.util.FileUtil._

object PFRCorpusToWord2VecTrainingData extends App {

  val resource = if (args.indexOf("--train-file") >= 0) args(args.indexOf("--train-file") + 1) else "lib-segment/src/test/resources/PFR-199801-utf-8.txt"
  val loader = PFRCorpusLoader(getResourceAsStream(resource))

  val saveFile = if (args.indexOf("--save-file") >= 0) args(args.indexOf("--save-file") + 1) else "words.txt"
  val printer = new PrintWriter(new File(saveFile), "utf-8")

  loader.load { sentence =>
    val buffer = new StringBuilder
    var i = 0
    while (i < sentence.length()) {
      val word = sentence(i).name

      for(ch <- word; if isChinese(ch)) {
        buffer.append(ch).append(" ")
      }

      i += 1
    }
    printer.print(buffer)
    printer.flush()
  }
}
