package me.yingrui.segment.crf

class CRFClassifier(model: CRFModel) {

  def findBestLabels(result: Seq[String]): Array[String] = {
    val document = CRFDocument(result, model)
    val classifier = new CRFViterbi(model)
    val labels = classifier.calculateResult(document.data).getBestPath.map(l => model.labelRepository.getFeature(l))
    labels
  }

}
