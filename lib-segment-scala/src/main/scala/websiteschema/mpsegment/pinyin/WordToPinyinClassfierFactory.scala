package websiteschema.mpsegment.pinyin

import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.hmm.HmmModel

class WordToPinyinClassfierFactory {

  private val classifier: WordToPinyinClassifier = new WordToPinyinClassifier()

  try {
    val model = new HmmModel()
    model.load(MPSegmentConfiguration().getPinyinModel())
    classifier.setModel(model)
  } catch {
    case ex: Throwable =>
      ex.printStackTrace()
  }

  def getClassifier(): WordToPinyinClassifier = {
    return classifier
  }
}

object WordToPinyinClassfierFactory {
  val instance = new WordToPinyinClassfierFactory()

  def apply() = instance
}
