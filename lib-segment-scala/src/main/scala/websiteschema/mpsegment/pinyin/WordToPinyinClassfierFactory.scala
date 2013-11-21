package websiteschema.mpsegment.pinyin

import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.hmm.{ViterbiImpl, HmmModel}

class WordToPinyinClassfierFactory {

  val model = new HmmModel(new ViterbiImpl)
  model.load(MPSegmentConfiguration().getPinyinModel())
  private val classifier = new WordToPinyinClassifier(model)

  def getClassifier(): WordToPinyinClassifier = {
    return classifier
  }
}

object WordToPinyinClassfierFactory {
  val instance = new WordToPinyinClassfierFactory()

  def apply() = instance
}
