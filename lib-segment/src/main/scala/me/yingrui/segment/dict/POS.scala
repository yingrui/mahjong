package me.yingrui.segment.dict

class POS extends Comparable[POS] {

  var name = ""
  var count = 0

  override def compareTo(obj: POS): Int = {
    if (obj != null) {
      return name.compareTo(obj.name)
    } else {
      return 1
    }
  }

  def equals(obj: POS): Boolean = {
    if (obj != null) {
      return name.equals(obj.name)
    } else {
      return false
    }
  }

  override def toString(): String = {
    val stringBuilder = new StringBuilder()
    stringBuilder.append((new StringBuilder("\u8BCD\u6027\u540D\u79F0=")).append(name).append("\n").toString())
    stringBuilder.append((new StringBuilder("\u51FA\u73B0\u6B21\u6570=")).append(count).append("\n").toString())
    return stringBuilder.toString()
  }

  def toDBFString(): String = {
    val stringBuilder = new StringBuilder()
    stringBuilder.append((new StringBuilder(" ")).append(name).toString())
    stringBuilder.append((new StringBuilder(" ")).append(count).toString())
    return stringBuilder.toString()
  }

  def getCount(): Int = {
    return count
  }

  def setCount(i: Int) {
    count = i
  }

  def getName(): String = {
    return name
  }

  def setName(s: String) {
    name = s
  }
}

object POS {

  def apply(name: String, count: Int) = {
    val pos = new POS()
    pos.setCount(count)
    pos.setName(name)
    pos
  }
}