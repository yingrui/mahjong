package websiteschema.mpsegment.concept;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ConceptLoaderTest {

    String text = "1 名词 n-noun\n" +
            "1.1 事物 n-object\n" +
            "1.1.1 具体事物 n-entity\n" +
            "1.1.1.1 生物 n-creature\n" +
            "1.4 时间 n-time\n" +
            "1.4.1 绝对时间 n-absolute-time\n" +
            "1.4.10 相对时间 n-relative-time\n" +
            "1.1.1.2.1.14.3 武器 n-weapon";

    ConceptTree conceptTree;

    public ConceptLoaderTest() {
        try {
            InputStream inputStream = new ByteArrayInputStream(text.getBytes("utf-8"));
            ConceptLoader loader = new ConceptLoader(inputStream);
            conceptTree = loader.getConceptTree();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void should_load_concept_tree_correctly() {
        Concept root = conceptTree.getRootConcept();
        Assert.assertEquals(1, root.getId());

        List<Concept> children = root.getChildren();
        Assert.assertEquals(3, children.size());
        Assert.assertEquals("n-object", children.get(0).getName());
        Assert.assertEquals(101, children.get(0).getId());
        Assert.assertEquals("n-time", children.get(1).getName());

        List<Concept> grandChildren = root.getChildren().get(0).getChildren();
        Assert.assertEquals(1, grandChildren.size());
        Assert.assertEquals(10101, grandChildren.get(0).getId());
        Assert.assertEquals("n-entity", grandChildren.get(0).getName());
    }
}
