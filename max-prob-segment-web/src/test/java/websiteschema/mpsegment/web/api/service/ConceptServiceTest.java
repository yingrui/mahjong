package websiteschema.mpsegment.web.api.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.mpsegment.web.UsingFixtures;
import websiteschema.mpsegment.web.api.model.Concept;
import websiteschema.mpsegment.web.api.model.PartOfSpeech;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Transactional
public class ConceptServiceTest extends UsingFixtures {

    private ConceptService conceptService = resolve("conceptServiceImpl", ConceptService.class);
    private EntityManager em = resolve("entityManagerFactory", EntityManagerFactory.class).createEntityManager();

    private PartOfSpeech posN;
    private PartOfSpeech posT;

    @Before
    public void onSetUp() {
        posN = addPartOfSpeech(1000, "名词", "N");
    }

    private PartOfSpeech addPartOfSpeech(int id, String note, String pos) {
        PartOfSpeech partOfSpeech = new PartOfSpeech();
        partOfSpeech.setId(id);
        partOfSpeech.setNote(note);
        partOfSpeech.setPartOfSpeech(pos);
        em.persist(partOfSpeech);
        return partOfSpeech;
    }

    @Test
    public void should_add_concept_in_database() {
        String c = uniq("Concept");

        Concept concept = addConcept(c, posN);

        Concept actualConcept = conceptService.getById(concept.getId());
        assertNotNull(actualConcept);
        assertEquals("N", actualConcept.getPartOfSpeech().getPartOfSpeech());
        assertEquals(c, actualConcept.getName());
    }

    private Concept addConcept(String c, PartOfSpeech pos) {
        Concept concept = new Concept();
        concept.setName(c);
        concept.setNote(uniq("Note"));
        concept.setPartOfSpeech(pos);
        conceptService.add(concept);
        return concept;
    }
}
