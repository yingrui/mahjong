package websiteschema.mpsegment.web.api.service;

import org.junit.Before;
import org.junit.Test;
import websiteschema.mpsegment.dict.POSUtil;
import websiteschema.mpsegment.web.UsingUserFixtures;
import websiteschema.mpsegment.web.api.model.PartOfSpeech;
import websiteschema.mpsegment.web.api.model.Pinyin;
import websiteschema.mpsegment.web.api.model.WordFreq;
import websiteschema.mpsegment.web.api.model.WordItem;
import websiteschema.mpsegment.web.api.model.dto.PartOfSpeechDto;
import websiteschema.mpsegment.web.api.model.dto.WordFreqDto;
import websiteschema.mpsegment.web.api.model.dto.WordItemDto;

import static org.junit.Assert.assertEquals;

public class WordItemUpdateRequestMergerTest extends UsingUserFixtures {

    private WordItemService wordItemService = resolve("wordItemServiceImpl", WordItemService.class);
    private String currentUserEmail = uniq("yingrui.f@gmail.com");
    private WordItem wordItem = null;

    @Before
    public void onSetUp() {
        setUpCurrentUser(currentUserEmail);

        String wordName = uniq("WordName");
        wordItem = addWord(wordName, "pinyin", posN);
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

        WordItemUpdateRequestMerger merger = resolve("wordItemUpdateRequestMerger", WordItemUpdateRequestMerger.class);

        merger.merge(dto).to(wordItem);

        assertEquals(expectedWord, wordItem.getName());
        assertEquals("core1", wordItem.getType());
        assertEquals(1, wordItem.getPinyinSet().size());
        assertEquals("expected'word", wordItem.getPinyinSet().iterator().next().getName());
        assertEquals(1, wordItem.getWordFreqSet().size());
        assertEquals(POSUtil.POS_UNKOWN(), wordItem.getWordFreqSet().iterator().next().getPartOfSpeech().getId());
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
}
