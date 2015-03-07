package websiteschema.mpsegment.lang.en


/**
 * http://tartarus.org/~martin/PorterStemmer/java.txt
 */
class PorterStemmer {
  private var b: Array[Char] = null
  private var i = 0
  /* offset into b */
  var i_end = 0
  /* offset to end of stemmed word */
  var j = 0
  var k = 0
  private val INC = 50

  /* unit of size whereby b is increased */

  b = new Array[Char](INC)
  i = 0
  i_end = 0

  /**
   * Add a character to the word being stemmed.  When you are finished
   * adding characters, you can call stem(void) to stem the word.
   */

  def add(ch: Char) {
    if (i == b.length) {
      val new_b = new Array[Char](i + INC)
      for (c <- 0 until i) new_b(c) = b(c)
      b = new_b
    }
    b(i) = ch
    i += 1
  }


  /**
   * Adds wLen characters to the word being stemmed contained in a portion
   * of a Array[Char] array. This is like repeated calls of add(Char ch), but
   * faster.
   */

  def add(w: Array[Char], wLen: Int) {
    if (i + wLen >= b.length) {
      val new_b = new Array[Char](i + wLen + INC)
      for (c <- 0 until i) new_b(c) = b(c)
      b = new_b
    }

    for (c <- 0 until wLen) {
      b(i) = w(c); i += 1
    }
  }

  /**
   * After a word has been stemmed, it can be retrieved by toString(),
   * or a reference to the internal buffer can be retrieved by getResultBuffer
   * and getResultLength (which is generally more efficient.)
   */
  override def toString(): String = {
    return new String(b, 0, i_end)
  }

  /**
   * Returns the length of the word resulting from the stemming process.
   */
  def getResultLength(): Int = {
    return i_end
  }

  /**
   * Returns a reference to a character buffer containing the results of
   * the stemming process.  You also need to consult getResultLength()
   * to determine the length of the result.
   */
  def getResultBuffer(): Array[Char] = {
    return b
  }

  /* cons(i) is true <=> b(i) is a consonant. */

  private def cons(i: Int): Boolean = {
    b(i) match {
      case 'a' => false
      case 'e' => false
      case 'i' => false
      case 'o' => false
      case 'u' => false
      case 'y' =>
        return if (i == 0) return true else return !cons(i - 1)
      case _ =>
        return true
    }
  }

  /* m() measures the number of consonant sequences between 0 and j. if c is
     a consonant sequence and v a vowel sequence, and <..> indicates arbitrary
     presence,

        [c][v]       gives 0
        [c]vc[v]     gives 1
        [c]vcvc[v]   gives 2
        [c]vcvcvc[v] gives 3
        ....
  */

  private def m(): Int = {
    var n = 0
    var i = 0
    if (i > j) return n
    while (cons(i)) {
      if (i > j) return n
      i += 1
    }
    i += 1
    while (true) {
      if (i > j) return n
      while (!cons(i)) {
        if (i > j) return n
        i += 1
      }
      i += 1
      n += 1
      if (i > j) return n
      while (cons(i)) {
        if (i > j) return n
        i += 1
      }
      i += 1
    }
    return n
  }

  /* vowelinstem() is true <=> 0,...j contains a vowel */

  private def vowelinstem(): Boolean = {
    for (i <- 0 to j) if (!cons(i)) return true
    return false
  }

  /* doublec(j) is true <=> j,(j-1) contain a Double consonant. */

  private def doublec(j: Int): Boolean = {
    if (j < 1) return false
    if (b(j) != b(j - 1)) return false
    return cons(j)
  }

  /* cvc(i) is true <=> i-2,i-1,i has the form consonant - vowel - consonant
     and also if the second c is not w,x or y. this is used when trying to
     restore an e at the end of a short word. e.g.

        cav(e), lov(e), hop(e), crim(e), but
        snow, box, tray.

  */

  private def cvc(i: Int): Boolean = {
    if (i < 2 || !cons(i) || cons(i - 1) || !cons(i - 2))
      return false

    val ch = b(i)
    if (ch == 'w' || ch == 'x' || ch == 'y') return false

    return true
  }

  private def ends(s: String): Boolean = {
    val l = s.length()
    val o = k - l + 1
    if (o < 0) return false
    for (i <- 0 until l) if (b(o + i) != s.charAt(i)) return false
    j = k - l
    return true
  }

  /* setto(s) sets (j+1),...k to the characters in the string s, readjusting
 k. */

  private def setto(s: String) {
    val l = s.length
    val o = j + 1
    for (i <- 0 until l) b(o + i) = s.charAt(i)
    k = j + l
  }

  /* r(s) is used further down. */

  private def r(s: String) {
    if (m() > 0) setto(s)
  }

  /* step1() gets rid of plurals and -ed or -ing. e.g.

         caresses  ->  caress
         ponies    ->  poni
         ties      ->  ti
         caress    ->  caress
         cats      ->  cat

         feed      ->  feed
         agreed    ->  agree
         disabled  ->  disable

         matting   ->  mat
         mating    ->  mate
         meeting   ->  meet
         milling   ->  mill
         messing   ->  mess

         meetings  ->  meet

  */

