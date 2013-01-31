package websiteschema.mpsegment.controller;

import websiteschema.mpsegment.core.WordAtom;
import websiteschema.mpsegment.dict.POSUtil;

public class WordDto {
    public String word;
    public String pinyin;
    public String pos;
    public String concept;
    public int domainType;

    public WordDto(WordAtom wordAtom) {
        word = wordAtom.word;
        pinyin = wordAtom.pinyin;
        pos = POSUtil.getPOSString(wordAtom.pos);
        concept = wordAtom.concept;
        domainType = wordAtom.domainType;
    }
}
