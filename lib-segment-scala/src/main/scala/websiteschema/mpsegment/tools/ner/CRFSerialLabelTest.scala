package websiteschema.mpsegment.tools.ner

import websiteschema.mpsegment.crf.{CRFViterbi, CRFModel, CRFCorpus}

object CRFSerialLabelTest extends App {

  val corpus = CRFCorpus("training.txt")
  val model = CRFModel.build(corpus)

  val classifier = new CRFViterbi(model)

  var total = 0
  var correctCount = 0
  for(i <- 0 until corpus.docs.length) {
    val result = classifier.calculateResult(corpus.docs(i).data)
    val path = result.getBestPath
    total += path.length
    for(index <- 0 until path.length) {
      val label = corpus.docs(i).label(index)
      correctCount += (if(label == path(index)) 1 else 0)
    }
  }
  println(correctCount.toDouble / total.toDouble)

}
