package me.yingrui.segment.hmm

import org.junit.Assert
import org.junit.Test
import me.yingrui.segment.util.SerializeHandler

import java.io.File

class TrieNodeTest {

    var filename = "test_trie.dat"

    @Test
    def should_insert_ngram_and_add_count() {
        val trie = new Trie()
        trie.insert(List[Int](1, 2).toArray)
        trie.insert(List[Int](1, 2).toArray)
        trie.insert(List[Int](1, 2).toArray)
        Assert.assertEquals(3, trie.searchNode(List[Int](1,2).toArray).getCount())
        trie.buildIndex(1)
        Assert.assertTrue(trie.searchNode(List[Int](1,2).toArray).getProb() - 0.75 < 0.0000001)
    }

    @Test
    def should_save_Trie_into_a_file_and_load_it_correctly() {
        val f = new File(filename)
        f.deleteOnExit()
        val root = create()
        val writeHandler = SerializeHandler(f, SerializeHandler.WRITE_ONLY)
        root.save(writeHandler)
        writeHandler.close()

        val readHandler = SerializeHandler(f, SerializeHandler.READ_ONLY)
        val copy = new Trie()
        copy.load(readHandler)
        readHandler.close()

        Assert.assertEquals(copy.key, 0)
        Assert.assertEquals(copy.count, 0)
        Assert.assertEquals(copy.descendant.length, 2)

        val child1 = copy.descendant(0)
        Assert.assertEquals(child1.key, 1)
        Assert.assertEquals(child1.count, 1)
        Assert.assertEquals(child1.descendant.length, 1)

        val grandChild1 = child1.descendant(0)
        Assert.assertEquals(grandChild1.key, 3)
        Assert.assertEquals(grandChild1.count, 3)
        assert (grandChild1.prob - 0.3 > -0.0000001 && grandChild1.prob - 0.3 < 0.0000001)
        Assert.assertNull(grandChild1.descendant)

        val child2 = copy.descendant(1)
        Assert.assertEquals(child2.key, 2)
        Assert.assertEquals(child2.count, 2)
        Assert.assertNull(child2.descendant)
    }

    private def create(): Trie = {
        val root = create(0, 0, 0.01)
        val child1 = create(1, 1, 0.1)
        val child2 = create(2, 2, 0.2)
        val grandChild1 = create(3, 3, 0.3)
        child1.add(grandChild1)
        root.add(child1)
        root.add(child2)
        return root
    }

    private def create(key: Int, count: Int, prob: Double): Trie = {
        val trie = new Trie()
        trie.key = key
        trie.count = count
        trie.prob = prob
        return trie
    }
}
