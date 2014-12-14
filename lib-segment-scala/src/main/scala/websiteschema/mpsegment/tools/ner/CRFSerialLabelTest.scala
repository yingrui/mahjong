package websiteschema.mpsegment.tools.ner

import websiteschema.mpsegment.crf.{CRFDocument, CRFViterbi, CRFModel, CRFCorpus}

object CRFSerialLabelTest extends App {

  val train = true
  val model = if (train) {
    val corpusForTrain = CRFCorpus("training.txt")
    val m = CRFModel.build(corpusForTrain)
    CRFModel.save(m, "segment-crf.m")
    m
  } else {
    CRFModel("segment-crf.m")
  }

  val classifier = new CRFViterbi(model)

  var total = 0
  var correctCount = 0
  val corpus = CRFCorpus("training.txt", false, true, model.featureRepository, model.labelRepository)
  for (i <- 0 until corpus.docs.length) {
    val doc = corpus.docs(i)
    val result = classifier.calculateResult(doc.data).getBestPath
    total += result.length
    var success = true
    for (index <- 0 until result.length) yield {
      val label = doc.label(index)
      correctCount += (if (label == result(index)) 1 else 0)

      if (label != result(index)) success = false
    }

    if (!success) {
      try {
        val errors = (0 until result.length)
          .map(index => doc.rowData(index) + " " + model.labelRepository.getFeature(result(index)))
        errors.foreach(println(_))
        println()
      } catch {
        case _ =>
      }
    }
  }
  println("total: " + total + " correct: " + correctCount + " error: " + (total - correctCount) + " rate: " + correctCount.toDouble / total.toDouble)

}
