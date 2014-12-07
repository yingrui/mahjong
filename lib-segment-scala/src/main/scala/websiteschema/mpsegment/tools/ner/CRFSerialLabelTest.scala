package websiteschema.mpsegment.tools.ner

import websiteschema.mpsegment.crf.{CRFViterbi, CRFModel, CRFCorpus}

object CRFSerialLabelTest extends App {

  val corpusForTrain = CRFCorpus("training.txt", true)
  val model = CRFModel.build(corpusForTrain)

  val classifier = new CRFViterbi(model)

  var total = 0
  var correctCount = 0
  val corpus = CRFCorpus("training.txt", true, model.featureRepository, model.labelRepository)
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