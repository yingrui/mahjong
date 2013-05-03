package websiteschema.mpsegment.web.api.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import websiteschema.mpsegment.web.UsingFixtures;
import websiteschema.mpsegment.web.api.model.Concept;
import websiteschema.mpsegment.web.api.model.PartOfSpeech;
import websiteschema.mpsegment.web.api.model.WordItem;
import websiteschema.mpsegment.web.ui.model.User;
import websiteschema.mpsegment.web.ui.service.UserService;

import java.util.Iterator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WordItemServiceTest extends UsingFixtures {

    private WordItemService wordItemService = resolve("wordItemServiceImpl", WordItemService.class);
    private ConceptService conceptService = resolve("conceptServiceImpl", ConceptService.class);
    private UserService userService = resolve("userServiceImpl", UserService.class);
    private String currentUserEmail = uniq("yingrui.f@gmail.com");

    @Before
    public void onSetUp() {
        setUpCurrentUser();
    }

    private void setUpCurrentUser() {
        User user = new User();
        user.setEmail(currentUserEmail);
        userService.addUser(user);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(currentUserEmail);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null);

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void should_add_word_item_in_database() {
        String wordName = uniq("WordName");

        WordItem wordItem = addWord(wordName, "pinyin");

        WordItem wordItemInDatabase = wordItemService.getById(wordItem.getId());
        assertNotNull(wordItemInDatabase);
        assertEquals(wordName, wordItemInDatabase.getWord());
        assertEquals(1, wordItemInDatabase.getPinyinSet().size());
        assertTrue(wordItemInDatabase.getPinyinSet().contains("pinyin"));
    }

    @Test
    public void should_add_current_user_as_creator_of_word() {
        String wordName = uniq("WordName");

        WordItem wordItem = addWord(wordName, "pinyin");

        WordItem wordItemInDatabase = wordItemService.getById(wordItem.getId());
        assertNotNull(wordItemInDatabase);
        User user = wordItemInDatabase.getUser();
        assertEquals(currentUserEmail, user.getEmail());
    }

    @Test
    public void should_add_part_of_speech_to_word() {
        String wordName = uniq("WordName");

        WordItem wordItem = new WordItem();
        wordItem.setWord(wordName);
        wordItem.getPinyinSet().add("pinyin");
        wordItem.getPartOfSpeechSet().add(posN);
        wordItem.getPartOfSpeechSet().add(posT);
        wordItemService.add(wordItem);

        WordItem wordItemInDatabase = wordItemService.getById(wordItem.getId());
        assertNotNull(wordItemInDatabase);
        assertEquals(2, wordItemInDatabase.getPartOfSpeechSet().size());
        Iterator<PartOfSpeech> iterator = wordItemInDatabase.getPartOfSpeechSet().iterator();
        String actualPos = iterator.next().getName() + iterator.next().getName();
        assertTrue(actualPos.contains("N"));
        assertTrue(actualPos.contains("T"));
    }

    @Test
    public void should_add_concepts_to_word() {
        String wordName = uniq("WordName");
        String c = uniq("Concept");

        WordItem wordItem = new WordItem();
        wordItem.setWord(wordName);
        wordItem.getPinyinSet().add("pinyin");
        wordItem.getPartOfSpeechSet().add(posN);
        wordItem.getPartOfSpeechSet().add(posT);

        Concept concept = addConcept(c, posN, null);
        wordItem.getConceptSet().add(concept);

        wordItemService.add(wordItem);

        WordItem wordItemInDatabase = wordItemService.getById(wordItem.getId());
        assertNotNull(wordItemInDatabase);
        assertFalse(wordItemInDatabase.getConceptSet().isEmpty());
        assertEquals(c, wordItemInDatabase.getConceptSet().iterator().next().getName());
    }

    private WordItem addWord(String wordName, String pinyin) {
        WordItem wordItem = new WordItem();
        wordItem.setWord(wordName);
        wordItem.getPinyinSet().add(pinyin);
        wordItemService.add(wordItem);
        return wordItem;
    }

    private Concept addConcept(String c, PartOfSpeech pos, Concept parent) {
        Concept concept = new Concept();
        concept.setName(c);
        concept.setNote(uniq("Note"));
        concept.setPartOfSpeech(pos);
        concept.setParent(parent);
        conceptService.add(concept);
        return concept;
    }

}
