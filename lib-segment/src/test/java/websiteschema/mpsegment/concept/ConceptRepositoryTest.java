package websiteschema.mpsegment.concept;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConceptRepositoryTest {

    String text = "1 名词 n-noun\n" +
            "1.1 事物 n-object\n" +
            "1.1.1 具体事物 n-entity\n" +
            "1.1.1.1 生物 n-creature\n" +
            "1.4 时间 n-time\n" +
            "1.4.1 绝对时间 n-absolute-time\n" +
            "1.4.10 相对时间 n-relative-time\n" +
            "1.1.1.2.1.14.3 武器 n-weapon";

    ConceptTree conceptTree;

    public ConceptRepositoryTest() {
        try {
            InputStream inputStream = new ByteArrayInputStream(text.getBytes("utf-8"));
            ConceptLoader loader = new ConceptLoader(inputStream);
            conceptTree = loader.getConceptTree();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void should_load_noun_verb_adj_concepts_from_resources() {
        try {
            ConceptRepository conceptRepository = new ConceptRepository();
            ConceptTree nounConceptTree = conceptRepository.getNounConceptTree();
            ConceptTree verbConceptTree = conceptRepository.getVerbConceptTree();
            ConceptTree adjConceptTree = conceptRepository.getAdjConceptTree();
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void should_return_concept_by_name() {
        try {
            ConceptRepository conceptRepository = new ConceptRepository();
            conceptRepository.setNounConceptTree(conceptTree);

            Concept concept = conceptRepository.getConceptByName("n-noun");
            Assert.assertEquals(1, concept.getId());
            Assert.assertEquals("n-noun", concept.getName());

            concept = conceptRepository.getConceptByName("n-UNKNOWN");
            Assert.assertNull(concept);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }
}
