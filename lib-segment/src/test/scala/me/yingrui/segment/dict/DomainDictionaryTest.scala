package me.yingrui.segment.dict



import domain.DomainDictFactory
import org.junit.Assert
import org.junit.Test

class DomainDictionaryTest {

  @Test
  def should_Loaded_Some_Synonyms_Like_PC() {
    try {
      val dictFactory = new DomainDictFactory()
      dictFactory.buildDictionary()

      var synonymSet = dictFactory.getDomainDictionary().getSynonymSet("个人电脑")
      Assert.assertEquals(synonymSet(0).getWordName(), "PC机")
      Assert.assertEquals(synonymSet(1).getWordName(), "个人电脑")

      synonymSet = dictFactory.getDomainDictionary().getSynonymSet("PC机")
      Assert.assertEquals(synonymSet(0).getWordName(), "PC机")
      Assert.assertEquals(synonymSet(1).getWordName(), "个人电脑")
    } catch {
      case ex: Throwable =>
        Assert.fail()
    }
  }
}
