package websiteschema.mpsegment.dict

class HeadIndexer {

    def getWordOccuredSum() : Int = {
        return wordOccuredSum
    }

    def getWordCount() : Int = {
        return wordCount
    }

    def getMaxWordLength() : Int = {
        return maxWordLength
    }

    def getHeadStr() : String = {
        return headStr
    }

    def add(word: IWord) {
        val wordName = word.getWordName()
        if (get(wordName) == null) {
            if (wordName.startsWith(headStr)) {
                addWord(wordName, word)
            } else {
                throw new DictionaryException("Head string is not start with " + headStr)
            }
        }
    }

    private def addWord(wordName: String, word: IWord) {
        if (wordName.length() > getMaxWordLength()) {
            maxWordLength = wordName.length()
        }
        wordCount+=1
        wordOccuredSum+=1
        //NOTE: 5711 HeadIndexers have words less than 64, which total number is 51426,
        //      232 HeadIndexers have words more than 64, which total number is 27261
        if (wordCount > HeadIndexer.Word_Array_Size_Threshold && wordArray.isInstanceOf[BinaryWordArray]) {
            val tmpWordArray = HashWordArray(wordArray.getWordItems())
            wordArray = tmpWordArray
        }
        wordArray.add(word)
    }


    override def toString() : String = {
        val stringBuilder = new StringBuilder()
        stringBuilder.append((new StringBuilder("首字:")).append(getHeadStr()).append("(不同词数量:").append(getWordCount()).toString())
        stringBuilder.append((new StringBuilder(",最长词长度:")).append(getMaxWordLength()).toString())
        stringBuilder.append((new StringBuilder(",所有词的全部出现次数:")).append(getWordOccuredSum()).append(")\n").toString())
        val wordItems = wordArray.getWordItems()
        for (i <- 0 until wordCount) {
            val word = wordItems(i)
            stringBuilder.append(word.toString())
        }

        return stringBuilder.toString()
    }

    def toDBFString() : String = {
        val stringBuilder = new StringBuilder()
        val wordItems = wordArray.getWordItems()
        for (i <- 0 until wordItems.length) {
            val word = wordItems(i)
            stringBuilder.append(word.toDBFString())
            stringBuilder.append("\n")
        }

        return stringBuilder.toString()
    }

    def get(word: String) : IWord = {
        return wordArray.find(word)
    }

    def findMultiWord(wordStr: String) : Array[IWord] = {
        if (wordStr.length() == 1) {
            if (wordStr.equals(headWord.getWordName())) {
                val words = new Array[IWord](1)
                words(0) = headWord
                return words
            } else {
                return null
            }
        }
        var maxWordLen = getMaxWordLength()
        if (wordStr.length() < maxWordLen) {
            maxWordLen = wordStr.length()
        }
        var array = List[IWord]()
        for (i <- 1 until maxWordLen if(array.size < 3)) {
            val candidateWord = wordStr.substring(0, i + 1)
            val word = wordArray.find(candidateWord)
            if (null != word) {
              array = word :: array
            }
        }

        if (array.size > 0) {
            return array.toArray
        } else {
            return null
        }
    }

    def findWord(wordStr: String) : IWord = {
        if (wordStr.length() == 1) {
            if (wordStr.equals(headStr)) {
                return headWord
            } else {
                return null
            }
        }
        var maxWordLen = getMaxWordLength()
        if (wordStr.length() < maxWordLen) {
            maxWordLen = wordStr.length()
        }
        for (i <- 1 until maxWordLen) {
            val candidateWord = wordStr.substring(0, i + 1)
            val word = get(candidateWord)
            if (null != word) {
                return word
            }
        }
        return null
    }

    def getWordArray() : IWordArray = {
        return wordArray
    }

    private var headStr : String = null
    private var maxWordLength : Int = 0
    private var wordOccuredSum : Int = 0
    private var wordCount : Int = 0
    private var headWord : IWord = null
    private var wordArray : IWordArray = null
}

object HeadIndexer {
  private val Word_Array_Size_Threshold = 64

  def apply(headWord:IWord) = create(headWord, 1)

  def apply(headWord: IWord, headLength: Int) = create(headWord, headLength)

  private def create(headWord: IWord, headLength: Int) = {
    val indexer = new HeadIndexer
    indexer.headStr = headWord.getWordName().substring(0, headLength)
    indexer.headWord = headWord
    indexer.wordCount = 1
    indexer.wordOccuredSum = 1
    indexer.maxWordLength = headWord.getWordLength()
    indexer.wordArray = BinaryWordArray(List[IWord](headWord).toArray)
    indexer
  }
}
