package websiteschema.mpsegment.web.api.model;

import org.junit.Test;
import websiteschema.mpsegment.web.api.model.dto.WordItemDto;
import websiteschema.mpsegment.web.ui.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WordItemTest {

    Date now = new Date();

    @Test
    public void should_return_word_dto_when_convert_word_item() {
        WordItemDto word = createWordItem().toDto();

        assertEquals("wordItem", word.word);
        assertEquals("core", word.type);
        assertEquals(1, word.id);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        assertEquals(dateFormat.format(now), word.createAt);

        assertEquals("FirstName", word.user.firstName);
        assertEquals("LastName", word.user.lastName);
        assertEquals("m@g.cn", word.user.email);

        assertTrue(word.pinyinSet.contains("pinyin"));

        assertEquals(10, word.partOfSpeeches.iterator().next().freq);
        assertEquals("N", word.partOfSpeeches.iterator().next().partOfSpeech.name);

        assertEquals("n-information", word.conceptSet.iterator().next().name);
        assertEquals("N", word.conceptSet.iterator().next().partOfSpeech.name);
    }

    private WordItem createWordItem() {
        WordItem wordItem = new WordItem();
        wordItem.setName("wordItem");
        wordItem.setId(1);
        wordItem.setType(DomainType.core);

        User user = new User();
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setEmail("m@g.cn");
        wordItem.setUser(user);


        wordItem.setCreateAt(now);

        PartOfSpeech partOfSpeech = new PartOfSpeech();
        partOfSpeech.setName("N");
        WordFreq wordFreq = new WordFreq();
        wordFreq.setFreq(10);
        wordFreq.setPartOfSpeech(partOfSpeech);
        wordFreq.setId(1);
        wordItem.getWordFreqSet().add(wordFreq);

        Pinyin pinyin = new Pinyin();
        pinyin.setName("pinyin");
        wordItem.getPinyinSet().add(pinyin);

        Concept concept = new Concept();
        concept.setName("n-information");
        concept.setPartOfSpeech(partOfSpeech);
        wordItem.getConceptSet().add(concept);
        return wordItem;
    }
}
