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
import me.yingrui.segment.web.Application;
import me.yingrui.segment.web.UsingFixtures;
import me.yingrui.segment.web.api.model.Concept;
import me.yingrui.segment.web.api.model.dto.ConceptDto;
import me.yingrui.segment.web.api.service.ConceptService;
import me.yingrui.segment.web.util.PojoMapper;

import java.io.IOException;
import java.util.List;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@WebAppConfiguration
@IntegrationTest({"server.port:0"})
public class ConceptControllerTest extends UsingFixtures {

    @Value("${local.server.port}")
    int port;

    @Autowired
    private ConceptService conceptService;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void should_list_all_concept() throws IOException {
        String c = uniq("Concept");

        Concept concept = new Concept();
        concept.setName(c);
        concept.setNote(uniq("Note"));
        concept.setPartOfSpeech(posN);
        conceptService.add(concept);

        ValidatableResponse validatableResponse = when().get("/concept").then().statusCode(HttpStatus.SC_OK);

        String content = validatableResponse.extract().response().asString();
        List<ConceptDto> conceptDtos = PojoMapper.fromJsonArray(content, ConceptDto.class);
        assertThat(conceptDtos.get(conceptDtos.size() - 1).id, is(concept.getId()));
    }

}
