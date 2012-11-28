package websiteschema.mpsegment.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import websiteschema.mpsegment.dict.DictionaryLookupResult;
import websiteschema.mpsegment.dict.DictionaryService;
import websiteschema.mpsegment.dict.IWord;
import websiteschema.mpsegment.util.StringUtil;

public abstract class AbstractWordScanner {
    private final static Log logger = LogFactory.getLog(AbstractWordScanner.class);
    private String sentence;
    private int maxWordLength;
    private DictionaryService dictionaryService;

    protected DictionaryService getDictionaryService() {
        return dictionaryService;
    }

    public void startScanningAt(int startPos) {
        int lastMinWordLen = 0;
        final int length = sentence.length();
        try {
            for (int begin = startPos; begin < length; begin += lastMinWordLen) {
                final int minWordLen = scanTheMinWordLength(begin);
                lastMinWordLen = minWordLen;

                //find single char word or multi-chars alpha-numeric word
                String atomWord = sentence.substring(begin, begin + minWordLen);
                IWord singleCharWord = foundAtomWord(atomWord);

                //find all possible slices except single word
                final String candidateWord = getCandidateSentence(begin, lastMinWordLen);
                DictionaryLookupResult result = dictionaryService.lookup(candidateWord);
                processFoundWordItems(begin, singleCharWord, result);
            }
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
        }
    }

    abstract public IWord foundAtomWord(String atomWord);
    abstract public void processFoundWordItems(int begin, IWord singleCharWord, DictionaryLookupResult lookupResult);

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public void setMaxWordLength(int maxWordLength) {
        this.maxWordLength = maxWordLength;
    }

    public void setDictionaryService(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    private int scanTheMinWordLength(int begin) {
        int index = scanEnglishWordAndShorten(begin);
        int minWordLen = (index - begin) + 1;
        return minWordLen;
    }

    private int scanEnglishWordAndShorten(int begin) {
        final int sentenceLength = sentence.length();
        int index = begin;
        if (StringUtil.isCharAlphabeticalOrDigital(sentence.charAt(index))) {
            while (index < sentenceLength && StringUtil.isCharAlphabeticalOrDigital(sentence.charAt(index))) {
                index++;
            }
            index--;
        }
        return index;
    }

    private String getCandidateSentence(int begin, int lastMinWordLen1) {
        String candidateWord = "";
        final int length = sentence.length();
        final int rest = length - begin;
        if (maxWordLength <= rest) {
            int end = (begin + maxWordLength + lastMinWordLen1) - 1;
            end = end < length
                    ? end : length;
            candidateWord = sentence.substring(begin, end);
        } else {
            candidateWord = sentence.substring(begin);
        }
        return candidateWord;
    }
}
