package websiteschema.mpsegment.concept

;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

class ConceptRepositoryTest {

  val text = "1 名词 n-noun\n" +
    "1.1 事物 n-object\n" +
    "1.1.1 具体事物 n-entity\n" +
    "1.1.1.1 生物 n-creature\n" +
    "1.4 时间 n-time\n" +
    "1.4.1 绝对时间 n-absolute-time\n" +
    "1.4.10 相对时间 n-relative-time\n" +
    "1.1.1.2.1.14.3 武器 n-weapon";

  var conceptTree: ConceptTree = null

  try {
    val inputStream = new ByteArrayInputStream(text.getBytes("utf-8"))
    val loader = ConceptLoader(inputStream)
    conceptTree = loader.getConceptTree();
  } catch {
    case ex: Exception =>
      ex.printStackTrace();
  }

  @Test
  def should_load_noun_verb_adj_concepts_from_resources() {
    try {
      var conceptRepository = new ConceptRepository()
      var nounConceptTree = conceptRepository.getNounConceptTree()
      var verbConceptTree = conceptRepository.getVerbConceptTree()
      var adjConceptTree = conceptRepository.getAdjConceptTree()
    } catch {
      case ex: Exception =>
        ex.printStackTrace();
        Assert.fail();
    }
  }

  @Test
  def should_return_concept_by_name() {
    try {
      val conceptRepository = new ConceptRepository()
      conceptRepository.setNounConceptTree(conceptTree);

      var concept = conceptRepository.getConceptByName("n-noun")
      Assert.assertEquals(1, concept.getId());
      Assert.assertEquals("n-noun", concept.getName());

      concept = conceptRepository.getConceptByName("n-UNKNOWN");
      Assert.assertNull(concept);
    } catch {
      case ex: Exception =>
        ex.printStackTrace();
        Assert.fail();
    }
  }
}
