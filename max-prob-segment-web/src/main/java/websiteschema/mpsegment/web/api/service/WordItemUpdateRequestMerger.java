package websiteschema.mpsegment.web.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websiteschema.mpsegment.web.api.model.PartOfSpeech;
import websiteschema.mpsegment.web.api.model.Pinyin;
import websiteschema.mpsegment.web.api.model.WordFreq;
import websiteschema.mpsegment.web.api.model.WordItem;
import websiteschema.mpsegment.web.api.model.dto.WordFreqDto;
import websiteschema.mpsegment.web.api.model.dto.WordItemDto;

@Service
public class WordItemUpdateRequestMerger {
    private WordItemDto dto;

    @Autowired
    private PartOfSpeechRepository partOfSpeechRepository;

    public WordItemUpdateRequestMerger merge(WordItemDto dto) {
        this.dto = dto;
        return this;
    }

    public void to(WordItem wordItem) {
        wordItem.setName(dto.word);
        wordItem.setType(dto.type);
        mergePinyin(wordItem);
        mergeWordFreq(wordItem);
    }

    private void mergeWordFreq(WordItem wordItem) {
        wordItem.getWordFreqSet().clear();
        for(WordFreqDto wordFreqDto: dto.partOfSpeeches) {
            WordFreq wordFreq = new WordFreq();
            wordFreq.setFreq(wordFreqDto.freq);
            PartOfSpeech partOfSpeech = partOfSpeechRepository.get(wordFreqDto.partOfSpeech.id);
            System.out.println("id:" + partOfSpeech.getId());
            wordFreq.setPartOfSpeech(partOfSpeech);
            wordItem.getWordFreqSet().add(wordFreq);
        }
    }

    private void mergePinyin(WordItem wordItem) {
        wordItem.getPinyinSet().clear();
        for(String pinyin: dto.pinyinSet) {
            Pinyin p = new Pinyin();
            p.setName(pinyin);
            wordItem.getPinyinSet().add(p);
        }
    }
}
