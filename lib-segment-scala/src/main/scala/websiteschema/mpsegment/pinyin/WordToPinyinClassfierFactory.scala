package websiteschema.mpsegment.pinyin

import websiteschema.mpsegment.conf.MPSegmentConfiguration

class WordToPinyinClassfierFactory {

  private val classifier: WordToPinyinClassifier = new WordToPinyinClassifier()

  try {
    val model = new WordToPinyinModel()
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
