/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.hmm;

import java.util.*;

/**
 *
 * @author ray
 */
public class TrieNodeBinarySort implements TrieNodeSortor {

    Comparator<Trie> comparator = null;
    
    public TrieNodeBinarySort() {
        comparator = new Comparator<Trie>() {

            @Override
            public int compare(Trie o1, Trie o2) {
                return o1.key - o2.key;
            }
        };
    }

    @Override
    public Trie[] sort(Trie[] values) {
        List<Trie> tmp = new ArrayList<Trie>();
        tmp.addAll(Arrays.asList(values));
        Collections.sort(tmp, comparator);
        return tmp.toArray(values);
    }
}
