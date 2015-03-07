package websiteschema.mpsegment.crf

import websiteschema.mpsegment.core.{SegmentResult, SegmentWorker}

import scala.collection.mutable.ListBuffer

class CRFSegmentWorker(model: CRFModel) extends SegmentWorker {

  def segment(sen: String): SegmentResult = {
    val document = CRFDocument(sen, model)

    val classifier = new CRFViterbi(model)

    val bestCuts = classifier.calculateResult(document.data).getBestPath
                    .map(l => model.labelRepository.getFeature(l))
                    .map(label => if(label=="S" || label=="B") 1 else 0)

    var from = 0
    val words = new ListBuffer[String]()
    for(i <- 0 until sen.length) {
      if(bestCuts(i) == 1) {
        val word = sen.substring(from, i)
        if (!word.isEmpty) words += word
        from = i
      } else if (i + 1 == sen.length) {
        words += sen.substring(from)
      }
    }

    val result = new SegmentResult(words.length)
    result.setWords(words.toArray)
    result
  }

}

object CRFSegmentWorker {

  private val model = CRFModel("segment-crf.m")

  def apply(): SegmentWorker = new CRFSegmentWorker(model)

  def apply(model: CRFModel): SegmentWorker = new CRFSegmentWorker(model)

}