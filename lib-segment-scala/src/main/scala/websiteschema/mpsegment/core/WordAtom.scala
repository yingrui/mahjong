package websiteschema.mpsegment.core

import websiteschema.mpsegment.dict.POSUtil

class WordAtom {
  var word: String = ""
  var pinyin: String = ""
  var pos: Int = POSUtil.POS_UNKOWN
  var domainType: Int = 0
  var concept: String = ""
  var start: Int = 0
  var end: Int = 0

  def length: Int = word.length
}
