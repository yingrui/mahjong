package me.yingrui.segment.util

object StringUtil {

    def halfShape(original: Char): Char = {
        if (isFullShapedChar(original)) {
            return (original - 65248).toChar
        }

        return original
    }

    def isFullShapedChar(original: Char): Boolean = {
        return (original >= '\uFF21' && original <= '\uFF41' || original >= '\uFF3A' && original <= '\uFF5A' || original >= '\uFF10' && original <= '\uFF19')
    }

    def halfShape(original: String): String = {
        val chArray = original.toArray
        for (i1 <- 0 until chArray.length()) {
            if (isCharAlphabeticalOrDigital(chArray(i1))) {
                chArray(i1) = StringUtil.halfShape(chArray(i1))
            }
        }

        return new String(chArray)
    }

    def toUpperCase(original: String): String = {
        val chArray = original.toArray
        for (i1 <- 0 until chArray.length) {
            if (isCharAlphabeticalOrDigital(chArray(i1))) {
                chArray(i1) = StringUtil.toUpperCase(chArray(i1))
            }
        }

        return new String(chArray)
    }

    def toUpperCase(original: Char): Char = {
        if (isLowerCaseChar(original)) {
            return (original - 32).toChar
        }
        return original
    }

    def isLowerCaseChar(original: Char): Boolean = {
        return original >= 'a' && original <= 'z'
    }

    def doUpperCaseAndHalfShape(original: String): String = {
        val chArray = original.toArray
        for (i1 <- 0 until chArray.length) {
            if (isCharAlphabeticalOrDigital(chArray(i1))) {
                chArray(i1) = StringUtil.halfShape(chArray(i1))
                chArray(i1) = StringUtil.toUpperCase(chArray(i1))
            }
        }

        return new String(chArray)
    }

    def isCharAlphabeticalOrDigital(ch: Char): Boolean = {
        return (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' || ch >= '0' && ch <= '9' || ch >= '\uFF21' && ch <= '\uFF41' || ch >= '\uFF3A' && ch <= '\uFF5A' || ch >= '\uFF10' && ch <= '\uFF19')
    }

    def isCharAlphabetical(ch: Char): Boolean = {
      return (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' || ch >= '\uFF21' && ch <= '\uFF41' || ch >= '\uFF3A' && ch <= '\uFF5A')
    }
}
