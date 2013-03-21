package websiteschema.mpsegment.dict

;

import domain.DomainDictFactory
import org.junit.Assert;
import org.junit.Test;

class DomainDictionaryTest {

  DomainDictFactory().buildDictionary()

  @Test
  def should_Loaded_Some_Synonyms_Like_PC() {
    try {
      var synonymSet = DomainDictFactory().getDomainDictionary().getSynonymSet("个人电脑")
      Assert.assertEquals(synonymSet(0).getWordName(), "PC机");
      Assert.assertEquals(synonymSet(1).getWordName(), "个人电脑");

      synonymSet = DomainDictFactory().getDomainDictionary().getSynonymSet("PC机");
      Assert.assertEquals(synonymSet(0).getWordName(), "PC机");
      Assert.assertEquals(synonymSet(1).getWordName(), "个人电脑");
    } catch {
      case ex: Throwable =>
        Assert.fail();
    }
  }
}
