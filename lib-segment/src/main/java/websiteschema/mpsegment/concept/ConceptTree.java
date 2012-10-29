package websiteschema.mpsegment.concept;

import java.util.HashMap;
import java.util.Map;

public class ConceptTree {

    Concept rootConcept;
    Map<Long, Concept> mapIntConcept = new HashMap<Long, Concept>();
    Map<String, Concept> mapStringConcept = new HashMap<String, Concept>();
    public Concept getRootConcept() {
        return rootConcept;
    }

    public Concept getConceptById(long id) {
        return mapIntConcept.get(id);
    }

    public Concept getConceptByName(String name) {
        return mapStringConcept.get(name);
    }

    public void addConcept(Concept concept) throws DuplicateConceptException {
        if (null == rootConcept) {
            rootConcept = concept;
            buildIndex(concept);
        } else {
            addConcept(concept, rootConcept);
        }
    }

    public void addConcept(Concept concept, Concept parent) throws DuplicateConceptException {
        verifyUniqConcept(concept);
        buildIndex(concept);
        parent.addChild(concept);
    }

    private void verifyUniqConcept(Concept concept) throws DuplicateConceptException {
        Concept existed = getConceptById(concept.getId());
        if (null != existed) {
            throw new DuplicateConceptException(existed);
        }
        existed = getConceptByName(concept.getName());
        if (null != existed) {
            throw new DuplicateConceptException(existed);
        }
    }

    private void buildIndex(Concept concept) {
        mapIntConcept.put(concept.getId(), concept);
        mapStringConcept.put(concept.getName(), concept);
    }
}
