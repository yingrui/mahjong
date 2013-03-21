package websiteschema.mpsegment.util

import java.text.DecimalFormat

object NumberUtil {

  val minus = "负";
  val digits = List("零", "一", "二", "三", "四", "五", "六", "七", "八", "九")
  val digits2 = List("０", "１", "２", "３", "４", "５", "６", "７", "８", "９")
  val digits3 = List("〇", "壹", "貳", "參", "肆", "伍", "陸", "柒", "捌", "玖")
  val beforeWan = List("十", "百", "千")

  val afterWan = List("", "万", "亿", "兆")
  val ALTTWO = "两";
  val TEN = 10L;
  private val hm = scala.collection.mutable.Map[String, Int]();

  for (i <- 0 until digits.length) {
    hm += (digits(i) -> i)
    hm += (digits2(i) -> i)
    hm += (digits3(i) -> i)
  }

  hm += ("贰" -> 2);
  hm += ("两" -> 2);
  hm += ("兩" -> 2);
  hm += ("叁" -> 3);
  hm += ("叄" -> 3);
  hm += ("陆" -> 6);

  def toChineseNumber(number: Long): String = {
    var l = number;
    val ai = new Array[Int](30);
    var i = 0;
    var flag = false;
    var flag1 = false;
    var flag2 = false;
    var flag3 = false;
    val stringBuilder = new StringBuilder();
    if (l == 0L) {
      return digits(0);
    }
    if (l < 0L) {
      flag1 = true;
      l = -l;
    }
    while (Math.pow(10D, i) <= l.toDouble) {
      val j = ((l.toDouble % Math.pow(10D, i + 1)) / Math.pow(10D, i)).toInt
      ai(i) = j
      l = (l.toDouble - l.toDouble % Math.pow(10D, i + 1)).toLong
      i += 1
    }

    for (k <- 0 until i) {
      if (k % 4 == 0) {
        if (ai(k) != 0) {
          flag2 = false;
          flag3 = true;
          stringBuilder.insert(0, (new StringBuilder(String.valueOf(digits(ai(k))))).append(afterWan(k / 4)).toString());
        } else if (k + 3 < i && ai(k + 3) != 0 || k + 2 < i && ai(k + 2) != 0 || k + 1 < i && ai(k + 1) != 0) {
          stringBuilder.insert(0, afterWan(k / 4));
        }
      } else if (ai(k) != 0) {
        flag2 = false;
        flag3 = true;
        if (i == 2 && k == 1 && ai(k) == 1) {
          stringBuilder.insert(0, beforeWan(k % 4 - 1));
        } else {
          stringBuilder.insert(0, (new StringBuilder(String.valueOf(digits(ai(k))))).append(beforeWan(k % 4 - 1)).toString());
        }
      } else if (flag3 && !flag2) {
        flag2 = true;
        stringBuilder.insert(0, digits(ai(k)));
      }
    }

    if (flag1) {
      stringBuilder.insert(0, "负");
    }
    return stringBuilder.toString();
  }

  def toChineseNumber(s: String): String = {
    var s1: String = null;
    var s3 = "";
    var s6 = "";
    val s2 = s.replaceAll(",", "");
    var i = s2.indexOf(".");
    if (i < 0) {
      i = s2.indexOf("．");
    }
    if (i >= 0) {
      var s4 = s2.substring(0, i);
      val l = strToLong(s4, 0L);
      s4 = toChineseNumber(l);
      if (i < s2.length()) {
        val s5 = s2.substring(i + 1);
        if (s5 != null && s5.length() > 0) {
          for (j <- 0 until s5.length()) {
            s6 = (new StringBuilder(String.valueOf(s6))).append(getChDigital(s5.charAt(j))).toString();
          }

        }
      }
      if (s6.length() > 0) {
        s1 = (new StringBuilder(String.valueOf(s4))).append("点").append(s6).toString();
      } else {
        s1 = s4;
      }
    } else {
      val l1 = strToLong(s2, 0L);
      s1 = toChineseNumber(l1);
    }
    return s1;
  }

  def strToLong(s: String, l: Long): Long = {
    var l1 = l;
    try {
      l1 = s.toLong
    } catch {
      case _: Exception =>
    }
    return l1;
  }

  def getPower(index: Int): Double = {
    var i = index
    var d = 1.0D;
    if (i > 0) {
      for (j <- 0 until i) {
        d *= 10D;
      }

    } else {
      i = -i;
      for (k <- 0 until i) {
        d /= 10D;
      }

    }
    return d;
  }

  def getChDigital(c: Char): String = {
    val i = c - 48;
    if (i >= 0 && i <= 9) {
      digits(i)
    } else {
      ""
    }
  }

  def isDefined(s: String): Boolean = hm.contains(s)


  def isDigital(s: String): Int = {
    hm.get(s) match {
      case Some(i) => i
      case _ => 0
    }
  }

  def isChineseDigitalStr(chineseNumberStr: String): Boolean = {
    if (chineseNumberStr.length() <= 0) {
      false
    } else {
      chineseNumberStr.find(ch => isDefined(ch.toString)) match {
        case Some(ch) => true
        case None => false
      }
    }
  }

