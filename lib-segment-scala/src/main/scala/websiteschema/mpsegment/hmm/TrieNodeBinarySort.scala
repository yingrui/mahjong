package websiteschema.mpsegment.hmm

class TrieNodeBinarySort extends TrieNodeSortor {

    override def sort(values: Array[Trie]) : Array[Trie] = {
        val tmp = values.toList.sortBy(t => t.key)
        return tmp.toArray;
    }
}
