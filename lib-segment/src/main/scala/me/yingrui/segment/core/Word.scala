package me.yingrui.segment.core

import me.yingrui.segment.dict.POSUtil

class Word {
  var name: String = ""
  var pinyin: String = ""
  var pos: Int = POSUtil.POS_UNKOWN
  var domainType: Int = 0
  var concept: String = ""
  var start: Int = 0
  var end: Int = 0

  def length: Int = name.length
}
