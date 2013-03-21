/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.dict;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author ray
 */
public class PunctuationTest {

    private HashDictionary hashDictionary = null;

    public PunctuationTest() {
        DictionaryFactory.getInstance().loadDictionary();
        hashDictionary = DictionaryFactory.getInstance().getCoreDictionary();
    }

    @Test
    public void should_contains_basic_punctuations() {
        char punctuations[] = ",./<>?;':\"{}[]!@#$%^&*()_+-=\\|。，、；：？！…-·ˉˇ¨‘`~'“”々～‖∶＂＇｀｜〃〔〕〈〉《》「」『』．〖〗【】（）［］｛｝".toCharArray();
        for (char punctuation : punctuations) {
            System.out.print(punctuation + " ");
            IWord word = hashDictionary.getWord(String.valueOf(punctuation));
            Assert.assertEquals(getFirstPosOfWord(word), POSUtil.POS_W);
        }
        System.out.println();
    }

    @Test
    public void should_contains_money_symbols() {
        char punctuations[] = "฿€￡₤￥".toCharArray();
        for (char punctuation : punctuations) {
            System.out.print(punctuation + " ");
            IWord word = hashDictionary.getWord(String.valueOf(punctuation));
            Assert.assertEquals(getFirstPosOfWord(word), POSUtil.POS_Q);
        }
        System.out.println();
    }

    @Test
    public void should_contains_number_symbols() {
        char punctuations[] = ("ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫ" +
                "㈠㈡㈢㈣㈤㈥㈦㈧㈨㈩" +
                "⑴⑵⑶⑷⑸⑹⑺⑻⑼⑽" +
                "①②③④⑤⑥⑦⑧⑨⑩⑾⑿⒀⒁⒂⒃⒄⒅⒆⒇" +
                "①②③④⑤⑥⑦⑧⑨⑩№" +
                "⑴⑵⑶⑷⑸⑹⑺⑻⑼⑽⑾⑿⒀⒁⒂⒃⒄⒅⒆⒇" +
                "⒈⒉⒊⒋⒌⒍⒎⒏⒐⒑⒒⒓⒔⒕⒖⒗⒘⒙⒚⒛" +
                "ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹ").toCharArray();
        for (char punctuation : punctuations) {
            System.out.print(punctuation + " ");
            IWord word = hashDictionary.getWord(String.valueOf(punctuation));
            Assert.assertEquals(getFirstPosOfWord(word), POSUtil.POS_M);
        }
        System.out.println();
    }

    @Test
    public void should_contains_math_symbols() {
        char punctuations[] = ("≈≡≠＝≤≥＜＞≮≯∷±＋－×÷／∫∮∝∞∧∨∑∏∪∩∈∵∴⊥∥∠⌒⊙≌∽√").toCharArray();
        for (char punctuation : punctuations) {
            System.out.print(punctuation + " ");
            IWord word = hashDictionary.getWord(String.valueOf(punctuation));
            Assert.assertEquals(getFirstPosOfWord(word), POSUtil.POS_W);
        }
        System.out.println();
    }

    @Test
    public void should_contains_units_of_measurement() {
        char punctuations[] = ("㎎㎏㎜㎝㎞㎡㏄㏎㏑°′″＄￡￥‰％℃¤￠").toCharArray();
        for (char punctuation : punctuations) {
            System.out.print(punctuation + " ");
            IWord word = hashDictionary.getWord(String.valueOf(punctuation));
            Assert.assertEquals(getFirstPosOfWord(word), POSUtil.POS_Q);
        }
        System.out.println();
    }

    @Test
    public void should_contains_breaking_characters() {
        String punctuations[] = new String[]{"\r\n", "\r", "\n", " ", "　"};
        for (String punctuation : punctuations) {
            System.out.print(java.net.URLEncoder.encode(punctuation) + " ");
            IWord word = hashDictionary.getWord(punctuation);
            Assert.assertEquals(getFirstPosOfWord(word), POSUtil.POS_W);
        }
        System.out.println();
    }

    @Test
    public void should_contains_other_characters() {
        char punctuations[] = ("┌┍┎┏┐┑┒┓─┄┈├┝┞┟┠┡┢┣│┆┊┬┭┮┯┰┱┲┳┼┽┾┿╀╁╂╃§☆★●◎◇◆□■△▲※→←↑↓〓＃＿＆＠＼＾αβγδεζηθικλμνξοπρστυφχψωΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩабвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯㄅㄉㄓㄚㄞㄢㄦㄆㄊㄍㄐㄔㄗㄧㄛㄟㄣㄇㄋㄎㄑㄕㄘㄨㄜㄠㄤㄈㄏㄒㄖㄙㄩㄝㄡㄥāáǎàōóǒòêēéěèīíǐìūúǔùǖǘǚǜぁぃぅぇぉかきくけこんさしすせそたちつってとゐなにぬねのはひふへほゑまみむめもゃゅょゎをあいうえおがぎぐげござじずぜぞだぢづでどぱぴぷぺぽぼびぶべぼらりるれろやゆよわァィゥヴェォカヵキクケヶコサシスセソタチツッテトヰンナニヌネノハヒフヘホヱマミムメモャュョヮヲアイウエオガギグゲゴザジズゼゾダヂヅデドパピプペポバビブベボラリルレロヤユヨワ]レロヤユヨワ").toCharArray();
        for (char punctuation : punctuations) {
            System.out.print(punctuation + " ");
            IWord word = hashDictionary.getWord(String.valueOf(punctuation));
            Assert.assertEquals(getFirstPosOfWord(word), POSUtil.POS_W);
        }
        System.out.println();
    }

    private int getFirstPosOfWord(IWord word) {
        return word.getPOSArray().getWordPOSTable()[0][0];
    }
}
