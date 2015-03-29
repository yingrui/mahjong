package me.yingrui.segment.web.api.model.dto;

import me.yingrui.segment.core.Word;
import me.yingrui.segment.dict.POSUtil;

public class WordDto {
    public String word;
    public String pinyin;
    public String pos;
    public String concept;
    public int domainType;

    public WordDto(Word word) {
        this.word = word.name();
        pinyin = word.pinyin();
        pos = POSUtil.getPOSString(word.pos());
        concept = word.concept();
        domainType = word.domainType();
    }

    @Override
    public String toString() {
        return word + "/" + pos;
    }
}
