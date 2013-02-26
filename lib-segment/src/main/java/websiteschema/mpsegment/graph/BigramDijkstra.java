package websiteschema.mpsegment.graph;

import websiteschema.mpsegment.dict.IWord;

public class BigramDijkstra extends DijkstraImpl {

    private WordBigram wordBigram;

    public BigramDijkstra(WordBigram wordBigram) {
        super();
        this.wordBigram = wordBigram;
    }

    @Override
    protected int getEdgeWeight(int location, int vertex) {
        if(location >= 0) {
            IWord lastWord = getEdgeObject(location);
            IWord word = getEdgeObject(location, vertex);
            if(null != lastWord && null != word) {
                double conditionProb = getConditionProbability(lastWord.getWordName(), word.getWordName());
                if(conditionProb > 0.0001) {
                    conditionProb = -Math.log(conditionProb);
//                    System.out.println("Found Bigram: " + lastWord.getWordName() + " " + word.getWordName() + " prob: " + conditionProb);
                    return (int)((conditionProb / 3) + super.getEdgeWeight(location, vertex) * 2 / 3);
                }
            }
        }
        return super.getEdgeWeight(location, vertex);
    }

    protected double getConditionProbability(String word1, String word2) {
        return wordBigram.getProbability(word1, word2);
    }

    protected IWord getEdgeObject(int tail) {
        if(getDijkstraElement().hasFoundShortestPathTo(tail)) {
            int head = getNearestNeighbor(tail);
            return getEdgeObject(head, tail);
        }
        return null;
    }

    private IWord getEdgeObject(int location, int vertex) {
        return getGraph().getEdgeObject(location, vertex);
    }

    private int getNearestNeighbor(int end) {
        return getRouteBackTrace()[end];
    }
}
