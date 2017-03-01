package me.yingrui.segment.apps

import java.io.{FileOutputStream, OutputStreamWriter, PrintWriter}

import me.yingrui.segment.core.Word
import me.yingrui.segment.dict.POSUtil
import me.yingrui.segment.tools.CorpusLoader
import me.yingrui.segment.util.FileUtil._

object PFRCorpusToWord2VecCorpus extends App {

  val resource = "./lib-segment/src/test/resources/PFR-199801-utf-8.txt"
  val writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream("pfr-word2vec-corpus.txt"), "utf-8"))
  val loader = CorpusLoader(getResourceAsStream(resource))

  loader.load(expect => {
    val originalString = expect.filter(byPOS).map(_.name).mkString(" ")
    writer.print(originalString + " ")
  })

  private def byPOS(word: Word): Boolean = {
    word.pos != POSUtil.POS_M && word.pos != POSUtil.POS_W && word.pos != POSUtil.POS_T
  }
}
