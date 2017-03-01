package me.yingrui.segment.tools

import me.yingrui.segment.core.SegmentResult
import me.yingrui.segment.dict.POSUtil
import me.yingrui.segment.pinyin.WordToPinyinClassfierFactory

import java.io._

object PFRCorpusPrinter extends App {

  var inputStream = getInputStream(args)
  var out = new PrintStream(new File("corpus.txt"))
  var loader = CorpusLoader(inputStream)
  loader.eliminateDomainType(POSUtil.POS_NR)
  var result = loader.readLine()
  while (result != null) {
    print(result, out)
    result = loader.readLine()
  }

  def print(result: SegmentResult, out: PrintStream) {
    WordToPinyinClassfierFactory().getClassifier().classify(result)
    val containsDomainWord = !result.getWords().forall(word => word.domainType == 0)

    if (containsDomainWord) {
      for (i <- 0 until result.length) {
        val domainType = result.getDomainType(i)
        out.println(result.getWord(i) + "\t" +
          result.getPinyin(i) + "\t" +
          POSUtil.getPOSString(result.getPOS(i)) + "\t" +
          result.getConcept(i) + "\t" +
          (if (domainType != 0) POSUtil.getPOSString(domainType) else "O") )
      }
      out.println()
    }
  }

  def getInputStream(args: Array[String]): InputStream = {
    var inputStream: InputStream = null
    if (args.length > 0) {
      inputStream = new FileInputStream(args(0))
    } else {
      inputStream = getClass().getClassLoader().getResourceAsStream("PFR-199801-utf-8.txt")
    }
    return inputStream
  }
}
