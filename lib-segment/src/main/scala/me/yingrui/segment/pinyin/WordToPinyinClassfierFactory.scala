package me.yingrui.segment.pinyin

import me.yingrui.segment.conf.MPSegmentConfiguration
import me.yingrui.segment.hmm.HmmModel

class WordToPinyinClassfierFactory {

  val model = new HmmModel
  model.load(MPSegmentConfiguration().getPinyinModel())
  model.buildViterbi
  private val classifier = new WordToPinyinClassifier(model)

  def getClassifier(): WordToPinyinClassifier = {
    return classifier
  }
}

object WordToPinyinClassfierFactory {
  val instance = new WordToPinyinClassfierFactory()

  def apply() = instance
}
