package websiteschema.mpsegment.web.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websiteschema.mpsegment.web.api.model.*;
import websiteschema.mpsegment.web.api.model.dto.ConceptDto;
import websiteschema.mpsegment.web.api.model.dto.WordFreqDto;
import websiteschema.mpsegment.web.api.model.dto.WordItemDto;

@Service
public class WordItemUpdateRequestMerger {
    private WordItemDto dto;

    @Autowired
    private PartOfSpeechRepository partOfSpeechRepository;
    @Autowired
    private ConceptService conceptService;

    public WordItemUpdateRequestMerger merge(WordItemDto dto) {
        this.dto = dto;
        return this;
    }

    public void to(WordItem wordItem) {
        wordItem.setName(dto.word);
        wordItem.setType(dto.type);
        mergePinyin(wordItem);
        mergeWordFreq(wordItem);
        mergeConcept(wordItem);
    }

    private void mergeConcept(WordItem wordItem) {
        wordItem.getConceptSet().clear();
        for(ConceptDto conceptDto: dto.conceptSet) {
            Concept concept = conceptService.getByName(conceptDto.name);
            wordItem.getConceptSet().add(concept);
        }
    }

    private void mergeWordFreq(WordItem wordItem) {
        wordItem.getWordFreqSet().clear();
        for(WordFreqDto wordFreqDto: dto.partOfSpeeches) {
            WordFreq wordFreq = new WordFreq();
            wordFreq.setFreq(wordFreqDto.freq);
            PartOfSpeech partOfSpeech = partOfSpeechRepository.get(wordFreqDto.partOfSpeech.id);
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