  def chineseToEnglishNumberStr(str: String): String = {
    var chineseNumberStr = str;
    var s1 = "";
    if (chineseNumberStr != null && chineseNumberStr.indexOf("分之") > 0) {
      s1 = chineseNumberStr.substring(0, chineseNumberStr.indexOf("分之") + 2);
      chineseNumberStr = chineseNumberStr.substring(s1.length());
    }
    if (chineseNumberStr != null && chineseNumberStr.indexOf("第") == 0) {
      s1 = chineseNumberStr.substring(0, 1);
      chineseNumberStr = chineseNumberStr.substring(1);
    }
    if (chineseNumberStr == null || chineseNumberStr.length() < 1) {
      return "";
    }
    if (!isChineseDigitalStr(chineseNumberStr)) {
      return "";
    }
    if (chineseNumberStr.indexOf("-") > 0 || chineseNumberStr.indexOf("－") > 0) {
      return "";
    }
    var s2 = chinesePositiveRealNumberToEnglishNumber(chineseNumberStr);
    if (s1.length() > 0) {
      s2 = (new StringBuilder(String.valueOf(s1))).append(s2).toString();
    }
    val s3 = chineseNumberStr.substring(chineseNumberStr.length() - 1);
    if (!isDefined(s3) && "〇零十拾百佰千仟万亿兆.．".indexOf(s3) < 0) {
      s2 = (new StringBuilder(String.valueOf(s2))).append(s3).toString();
    }
    return s2;
  }

  def chinesePositiveRealNumberToEnglishNumber(positiveRealNumberStr: String): String = {
    val number = chineseToEnglishNumber(positiveRealNumberStr);
    if (number == 0.0D && (!positiveRealNumberStr.equals("零") || !positiveRealNumberStr.equals("〇") || !positiveRealNumberStr.equals("０"))) {
      return "";
    }
    if (containsDot(positiveRealNumberStr)) {
      new DecimalFormat("###.#########").format(number)
    } else {
      new DecimalFormat("###").format(number)
    }
  }

  private def containsDot(str: String): Boolean = {
    return str.contains("点") || str.contains(".") || str.contains("．") || str.contains("·");
  }

  def chineseToEnglishNumber(str: String): Double = {
    var s = str
    var flag = false;
    var j = 0;

    var flag1 = false;
    var k = 0;
    var d = 0.0D;
    var d1 = 0.0D;
    var l = 0L;
    s = s.replaceAll("亿万", "兆");
    s = s.replaceAll("万亿", "兆");
    s = s.replaceAll("万万", "亿");
    s = s.replaceAll("廿", "二十");
    s = s.replaceAll("卄", "二十");
    s = s.replaceAll("卅", "三十");
    s = s.replaceAll("卌", "四十");
    var i = s.length()
    while (j < s.length()) {
      var c = s.charAt(j);
      if (j == 0 && (c == '-' || c == '负')) {
        flag1 = true;
      } else if (j != 0 || c != '第') {
        if (c == '点' || c == '.' || c == '．') {
          l = 1L;
          k = -1;
        } else {
          if (c == '兆') {
            k = 12;
            if (d == 0.0D) {
              d = 1.0D;
            }
            d1 += d * getPower(k);
            d = 0.0D;
            k -= 4;
          }
          if (c == '亿') {
            k = 8;
            if (d == 0.0D) {
              d = 1.0D;
            }
            d1 += d * getPower(k);
            d = 0.0D;
            k -= 4;
          }
          if (c == '万') {
            k = 4;
            if (d == 0.0D) {
              d = 1.0D;
            }
            d1 += d * getPower(k);
            d = 0.0D;
            k -= 4;
          }
          if (c == '千' || c == '仟') {
            d += 1000D;
          }
          if (c == '百' || c == '佰') {
            d += 100D;
          }
          if (c == '十' || c == '拾') {
            d += 10D;
          }
          if (c == '零' || c == '〇' || c == '0' || c == '０') {
            k = 0;
          } else {
            var s1 = s.substring(j, j + 1);
            if (isDefined(s1)) {
              var i1 = isDigital(s1);
              if (l > 0L) {
                d += i1 * getPower(k).toDouble;
                k -= 1;
                while (j + 1 < i && isDefined(s.substring(j + 1, j + 2))) {
                  i1 = isDigital(s.substring(j + 1, j + 2))
                  d += i1 * getPower(k).toDouble;
                  k -= 1;
                  j += 1
                }

              } else if (j + 1 < i) {
                var c1 = s.charAt(j + 1);
                if (c1 == '十' || c1 == '拾') {
                  d += i1 * 10;
                  j += 1;
                } else if (c1 == '百' || c1 == '佰') {
                  d += i1 * 100;
                  j += 1;
                } else if (c1 == '千' || c1 == '仟') {
                  d += i1 * 1000;
                  j += 1;
                } else if (isDefined(s.substring(j + 1, j + 2))) {
                  var j1 = 0;
                  j1 += i1;
                  while (j + 1 < i && isDefined(s.substring(j + 1, j + 2))) {
                    j1 *= 10;
                    j1 += isDigital(s.substring(j + 1, j + 2));
                    j += 1
                  }

                  d += j1;
                } else {
                  d += i1;
                }
              } else if (j + 1 == i && j > 0) {
                var c2 = s.charAt(j - 1);
                if (c2 == '兆') {
                  d += i1 * getPower(11).toDouble;
                } else if (c2 == '亿') {
                  d += i1 * getPower(7).toDouble;
                } else if (c2 == '万') {
                  d += i1 * 1000;
                } else if (c2 == '千' || c2 == '仟') {
                  d += i1 * 100;
                } else if (c2 == '百' || c2 == '佰') {
                  d += i1 * 10;
                } else {
                  d += i1;
                }
              } else {
                d += i1;
              }
            }
          }
        }
      }
      j += 1
    }

    d1 += d;
    if (flag1) {
      d1 = -d1;
    }
    return d1;
  }

}
