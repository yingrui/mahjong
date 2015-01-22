package websiteschema.mpsegment.tools.ner

import websiteschema.mpsegment.crf._

object CRFSerialLabelTest extends App {

  val train = true
  val file = "training-100000.txt"
  val model = if (train) {
    val corpusForTrain = CRFCorpus(file)
    val m = CRFModel.build(corpusForTrain)
    CRFModel.save(m, "segment-crf.m")
    m
  } else {
    CRFModel("segment-crf.m")
  }

  println("model loaded")

  val classifier = new CRFViterbi(model)

  var total = 0
  var correctCount = 0
  val corpus = CRFCorpus(file, false, true, model.featureRepository, model.labelRepository)
  println("test corpus loaded")
  corpus.docs.foreach(doc => {
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
          .map(index => {
            def isCurrentOrNextFailed(currentIndex: Int) = {
              val line = doc.rowData(currentIndex)
              val label = model.labelRepository.getFeature(result(currentIndex))
              val failed = !line.endsWith(label)
              if(!failed && currentIndex < result.length - 1) {
                val nextLine = doc.rowData(currentIndex + 1)
                val nextLabel = model.labelRepository.getFeature(result(currentIndex + 1))
                !nextLine.endsWith(nextLabel)
              } else {
                failed
              }
            }

            if (isCurrentOrNextFailed(index)) {
              doc.rowData(index) + " " + model.labelRepository.getFeature(result(index)) + " --"
            } else {
              doc.rowData(index) + " " + model.labelRepository.getFeature(result(index))
            }
          })
        errors.foreach(println(_))
        println()
      } catch {
        case _: Exception =>
      }
    }
  })
  println("total: " + total + " correct: " + correctCount + " error: " + (total - correctCount) + " rate: " + correctCount.toDouble / total.toDouble)

}
