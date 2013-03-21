//package websiteschema.mpsegment.graph;
//
//import org.junit.Assert;
//import org.junit.Ignore;
//import org.junit.Test;
//import websiteschema.mpsegment.dict.IWord;
//import websiteschema.mpsegment.dict.WordImpl;
//
//@Ignore
//class BigramDijkstraTest {
//
//    private var wordBigram : WordBigram = null
//
//    public BigramDijkstraTest() {
//        wordBigram = new WordBigram("word-bigram.dat");
//    }
//
//    @Test
//    def should_return_edge_object_according_tail() {
//        var dijkstra = new BigramDijkstra(wordBigram)
//        dijkstra.setGraph(createGraph());
//        dijkstra.getShortestPath(0, 4);
//        var word1 = dijkstra.getEdgeObject(3)
//        Assert.assertEquals("S2S3", word1.getWordName());
//        var word2 = dijkstra.getEdgeObject(1)
//        Assert.assertEquals("S1", word2.getWordName());
//        var word3 = dijkstra.getEdgeObject(2)
//        Assert.assertEquals("S2", word3.getWordName());
//    }
//
//    @Test
//    def should_get_bigram_data() {
//        var dijkstra = new BigramDijkstra(wordBigram)
//        var prob = dijkstra.getConditionProbability("好", "地")
//        Assert.assertTrue(prob > -0.00000001);
//    }
//
//    private def createGraph() : IGraph = {
//        var graph = new Graph()
//        graph.addEdge(0, 1, 1, new WordImpl("S1"));
//        graph.addEdge(0, 2, 5, new WordImpl("S1S2"));
//        graph.addEdge(1, 2, 2, new WordImpl("S2"));
//        graph.addEdge(1, 3, 2, new WordImpl("S2S3"));
//        graph.addEdge(2, 3, 3, new WordImpl("S3"));
//        graph.addEdge(0, 3, 5, new WordImpl("S1S2S3"));
//        graph.addEdge(3, 4, 1, new WordImpl("S1S2S3"));
//        return graph;
//    }
//
//}
