package me.yingrui.segment.dict

import me.yingrui.segment.dict.domain.DomainDictLoader
import me.yingrui.segment.dict.domain.DomainDictionary

class SimpleDomainDictLoader extends DomainDictLoader {

  override def load(dict: DomainDictionary) {
    try {
      dict.pushWord("PC机", null, "N", 5, 10001)
      dict.pushWord("个人电脑", "PC机", "N", 5, 10001)
      dict.pushWord("复方", null, "N", 5, 10001)
      dict.pushWord("复方青黛胶囊", null, "N", 5, 10001)
    } catch {
      case e: Throwable =>
        e.printStackTrace()
    }
  }
}
