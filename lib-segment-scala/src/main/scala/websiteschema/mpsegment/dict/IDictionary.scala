package websiteschema.mpsegment.dict;

trait IDictionary {

    def getWord(wordStr: String) : IWord

    def getWords(wordStr: String) : Array[IWord]

    def iterator() : List[IWord]

}