  private def step1() {
    if (b(k) == 's') {
      if (ends("sses")) k -= 2
      else if (ends("ies")) setto("i")
      else if (b(k - 1) != 's') k -= 1
    }
    if (ends("eed")) {
      if (m() > 0) k -= 1
    } else if ((ends("ed") || ends("ing")) && vowelinstem()) {
      k = j
      if (ends("at")) setto("ate")
      else if (ends("bl")) setto("ble")
      else if (ends("iz")) setto("ize")
      else if (doublec(k)) {
        k -= 1
        var ch = b(k)
        if (ch == 'l' || ch == 's' || ch == 'z') k += 1
      } else if (m() == 1 && cvc(k)) setto("e")
    }
  }

  /* step2() turns terminal y to i when there is another vowel in the stem. */

  private def step2() {
    if (ends("y") && vowelinstem()) b(k) = 'i'
  }

  /* step3() maps Double suffices to single ones. so -ization ( = -ize plus
-ation) maps to -ize etc. note that the string before the suffix must give
m() > 0. */

  private def step3() {
    if (k == 0) return; /* For Bug 1 */
    b(k - 1) match {
      case 'a' =>
        if (ends("ational")) {
          r("ate")
        } else if (ends("tional")) {
          r("tion")
        }
      case 'c' =>
        if (ends("enci")) {
          r("ence")
        } else if (ends("anci")) {
          r("ance")
        }
      case 'e' =>
        if (ends("izer")) {
          r("ize")
        }
      case 'l' =>
        if (ends("bli")) {
          r("ble")
        } else if (ends("alli")) {
          r("al")
        } else if (ends("entli")) {
          r("ent")
        } else if (ends("eli")) {
          r("e")
        } else if (ends("ousli")) {
          r("ous")
        }
      case 'o' =>
        if (ends("ization")) {
          r("ize")
        } else if (ends("ation")) {
          r("ate")
        } else if (ends("ator")) {
          r("ate")
        }
      case 's' =>
        if (ends("alism")) {
          r("al")
        } else if (ends("iveness")) {
          r("ive")
        } else if (ends("fulness")) {
          r("ful")
        } else if (ends("ousness")) {
          r("ous")
        }
      case 't' =>
        if (ends("aliti")) {
          r("al")
        } else if (ends("iviti")) {
          r("ive")
        } else if (ends("biliti")) {
          r("ble")
        }
      case 'g' =>
        if (ends("logi")) {
          r("log")
        }
      case _ =>
    }
  }

  /* step4() deals with -ic-, -full, -ness etc. similar strategy to step3. */

  private def step4() {
    b(k) match {
      case 'e' =>
        if (ends("icate")) {
          r("ic")
        }
        else if (ends("ative")) {
          r("")
        }
        else if (ends("alize")) {
          r("al")
        }
      case 'i' =>
        if (ends("iciti")) {
          r("ic")
        }
      case 'l' =>
        if (ends("ical")) {
          r("ic")
        }
        else if (ends("ful")) {
          r("")
        }
      case 's' =>
        if (ends("ness")) {
          r("")
        }
      case _ =>
    }
  }

  /* step5() takes off -ant, -ence etc., in context [c]vcvc[v]. */

  private def step5() {
    if (k == 0) return; /* for Bug 1 */
    b(k - 1) match {
      case 'a' =>
        if (!ends("al")) return
      case 'c' =>
        if (!ends("ance") && ends("ence")) return
      case 'e' =>
        if (!ends("er")) return
      case 'i' =>
        if (!ends("ic")) return
      case 'l' =>
        if (!ends("able") && !ends("ible")) return
      case 'n' =>
        if (!ends("ant") && !ends("ement") && !ends("ment") && !ends("ent")) return
      case 'o' =>
        if (!(ends("ion") && j >= 0 && (b(j) == 's' || b(j) == 't')) && !ends("ou")) return
      /* takes care of -ous */
      case 's' =>
        if (!ends("ism")) return
      case 't' =>
        if (!ends("ate") && !ends("iti")) return
      case 'u' =>
        if (!ends("ous")) return
      case 'v' =>
        if (!ends("ive")) return
      case 'z' =>
        if (!ends("ize")) return
      case _ =>
        return
    }
    if (m() > 1) k = j
  }

  /* step6() removes a -e if m() > 1. */

  private def step6() {
    j = k
    if (b(k) == 'e') {
      val a = m()
      if (a > 1 || a == 1 && !cvc(k - 1)) k -= 1
    }
    if (b(k) == 'l' && doublec(k) && m() > 1) k -= 1
  }

  /**
   * Stem the word placed into the Stemmer buffer through calls to add().
   * Returns true if the stemming process resulted in a word different
   * from the input.  You can retrieve the result with
   * getResultLength()/getResultBuffer() or toString().
   */
  def stem(word: String): String = {
    add(word.toCharArray(), word.length())
    k = i - 1
    if (k > 1) {
      step1()
      step2()
      step3()
      step4()
      step5()
      step6()
    }
    i_end = k + 1
    i = 0
    return toString()
  }
}
