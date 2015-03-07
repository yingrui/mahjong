package websiteschema.mpsegment.tools.ner

import websiteschema.mpsegment.crf.CRFCorpus
import websiteschema.mpsegment.hmm.{Node, HmmViterbi, HmmModel}

import scala.collection.mutable.ListBuffer
import scala.io.Source

object HMMSerialLabelTest extends App {

  val model = new HmmModel
  model.load("segment-hmm.m")
  model.buildViterbi

  val classifier = model.getViterbi

  val rowData = ListBuffer[String]()
  var total = 0
  var correctCount = 0
  Source.fromFile("training.txt").getLines().foreach(line => {
    if (line.trim().isEmpty) {
      val dataAndLabel = rowData.map(l => l.split("\\s"))

      val data = dataAndLabel.map(a => a(0))
      val labels = dataAndLabel.map(a => a(1))

      val result = classifier.calculateWithLog(data).map(n => n.getName())

      for(i <- 0 until data.length) {
        total += 1
        if(result(i) == labels(i)) {
          correctCount += 1
        }
      }

      rowData.clear()
    } else {
      rowData += line
    }
  })

  println(correctCount.toDouble / total.toDouble)
}
