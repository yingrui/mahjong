package websiteschema.mpsegment.web.api.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import websiteschema.mpsegment.dict.POSUtil;
import websiteschema.mpsegment.web.Application;
import websiteschema.mpsegment.web.UsingUserFixtures;
import websiteschema.mpsegment.web.api.model.*;
import websiteschema.mpsegment.web.api.model.dto.ConceptDto;
import websiteschema.mpsegment.web.api.model.dto.PartOfSpeechDto;
import websiteschema.mpsegment.web.api.model.dto.WordFreqDto;
import websiteschema.mpsegment.web.api.model.dto.WordItemDto;

import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
public class WordItemUpdateRequestMergerTest extends UsingUserFixtures {

    @Autowired
    private WordItemService wordItemService;

    @Autowired
    WordItemUpdateRequestMerger merger;

    @Autowired
    private ConceptService conceptService;
    private String currentUserEmail = uniq("yingrui.f@gmail.com");
    private WordItem wordItem = null;
    protected Concept adj;
    protected Concept noun;
    protected Concept verb;

    @Before
    public void onSetUp() {
        setUpCurrentUser(currentUserEmail);

        String wordName = uniq("WordName");
        wordItem = addWord(wordName, "pinyin", posN);
        initConcepts();
    }

    @Test
    public void should_merge_word_name_from_dto_to_word() {
        String expectedWord = "ExpectedWord";

        WordItemDto dto = new WordItemDto(expectedWord);
        dto.type = "core1";
        dto.pinyinSet.clear();
        dto.pinyinSet.add("expected'word");

        dto.partOfSpeeches.clear();
        WordFreqDto wordFreqDto = createPosUnknown();
        dto.partOfSpeeches.add(wordFreqDto);

        dto.conceptSet.clear();
        ConceptDto adj = new ConceptDto();
        adj.name = "a-adj";
        ConceptDto verb = new ConceptDto();
        verb.name = "v-verb";
        dto.conceptSet.add(adj);
        dto.conceptSet.add(verb);

        merger.merge(dto).to(wordItem);

        assertEquals(expectedWord, wordItem.getName());
        assertEquals("core1", wordItem.getType());
        assertEquals(1, wordItem.getPinyinSet().size());
        assertEquals("expected'word", wordItem.getPinyinSet().iterator().next().getName());
        assertEquals(1, wordItem.getWordFreqSet().size());
        assertEquals(POSUtil.POS_UNKOWN(), wordItem.getWordFreqSet().iterator().next().getPartOfSpeech().getId());
        assertEquals(2, wordItem.getConceptSet().size());
        Concept[] concepts = new ArrayList<Concept>(wordItem.getConceptSet()).toArray(new Concept[0]);
        Arrays.sort(concepts, new Comparator<Concept>() {
            public int compare(Concept o1, Concept o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        assertEquals("形容词", concepts[0].getNote());
        assertEquals("动词", concepts[1].getNote());
    }

    private WordFreqDto createPosUnknown() {
        WordFreqDto wordFreqDto = new WordFreqDto();
        wordFreqDto.freq = 100;
        PartOfSpeechDto partOfSpeechDto = new PartOfSpeechDto();
        partOfSpeechDto.id = POSUtil.POS_UNKOWN();
        partOfSpeechDto.name = POSUtil.getPOSString(partOfSpeechDto.id);
        partOfSpeechDto.note = "未登录词";
        wordFreqDto.partOfSpeech = partOfSpeechDto;
        return wordFreqDto;
    }

    private WordItem addWord(String wordName, String pinyin, PartOfSpeech... pos) {
        WordItem wordItem = new WordItem();
        wordItem.setName(wordName);

        Pinyin p = new Pinyin();
        p.setName(pinyin);
        wordItem.getPinyinSet().add(p);

        for (PartOfSpeech partOfSpeech : pos) {
            WordFreq wordFreq = new WordFreq();
            wordFreq.setFreq(10);
            wordFreq.setPartOfSpeech(partOfSpeech);
            wordItem.getWordFreqSet().add(wordFreq);
        }

        wordItemService.add(wordItem);
        return wordItem;
    }

    private void initConcepts() {
        adj = addConcept("a-adj", "形容词", posA);
        verb = addConcept("v-verb", "动词", posV);
        noun = addConcept("n-noun", "名词", posN);
    }

    private Concept addConcept(String name, String note, PartOfSpeech partOfSpeech) {
        Concept concept = new Concept();
        concept.setName(name);
        concept.setNote(note);
        concept.setPartOfSpeech(partOfSpeech);
        conceptService.add(concept);
        return concept;
    }
}
