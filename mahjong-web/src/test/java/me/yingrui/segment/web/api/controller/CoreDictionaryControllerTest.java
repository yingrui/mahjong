package me.yingrui.segment.web.api.controller;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import me.yingrui.segment.dict.DictionaryFactory;
import me.yingrui.segment.dict.HashDictionary;
import me.yingrui.segment.dict.IDictionary;
import me.yingrui.segment.web.Application;
import me.yingrui.segment.web.UsingUserFixtures;
import me.yingrui.segment.web.api.model.Concept;
import me.yingrui.segment.web.api.model.DomainType;
import me.yingrui.segment.web.api.model.WordFreq;
import me.yingrui.segment.web.api.model.WordItem;
import me.yingrui.segment.web.api.service.ConceptService;
import me.yingrui.segment.web.api.service.WordItemService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@WebAppConfiguration
@IntegrationTest({"server.port:0"})
public class CoreDictionaryControllerTest extends UsingUserFixtures {

    @Value("${local.server.port}")
    int port;

    @Autowired
    WordItemService wordItemService;

    @Autowired
    private ConceptService conceptService;

    @Before
    public void setUp() {
        RestAssured.port = port;
        setUpCurrentUser("admin@domain.com");
    }

    @Test
    public void should_return_dictionary() throws IOException {
        String c = uniq("Concept");

        Concept concept = new Concept();
        concept.setName(c);
        concept.setNote(uniq("Note"));
        concept.setPartOfSpeech(posN);
        conceptService.add(concept);

        WordItem wordItem = new WordItem();
        wordItem.setName(uniq("\""));
        wordItem.setType(DomainType.core);
        wordItem.getWordFreqSet().add(new WordFreq(posN, 1));
        wordItem.getWordFreqSet().add(new WordFreq(posA, 100));
        wordItemService.add(wordItem);

        WordItem wordItem2 = new WordItem();
        wordItem2.setName(uniq("中国"));
        wordItem2.setType(DomainType.core);
        wordItem2.getWordFreqSet().add(new WordFreq(posN, 1));
        wordItem2.getConceptSet().add(concept);
        wordItemService.add(wordItem2);

        ValidatableResponse validatableResponse = when().get("/dictionary/core").then().statusCode(HttpStatus.SC_OK);

        String content = validatableResponse.extract().response().asString();

        IDictionary dictionary = new HashDictionary();
        InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));
        DictionaryFactory.apply().loadDictionary(inputStream, dictionary);

        assertThat(dictionary.getWord(wordItem.getName()).getWordName(), is(wordItem.getName()));
        assertThat(dictionary.getWord(wordItem2.getName()).getWordName(), is(wordItem2.getName()));
    }

}
