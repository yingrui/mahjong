package websiteschema.mpsegment.dict

import websiteschema.mpsegment.dict.domain.DomainDictLoader
import websiteschema.mpsegment.dict.domain.DomainDictionary

class SimpleDomainDictLoader extends DomainDictLoader {

  override def load(dict: DomainDictionary) {
    try {
      dict.pushWord("PC机", null, "N", 5, 10001);
      dict.pushWord("个人电脑", "PC机", "N", 5, 10001);
    } catch {
      case e: Throwable =>
        e.printStackTrace();
    }
  }
}
