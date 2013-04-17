package websiteschema.mpsegment.dict

class TrieDictionary extends IDictionary {

  val step = 100
  var allWords = new Array[IWord](step)
  var top = 0
  val root = new TrieNode('\0', -1)

  def addWord(word: IWord) {
    checkWordArray
    allWords(top) = word
    root.insert(word.getWordName(), top)
    top += 1
  }

  private def checkWordArray {
    if (top >= allWords.length) {
      val temp = allWords
      allWords = new Array[IWord](temp.length + step)
      System.arraycopy(temp, 0, allWords, 0, temp.length)
    }
  }

  override def getWord(wordStr: String): IWord = {
    val word = root.search(wordStr)
    if (word != null && word.index >= 0) allWords(word.index) else null
  }

  override def getWords(wordStr: String): Array[IWord] = {
    getWordList(wordStr).toArray
  }

  def getWordList(wordStr: String): List[IWord] = {
    val word = getWord(wordStr)
    if (null != word) {
      if (wordStr.length > 1)
        word :: getWordList(wordStr.substring(0, wordStr.length - 1))
      else
        List[IWord](word)
    } else {
      if (wordStr.length > 1)
        getWordList(wordStr.substring(0, wordStr.length - 1))
      else
        List[IWord]()
    }
  }

  override def iterator(): List[IWord] = {
    allWords.toList.filter(_ != null)
  }
}

class TrieNode(key: Char, var index: Int) extends Comparable[TrieNode] {
  def this(index: Int) = this('\0', index)

  var descendant: Array[TrieNode] = new Array[TrieNode](0)

  def getKey = key
  def getIndex = index

  def search(array: Seq[Char]): TrieNode = {
    if (array.length == 1) {
      search(array.head)
    } else {
      val child = search(array.head)
      if (null != child) child.search(array.tail) else null
    }
  }

  def search(ch: Char): TrieNode = {
    val child: TrieNode = new TrieNode(ch, -1)
    val trieIndex = java.util.Arrays.binarySearch(descendant, child, TrieNode.comparator)
    if (trieIndex >= 0) {
      descendant(trieIndex)
    } else {
      null
    }
  }

  def insert(array: Seq[Char], index: Int) {
    if (array.length == 1) {
      findChildAndCreateIfNotExisted(array.head, index)
    } else {
      val child = findChildAndCreateIfNotExisted(array.head, -1)
      child.insert(array.tail, index)
    }
  }

  private def findChildAndCreateIfNotExisted(ch: Char, index: Int): TrieNode = {
    var child: TrieNode = new TrieNode(ch, index)
    val trieIndex = java.util.Arrays.binarySearch(descendant, child, TrieNode.comparator)
    if (trieIndex >= 0) {
      child = descendant(trieIndex)
      if (index >= 0) {
        child.index = index
      }
    } else {
      addTrieIndexNode(child)
    }
    child
  }

  private def addTrieIndexNode(child: TrieNode) {
    descendant = (descendant.toList :+ child).sorted.toArray
  }

  override def compareTo(other: TrieNode) = {
    key.compareTo(other.getKey)
  }
}

object TrieNode {
  val comparator = new java.util.Comparator[TrieNode] {
    override def compare(o1: TrieNode, o2: TrieNode): Int = o1.compareTo(o2)
  }
}
