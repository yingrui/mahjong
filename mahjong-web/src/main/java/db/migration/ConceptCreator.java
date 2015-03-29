package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;
import scala.collection.immutable.List;
import me.yingrui.segment.concept.Concept;
import me.yingrui.segment.concept.ConceptRepository;
import me.yingrui.segment.concept.ConceptTree;
import me.yingrui.segment.dict.POSUtil;

import java.util.HashMap;

public class ConceptCreator {

    private HashMap<String, Integer> mapConceptId = new HashMap<String, Integer>();

    public void createConcepts(JdbcTemplate jdbcTemplate) {
        createConceptsOfNoun(jdbcTemplate);
        createConceptsOfVerb(jdbcTemplate);
        createConceptsOfAdj(jdbcTemplate);
    }

    private void createConceptsOfNoun(JdbcTemplate jdbcTemplate) {
        ConceptTree nounConceptTree = ConceptRepository.apply().getNounConceptTree();
        Concept rootConcept = nounConceptTree.getRootConcept();
        createConceptRecursively(jdbcTemplate, rootConcept, POSUtil.POS_N());
    }

    private void createConceptsOfVerb(JdbcTemplate jdbcTemplate) {
        ConceptTree verbConceptTree = ConceptRepository.apply().getVerbConceptTree();
        Concept rootConcept = verbConceptTree.getRootConcept();
        createConceptRecursively(jdbcTemplate, rootConcept, POSUtil.POS_V());
    }

    private void createConceptsOfAdj(JdbcTemplate jdbcTemplate) {
        ConceptTree adjConceptTree = ConceptRepository.apply().getAdjConceptTree();
        Concept rootConcept = adjConceptTree.getRootConcept();
        createConceptRecursively(jdbcTemplate, rootConcept, POSUtil.POS_A());
    }

    private void createConceptRecursively(JdbcTemplate jdbcTemplate, Concept rootConcept, int partOfSpeech) {
        createConcept(jdbcTemplate, rootConcept, partOfSpeech);
        List<Concept> children = rootConcept.getChildren();
        if (children != null && !children.isEmpty()) {
            for (int i = 0; i < children.length(); i++) {
                createConceptRecursively(jdbcTemplate, children.apply(i), partOfSpeech);
            }
        }
    }

    private void createConcept(JdbcTemplate jdbcTemplate, Concept concept, int partOfSpeech) {
        Concept parent = concept.getParent();
        int parentId = 0;
        if (null != parent) {
            parentId = mapConceptId.get(parent.getName());
            jdbcTemplate.execute("INSERT INTO Concepts (ParentId, Name, PartOfSpeech, Note, CreateAt) VALUES ("
                    + parentId + ", '" + concept.getName() + "', " + partOfSpeech + ", '" + concept.getDescription() + "', 'now')");
        } else {
            jdbcTemplate.execute("INSERT INTO Concepts (Name, PartOfSpeech, Note, CreateAt) VALUES ('"
                    + concept.getName() + "', " + partOfSpeech + ", '" + concept.getDescription() + "', 'now')");
        }
        int id = jdbcTemplate.queryForInt("SELECT Id FROM Concepts WHERE Name='" + concept.getName() + "'");
        mapConceptId.put(concept.getName(), id);
    }

}
