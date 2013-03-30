package websiteschema.mpsegment.graph

import websiteschema.mpsegment.dict.IWord

class BigramDijkstra(numOfVertexes: Int, wordBigram: WordBigram) extends DijkstraImpl(numOfVertexes: Int) {

    override def getEdgeWeight(location: Int, vertex: Int) : Int = {
        if(location >= 0) {
            val lastWord = getEdgeObject(location)
            val word = getEdgeObject(location, vertex)
            if(null != lastWord && null != word) {
                var conditionProb = getConditionProbability(lastWord.getWordName(), word.getWordName())
                if(conditionProb > 0.0001) {
                    conditionProb = -Math.log(conditionProb)
//                    println("Found Bigram: " + lastWord.getWordName() + " " + word.getWordName() + " prob: " + conditionProb)
                    return ((conditionProb / 3) + super.getEdgeWeight(location, vertex) * 2 / 3).toInt
                }
            }
        }
        return super.getEdgeWeight(location, vertex)
    }

    def getConditionProbability(word1: String, word2: String) : Double = {
        return wordBigram.getProbability(word1, word2)
    }

    def getEdgeObject(tail: Int) : IWord = {
        if(getDijkstraElement().hasFoundShortestPathTo(tail)) {
            val head = getNearestNeighbor(tail)
            return getEdgeObject(head, tail)
        }
        return null
    }

    private def getEdgeObject(location: Int, vertex: Int) : IWord = {
        return getGraph().getEdgeObject(location, vertex)
    }

    private def getNearestNeighbor(end: Int) : Int = {
        return getRouteBackTrace()(end)
    }
}
