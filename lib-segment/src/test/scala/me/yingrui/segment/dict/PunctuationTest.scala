package me.yingrui.segment.dict

import org.junit.Assert
import org.junit.Assert._
import org.junit.Test

class PunctuationTest {

  DictionaryFactory().loadDictionary()
  private val hashDictionary = DictionaryFactory().getCoreDictionary

  @Test
  def should_contains_basic_punctuations() {
    val punctuations = ",./<>?;':\"{}[]!@#$%^&*()_+-=\\|。，、；：？！…-·ˉˇ¨‘`~'“”々～‖∶＂＇｀｜〃〔〕〈〉《》「」『』．〖〗【】（）［］｛｝".toCharArray()
    for (punctuation <- punctuations) {
      val word = hashDictionary.getWord(punctuation.toString)
      print(punctuation + "," + getFirstPosOfWord(word) + "; ")
      assertEquals(POSUtil.POS_W, getFirstPosOfWord(word))
    }
    println()
  }

  @Test
  def should_contains_money_symbols() {
    val punctuations = "฿€￡₤￥".toCharArray()
    for (punctuation <- punctuations) {
      print(punctuation + " ")
      val word = hashDictionary.getWord(punctuation.toString)
      Assert.assertEquals(getFirstPosOfWord(word), POSUtil.POS_Q)
    }
    println()
  }

  @Test
  def should_contains_number_symbols() {
    val punctuations = ("ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫ" +
      "㈠㈡㈢㈣㈤㈥㈦㈧㈨㈩" +
      "⑴⑵⑶⑷⑸⑹⑺⑻⑼⑽" +
      "①②③④⑤⑥⑦⑧⑨⑩⑾⑿⒀⒁⒂⒃⒄⒅⒆⒇" +
      "①②③④⑤⑥⑦⑧⑨⑩№" +
      "⑴⑵⑶⑷⑸⑹⑺⑻⑼⑽⑾⑿⒀⒁⒂⒃⒄⒅⒆⒇" +
      "⒈⒉⒊⒋⒌⒍⒎⒏⒐⒑⒒⒓⒔⒕⒖⒗⒘⒙⒚⒛" +
      "ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹ").toCharArray()
    for (punctuation <- punctuations) {
      print(punctuation + " ")
      val word = hashDictionary.getWord(punctuation.toString)
      Assert.assertEquals(getFirstPosOfWord(word), POSUtil.POS_M)
    }
    println()
  }

  @Test
  def should_contains_math_symbols() {
    val punctuations = ("≈≡≠＝≤≥＜＞≮≯∷±＋－×÷／∫∮∝∞∧∨∑∏∪∩∈∵∴⊥∥∠⌒⊙≌∽√").toCharArray()
    for (punctuation <- punctuations) {
      print(punctuation + " ")
      val word = hashDictionary.getWord(punctuation.toString)
      Assert.assertEquals(getFirstPosOfWord(word), POSUtil.POS_W)
    }
    println()
  }

  @Test
  def should_contains_units_of_measurement() {
    val punctuations = ("㎎㎏㎜㎝㎞㎡㏄㏎㏑°′″＄￡￥‰％℃¤￠").toCharArray()
    for (punctuation <- punctuations) {
      print(punctuation + " ")
      val word = hashDictionary.getWord(punctuation.toString)
      Assert.assertEquals(getFirstPosOfWord(word), POSUtil.POS_Q)
    }
    println()
  }

  @Test
  def should_contains_breaking_characters() {
    val punctuations = List(
      "\r\n", "\r", "\n", " ", "　"
    )
    for (punctuation <- punctuations) {
      print(java.net.URLEncoder.encode(punctuation) + " ")
      val word = hashDictionary.getWord(punctuation)
      Assert.assertEquals(getFirstPosOfWord(word), POSUtil.POS_W)
    }
    println()
  }

  @Test
  def should_contains_other_characters() {
    val punctuations = ("┌┍┎┏┐┑┒┓─┄┈├┝┞┟┠┡┢┣│┆┊┬┭┮┯┰┱┲┳┼┽┾┿╀╁╂╃§☆★●◎◇◆□■△▲※→←↑↓〓＃＿＆＠＼＾αβγδεζηθικλμνξοπρστυφχψωΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩабвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯㄅㄉㄓㄚㄞㄢㄦㄆㄊㄍㄐㄔㄗㄧㄛㄟㄣㄇㄋㄎㄑㄕㄘㄨㄜㄠㄤㄈㄏㄒㄖㄙㄩㄝㄡㄥāáǎàōóǒòêēéěèīíǐìūúǔùǖǘǚǜぁぃぅぇぉかきくけこんさしすせそたちつってとゐなにぬねのはひふへほゑまみむめもゃゅょゎをあいうえおがぎぐげござじずぜぞだぢづでどぱぴぷぺぽぼびぶべぼらりるれろやゆよわァィゥヴェォカヵキクケヶコサシスセソタチツッテトヰンナニヌネノハヒフヘホヱマミムメモャュョヮヲアイウエオガギグゲゴザジズゼゾダヂヅデドパピプペポバビブベボラリルレロヤユヨワ]レロヤユヨワ").toCharArray()
    for (punctuation <- punctuations) {
      print(punctuation + " ")
      val word = hashDictionary.getWord(punctuation.toString)
      Assert.assertEquals(getFirstPosOfWord(word), POSUtil.POS_W)
    }
    println()
  }

  private def getFirstPosOfWord(word: IWord) = word.getPOSArray().getWordPOSTable()(0)(0)
}
