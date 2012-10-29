/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.concept;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ConceptTreeTest {

    ConceptTree conceptTree;
    Concept nounRoot = new Concept(100000, "noun");
    Concept nounObject = new Concept(100001, "object");
    Concept nounProcess = new Concept(100002, "process");
    Concept nounSpace = new Concept(100003, "space");
    Concept nounTime = new Concept(100004, "time");

    public ConceptTreeTest() {
        conceptTree = new ConceptTree();
        buildTestData();
    }

    @Test
    public void should_get_concept_by_id_or_name() {
        Assert.assertEquals(nounRoot, conceptTree.getConceptById(100000));
        Assert.assertEquals(nounObject, conceptTree.getConceptById(100001));
        Assert.assertEquals(nounProcess, conceptTree.getConceptByName("process"));
        Assert.assertEquals(nounSpace, conceptTree.getConceptByName("space"));
    }

    @Test
    public void should_return_error_when_add_a_concept_which_id_already_existed() {
        Concept nounDuplicate = new Concept(100004, "duplicate");
        try {
            conceptTree.addConcept(nounDuplicate, nounRoot);
            Assert.fail();
        } catch (DuplicateConceptException ex) {
            Assert.assertEquals(ex.getMessage(), "A concept 'time' has id 100004 already existed.");
        }
    }

    @Test
    public void should_return_error_when_add_a_concept_which_name_already_existed() {
        Concept nounDuplicate = new Concept(100005, "noun");
        try {
            conceptTree.addConcept(nounDuplicate, nounRoot);
            Assert.fail();
        } catch (DuplicateConceptException ex) {
            Assert.assertEquals(ex.getMessage(), "A concept 'noun' has id 100000 already existed.");
        }
    }

    @Test
    public void should_add_concept_to_concept_tree_correctly() {
        Concept nounRoot = conceptTree.getRootConcept();
        List<Concept> children = conceptTree.getRootConcept().getChildren();
        Assert.assertEquals(4, children.size());
        Assert.assertEquals("object", children.get(0).getName());
        Assert.assertEquals(100001, children.get(0).getId());
        Assert.assertEquals(nounRoot, children.get(0).getParent());
        Assert.assertEquals(children, children.get(0).getSiblings());

        Assert.assertEquals("process", children.get(1).getName());
        Assert.assertEquals(100002, children.get(1).getId());
        Assert.assertEquals("space", children.get(2).getName());
        Assert.assertEquals(100003, children.get(2).getId());
        Assert.assertEquals("time", children.get(3).getName());
        Assert.assertEquals(100004, children.get(3).getId());
    }

    private void buildTestData() {
        try {
            conceptTree.addConcept(nounRoot);
            conceptTree.addConcept(nounObject, nounRoot);
            addConceptToRoot(nounProcess, nounSpace, nounTime);
        } catch (DuplicateConceptException ex) {

        }
    }

    private void addConceptToRoot(Concept nounProcess, Concept nounSpace, Concept nounTime) throws DuplicateConceptException {
        conceptTree.addConcept(nounProcess);
        conceptTree.addConcept(nounSpace);
        conceptTree.addConcept(nounTime);
    }

}
