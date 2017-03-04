package me.yingrui.segment.dict

trait IWordArray {

  def find(word: String): IWord

  def getWordItems(): Array[IWord]

  def add(word: IWord): Unit
}
