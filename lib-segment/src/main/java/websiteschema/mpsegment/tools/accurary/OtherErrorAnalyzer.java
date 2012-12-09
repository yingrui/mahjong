package websiteschema.mpsegment.tools.accurary;

import websiteschema.mpsegment.core.WordAtom;

class OtherErrorAnalyzer extends AbstractErrorAnalyzer {

    @Override
    public boolean analysis(WordAtom expect, String possibleErrorWord) {
        increaseOccur();
        addErrorWord(expect.word);
        return true;
    }
}
