package me.yingrui.segment.dict

trait IDictionary {

  def getWord(wordStr: String): IWord

  def getWords(wordStr: String): Array[IWord]

  def iterator(): List[IWord]

  def addWord(word: IWord): Unit

  def lookupWord(wordStr: String): IWord
}
