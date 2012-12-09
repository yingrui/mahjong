package websiteschema.mpsegment.tools.accurary;

import websiteschema.mpsegment.core.WordAtom;
import websiteschema.mpsegment.dict.DictionaryFactory;
import websiteschema.mpsegment.dict.IWord;
import websiteschema.mpsegment.dict.POSUtil;

import java.util.Map;

class NewWordErrorAnalyzer extends AbstractErrorAnalyzer {

    @Override
    public boolean analysis(WordAtom expect, String possibleErrorWord) {
        boolean foundError = false;
        if (possibleErrorWord.replaceAll(" ", "").equals(expect.word)) {
            if (expect.pos != POSUtil.POS_NR && expect.pos != POSUtil.POS_NS) {
                increaseOccur();
                addErrorWord(expect.word);
                foundError = true;
            }
        }
        return foundError;
    }

    @Override
    public void postAnalysis(Map<String, Integer> allWordsAndFreqInCorpus) {
        for(String wordStr : getWords()) {
            IWord word = DictionaryFactory.getInstance().getCoreDictionary().getWord(wordStr);
            if(null != word) {
                removeErrorWord(wordStr);
            }
        }
    }
}
