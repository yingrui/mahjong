package me.yingrui.segment.filter

import me.yingrui.segment.util.StringUtil

class UpperCaseAndHalfShapeFilter(halfShapeAll: Boolean, upperCaseAll: Boolean) extends AbstractSegmentFilter {

  private val upperCaseOrHalfShapeAll = halfShapeAll || upperCaseAll

  override def doFilter() {
    val length = segmentResult.length()
    for (index <- 0 until length) {
      val word = segmentResult.getWord(index)
      segmentResult.setWord(index, filterWord(word))
    }
  }

  private def filterWord(word: String): String = {
    if (upperCaseOrHalfShapeAll) {
      if (halfShapeAll && upperCaseAll) {
        return StringUtil.doUpperCaseAndHalfShape(word)
      }
      if (halfShapeAll) {
        return StringUtil.halfShape(word)
      }
      return StringUtil.toUpperCase(word)
    }
    return word
  }

}
