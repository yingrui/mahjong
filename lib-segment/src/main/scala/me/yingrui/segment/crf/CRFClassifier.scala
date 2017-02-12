package me.yingrui.segment.crf

class CRFClassifier(model: CRFModel) {

  def findBestLabels(observeList: Seq[String]): Seq[String] = {
    val document = CRFDocument(observeList, model)
    val viterbi = new CRFViterbi(model)
    val labels = viterbi.calculateResult(document.data).getBestPath.map(l => model.labelRepository.getFeature(l))
    labels
  }

  def isFeatureExists(feature: String): Boolean = model.featureRepository.getFeatureId(feature) > 0

}
