package websiteschema.mpsegment.web.api.service;

import websiteschema.mpsegment.web.api.model.WordItem;
import websiteschema.mpsegment.web.api.model.dto.WordItemDto;

public class WordItemUpdateRequestMerger {
    private WordItemDto dto;

    public WordItemUpdateRequestMerger merge(WordItemDto dto) {
        this.dto = dto;
        return this;
    }

    public void to(WordItem wordItem) {
        wordItem.setName(dto.word);
        wordItem.setType(dto.type);
    }
}
