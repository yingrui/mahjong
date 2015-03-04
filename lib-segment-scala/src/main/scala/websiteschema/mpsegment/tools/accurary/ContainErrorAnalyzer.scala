package websiteschema.mpsegment.tools.accurary

import websiteschema.mpsegment.core.Word
import collection.mutable.Map

class ContainErrorAnalyzer extends AbstractErrorAnalyzer {

    override def analysis(expect: Word, possibleErrorWord: String) : Boolean = {
        var foundError = false
        if (possibleErrorWord.length() == 0) {
            // Bigger word contains the littler words.
            // Should remove the word from dictionary.
            increaseOccur()
//            println(expect.word + " in " + possibleErrorWord)
            foundError = true
        } else if (!possibleErrorWord.replaceAll(" ", "").equals(expect.name)
                && possibleErrorWord.contains(expect.name)) {
            increaseOccur()
            addErrorWord(possibleErrorWord)
//            println(expect.word + " in " + possibleErrorWord)
            foundError = true
        }
        return foundError
    }

    override def postAnalysis(allWordsAndFreqInCorpus: Map[String,Int]) {
        for(word <- getWords().keys) {
            if(allWordsAndFreqInCorpus.contains(word)) {
                removeErrorWord(word)
            }
        }
    }
}
