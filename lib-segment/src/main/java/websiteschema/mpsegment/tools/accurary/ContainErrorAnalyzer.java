package websiteschema.mpsegment.tools.accurary;

import websiteschema.mpsegment.core.WordAtom;

import java.util.Map;

class ContainErrorAnalyzer extends AbstractErrorAnalyzer {

    @Override
    public boolean analysis(WordAtom expect, String possibleErrorWord) {
        boolean foundError = false;
        if (possibleErrorWord.length() == 0) {
            // Bigger word contains the littler words.
            // Should remove the word from dictionary.
            increaseOccur();
//            System.out.println(expect.word + " in " + errorSegment);
            foundError = true;
        } else if (!possibleErrorWord.replaceAll(" ", "").equals(expect.word)
                && possibleErrorWord.contains(expect.word)) {
            increaseOccur();
            addErrorWord(possibleErrorWord);
//            System.out.println(expect.word + " in " + errorSegment);
            foundError = true;
        }
        return foundError;
    }

    @Override
    public void postAnalysis(Map<String, Integer> allWordsAndFreqInCorpus) {
        for(String word : getWords()) {
            if(allWordsAndFreqInCorpus.containsKey(word)) {
                removeErrorWord(word);
            }
        }
    }
}
