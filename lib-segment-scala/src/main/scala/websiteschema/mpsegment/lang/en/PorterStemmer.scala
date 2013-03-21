//package websiteschema.mpsegment.lang.en;
//
///**
// * http://tartarus.org/~martin/PorterStemmer/java.txt
// */
//class PorterStemmer {
//    private var b : Array[Char] = null
//    private Int i,     /* offset into b */
//            i_end, /* offset to end of stemmed word */
//            j, k;
//    private static Int INC = 50;
//
//    /* unit of size whereby b is increased */
//    public PorterStemmer() {
//        b = new Array[Char](INC);
//        i = 0;
//        i_end = 0;
//    }
//
//    /**
//     * Add a character to the word being stemmed.  When you are finished
//     * adding characters, you can call stem(void) to stem the word.
//     */
//
//    def add(ch: Char) {
//        if (i == b.length) {
//            var new_b = new Char[i + INC]
//            for (Int c = 0; c < i; c++) new_b(c) = b(c);
//            b = new_b;
//        }
//        b[i++] = ch;
//    }
//
//
//    /**
//     * Adds wLen characters to the word being stemmed contained in a portion
//     * of a Array[Char] array. This is like repeated calls of add(Char ch), but
//     * faster.
//     */
//
//    def add(w: Array[Char], wLen: Int) {
//        if (i + wLen >= b.length) {
//            var new_b = new Char[i + wLen + INC]
//            for (Int c = 0; c < i; c++) new_b(c) = b(c);
//            b = new_b;
//        }
//        for (Int c = 0; c < wLen; c++) b[i++] = w(c);
//    }
//
//    /**
//     * After a word has been stemmed, it can be retrieved by toString(),
//     * or a reference to the internal buffer can be retrieved by getResultBuffer
//     * and getResultLength (which is generally more efficient.)
//     */
//    def toString() : String = {
//        return new String(b, 0, i_end);
//    }
//
//    /**
//     * Returns the length of the word resulting from the stemming process.
//     */
//    def getResultLength() : Int = {
//        return i_end;
//    }
//
//    /**
//     * Returns a reference to a character buffer containing the results of
//     * the stemming process.  You also need to consult getResultLength()
//     * to determine the length of the result.
//     */
//    def getResultBuffer() : Array[Char] = {
//        return b;
//    }
//
//    /* cons(i) is true <=> b(i) is a consonant. */
//
//    private def cons(i: Int) : Boolean = {
//        switch (b(i)) {
//            case 'a':
//            case 'e':
//            case 'i':
//            case 'o':
//            case 'u':
//                return false;
//            case 'y':
//                return (i == 0) ? true : !cons(i - 1);
//            default:
//                return true;
//        }
//    }
//
//    /* m() measures the number of consonant sequences between 0 and j. if c is
//       a consonant sequence and v a vowel sequence, and <..> indicates arbitrary
//       presence,
//
//          [c][v]       gives 0
//          [c]vc[v]     gives 1
//          [c]vcvc[v]   gives 2
//          [c]vcvcvc[v] gives 3
//          ....
//    */
//
//    private def m() : Int = {
//        var n = 0
//        var i = 0
//        while (true) {
//            if (i > j) return n;
//            if (!cons(i)) break;
//            i++;
//        }
//        i++;
//        while (true) {
//            while (true) {
//                if (i > j) return n;
//                if (cons(i)) break;
//                i++;
//            }
//            i++;
//            n++;
//            while (true) {
//                if (i > j) return n;
//                if (!cons(i)) break;
//                i++;
//            }
//            i++;
//        }
//    }
//
//    /* vowelinstem() is true <=> 0,...j contains a vowel */
//
//    private def vowelinstem() : Boolean = {
//        var i: Int = null
//        for (i = 0; i <= j; i++) if (!cons(i)) return true;
//        return false;
//    }
//
//    /* doublec(j) is true <=> j,(j-1) contain a Double consonant. */
//
//    private def doublec(j: Int) : Boolean = {
//        if (j < 1) return false;
//        if (b(j) != b[j - 1]) return false;
//        return cons(j);
//    }
//
//    /* cvc(i) is true <=> i-2,i-1,i has the form consonant - vowel - consonant
//       and also if the second c is not w,x or y. this is used when trying to
//       restore an e at the end of a short word. e.g.
//
//          cav(e), lov(e), hop(e), crim(e), but
//          snow, box, tray.
//
//    */
//
//    private def cvc(i: Int) : Boolean = {
//        if (i < 2 || !cons(i) || cons(i - 1) || !cons(i - 2)) return false;
//        {
//            var ch = b(i)
//            if (ch == 'w' || ch == 'x' || ch == 'y') return false;
//        }
//        return true;
//    }
//
//    private def ends(s: String) : Boolean = {
//        var l = s.length()
//        var o = k - l + 1
//        if (o < 0) return false;
//        for (Int i = 0; i < l; i++) if (b[o + i] != s.charAt(i)) return false;
//        j = k - l;
//        return true;
//    }
//
//    /* setto(s) sets (j+1),...k to the characters in the string s, readjusting
//   k. */
//
//    private def setto(s: String) {
//        var l = s.length()
//        var o = j + 1
//        for (Int i = 0; i < l; i++) b[o + i] = s.charAt(i);
//        k = j + l;
//    }
//
//    /* r(s) is used further down. */
//
//    private def r(s: String) {
//        if (m() > 0) setto(s);
//    }
//
//    /* step1() gets rid of plurals and -ed or -ing. e.g.
//
//           caresses  ->  caress
//           ponies    ->  poni
//           ties      ->  ti
//           caress    ->  caress
//           cats      ->  cat
//
//           feed      ->  feed
//           agreed    ->  agree
//           disabled  ->  disable
//
//           matting   ->  mat
//           mating    ->  mate
//           meeting   ->  meet
//           milling   ->  mill
//           messing   ->  mess
//
//           meetings  ->  meet
//
//    */
//
//    private def step1() {
//        if (b(k) == 's') {
//            if (ends("sses")) k -= 2;
//            else if (ends("ies")) setto("i");
//            else if (b[k - 1] != 's') k--;
//        }
//        if (ends("eed")) {
//            if (m() > 0) k--;
//        } else if ((ends("ed") || ends("ing")) && vowelinstem()) {
//            k = j;
//            if (ends("at")) setto("ate");
//            else if (ends("bl")) setto("ble");
//            else if (ends("iz")) setto("ize");
//            else if (doublec(k)) {
//                k--;
//                {
//                    var ch = b(k)
//                    if (ch == 'l' || ch == 's' || ch == 'z') k++;
//                }
//            } else if (m() == 1 && cvc(k)) setto("e");
//        }
//    }
//
//    /* step2() turns terminal y to i when there is another vowel in the stem. */
//
//    private def step2() {
//        if (ends("y") && vowelinstem()) b(k) = 'i';
//    }
//
//    /* step3() maps Double suffices to single ones. so -ization ( = -ize plus
// -ation) maps to -ize etc. note that the string before the suffix must give
// m() > 0. */
//
//    private def step3() {
//        if (k == 0) return; /* For Bug 1 */
//        switch (b[k - 1]) {
//            case 'a':
//                if (ends("ational")) {
//                    r("ate");
//                    break;
//                }
//                if (ends("tional")) {
//                    r("tion");
//                    break;
//                }
//                break;
//            case 'c':
//                if (ends("enci")) {
//                    r("ence");
//                    break;
//                }
//                if (ends("anci")) {
//                    r("ance");
//                    break;
//                }
//                break;
//            case 'e':
//                if (ends("izer")) {
//                    r("ize");
//                    break;
//                }
//                break;
//            case 'l':
//                if (ends("bli")) {
//                    r("ble");
//                    break;
//                }
//                if (ends("alli")) {
//                    r("al");
//                    break;
//                }
//                if (ends("entli")) {
//                    r("ent");
//                    break;
//                }
//                if (ends("eli")) {
//                    r("e");
//                    break;
//                }
//                if (ends("ousli")) {
//                    r("ous");
//                    break;
//                }
//                break;
//            case 'o':
//                if (ends("ization")) {
//                    r("ize");
//                    break;
//                }
//                if (ends("ation")) {
//                    r("ate");
//                    break;
//                }
//                if (ends("ator")) {
//                    r("ate");
//                    break;
//                }
//                break;
//            case 's':
//                if (ends("alism")) {
//                    r("al");
//                    break;
//                }
//                if (ends("iveness")) {
//                    r("ive");
//                    break;
//                }
//                if (ends("fulness")) {
//                    r("ful");
//                    break;
//                }
//                if (ends("ousness")) {
//                    r("ous");
//                    break;
//                }
//                break;
//            case 't':
//                if (ends("aliti")) {
//                    r("al");
//                    break;
//                }
//                if (ends("iviti")) {
//                    r("ive");
//                    break;
//                }
//                if (ends("biliti")) {
//                    r("ble");
//                    break;
//                }
//                break;
//            case 'g':
//                if (ends("logi")) {
//                    r("log");
//                    break;
//                }
//        }
//    }
//
//    /* step4() deals with -ic-, -full, -ness etc. similar strategy to step3. */
//
//    private def step4() {
//        switch (b(k)) {
//            case 'e':
//                if (ends("icate")) {
//                    r("ic");
//                    break;
//                }
//                if (ends("ative")) {
//                    r("");
//                    break;
//                }
//                if (ends("alize")) {
//                    r("al");
//                    break;
//                }
//                break;
//            case 'i':
//                if (ends("iciti")) {
//                    r("ic");
//                    break;
//                }
//                break;
//            case 'l':
//                if (ends("ical")) {
//                    r("ic");
//                    break;
//                }
//                if (ends("ful")) {
//                    r("");
//                    break;
//                }
//                break;
//            case 's':
//                if (ends("ness")) {
//                    r("");
//                    break;
//                }
//                break;
//        }
//    }
//
//    /* step5() takes off -ant, -ence etc., in context [c]vcvc[v]. */
//
//    private def step5() {
//        if (k == 0) return; /* for Bug 1 */
//        switch (b[k - 1]) {
//            case 'a':
//                if (ends("al")) break;
//                return;
//            case 'c':
//                if (ends("ance")) break;
//                if (ends("ence")) break;
//                return;
//            case 'e':
//                if (ends("er")) break;
//                return;
//            case 'i':
//                if (ends("ic")) break;
//                return;
//            case 'l':
//                if (ends("able")) break;
//                if (ends("ible")) break;
//                return;
//            case 'n':
//                if (ends("ant")) break;
//                if (ends("ement")) break;
//                if (ends("ment")) break;
//                /* element etc. not stripped before the m */
//                if (ends("ent")) break;
//                return;
//            case 'o':
//                if (ends("ion") && j >= 0 && (b(j) == 's' || b(j) == 't')) break;
//                /* j >= 0 fixes Bug 2 */
//                if (ends("ou")) break;
//                return;
//            /* takes care of -ous */
//            case 's':
//                if (ends("ism")) break;
//                return;
//            case 't':
//                if (ends("ate")) break;
//                if (ends("iti")) break;
//                return;
//            case 'u':
//                if (ends("ous")) break;
//                return;
//            case 'v':
//                if (ends("ive")) break;
//                return;
//            case 'z':
//                if (ends("ize")) break;
//                return;
//            default:
//                return;
//        }
//        if (m() > 1) k = j;
//    }
//
//    /* step6() removes a -e if m() > 1. */
//
//    private def step6() {
//        j = k;
//        if (b(k) == 'e') {
//            var a = m()
//            if (a > 1 || a == 1 && !cvc(k - 1)) k--;
//        }
//        if (b(k) == 'l' && doublec(k) && m() > 1) k--;
//    }
//
//    /**
//     * Stem the word placed into the Stemmer buffer through calls to add().
//     * Returns true if the stemming process resulted in a word different
//     * from the input.  You can retrieve the result with
//     * getResultLength()/getResultBuffer() or toString().
//     */
//    def stem(word: String) : String = {
//        add(word.toCharArray(), word.length());
//        k = i - 1;
//        if (k > 1) {
//            step1();
//            step2();
//            step3();
//            step4();
//            step5();
//            step6();
//        }
//        i_end = k + 1;
//        i = 0;
//        return toString();
//    }
//}
