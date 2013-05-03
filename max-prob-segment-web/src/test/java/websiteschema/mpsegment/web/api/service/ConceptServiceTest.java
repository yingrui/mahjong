package websiteschema.mpsegment.web.api.service;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.mpsegment.web.UsingFixtures;
import websiteschema.mpsegment.web.api.model.Concept;
import websiteschema.mpsegment.web.api.model.PartOfSpeech;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Transactional
public class ConceptServiceTest extends UsingFixtures {

    private ConceptService conceptService = resolve("conceptServiceImpl", ConceptService.class);

    @Test
    public void should_add_concept_in_database() {
        String c = uniq("Concept");

        Concept concept = addConcept(c, posN, null);

        Concept actualConcept = conceptService.getById(concept.getId());
        assertNotNull(actualConcept);
        assertEquals("N", actualConcept.getPartOfSpeech().getName());
        assertEquals(c, actualConcept.getName());
    }

    @Test
    public void should_add_parent_concept() {
        String c1 = uniq("Concept");
        String c2 = uniq("Concept");

        Concept concept1 = addConcept(c1, posN, null);
        Concept concept2 = addConcept(c2, posT, concept1);

        Concept actualConcept = conceptService.getById(concept2.getId());
        assertNotNull(actualConcept);
        assertEquals("N", actualConcept.getParent().getPartOfSpeech().getName());
        assertEquals(c1, actualConcept.getParent().getName());
        assertEquals(c2, actualConcept.getName());
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
