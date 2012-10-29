/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.hmm;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.util.SerializeHandler;

/**
 * @author twer
 */
public class TrieNodeTest {

    String filename = "test_trie.dat";

    @Test
    public void should_save_Trie_into_a_file_and_load_it_correctly() throws IOException {
        File f = new File(filename);
        f.deleteOnExit();
        Trie root = create();
        SerializeHandler writeHandler = new SerializeHandler(f, SerializeHandler.MODE_WRITE_ONLY);
        root.save(writeHandler);
        writeHandler.close();

        SerializeHandler readHandler = new SerializeHandler(f, SerializeHandler.MODE_READ_ONLY);
        Trie copy = new Trie();
        copy.load(readHandler);
        readHandler.close();

        Assert.assertEquals(copy.key, 0);
        Assert.assertEquals(copy.count, 0);
        Assert.assertEquals(copy.descendant.length, 2);

        Trie child1 = copy.descendant[0];
        Assert.assertEquals(child1.key, 1);
        Assert.assertEquals(child1.count, 1);
        Assert.assertEquals(child1.descendant.length, 1);

        Trie grandChild1 = child1.descendant[0];
        Assert.assertEquals(grandChild1.key, 3);
        Assert.assertEquals(grandChild1.count, 3);
        assert (grandChild1.prob - 0.3 > -0.0000001 && grandChild1.prob - 0.3 < 0.0000001);
        Assert.assertNull(grandChild1.descendant);

        Trie child2 = copy.descendant[1];
        Assert.assertEquals(child2.key, 2);
        Assert.assertEquals(child2.count, 2);
        Assert.assertNull(child2.descendant);
    }

    private Trie create() {
        Trie root = create(0, 0, 0.01);
        Trie child1 = create(1, 1, 0.1);
        Trie child2 = create(2, 2, 0.2);
        Trie grandChild1 = create(3, 3, 0.3);
        child1.add(grandChild1);
        root.add(child1);
        root.add(child2);
        return root;
    }

    private Trie create(int key, int count, double prob) {
        Trie trie = new Trie();
        trie.key = key;
        trie.count = count;
        trie.prob = prob;
        return trie;
    }
}
