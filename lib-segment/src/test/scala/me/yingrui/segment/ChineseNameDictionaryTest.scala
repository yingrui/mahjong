package me.yingrui.segment

import java.io.File

import org.junit.Assert
import org.junit.Test
import me.yingrui.segment.dict.ChNameDictionary

class ChineseNameDictionaryTest {

  @Test
  def should_load_Chinese_name_dictionary_correctly() {
    val dict = new ChNameDictionary()
    dict.loadNameDict("ChName.dict")
    testChineseName(dict, "张学友")
  }

  @Test
  def should_save_Chinese_name_dictionary_correctly() {
    var dict = new ChNameDictionary()
    dict.loadNameDict("ChName.dict")

    dict.saveNameDict("ChName2.dict")
    val f = new File("ChName2.dict")
    assert(f.exists())
    f.deleteOnExit()

    dict = new ChNameDictionary()
    dict.loadNameDict("ChName2.dict")
    testChineseName(dict, "张学友")
  }

  private def testChineseName(dict: ChNameDictionary, name: String) {
    print(name + " ")
    if (dict.getFuXing().containsKey(name.substring(0, 2))) {
      Assert.fail()
    }
    var xing: String = null
    var ming1: String = null
    var ming2 = ""
    xing = name.substring(0, 1)
    ming1 = name.substring(1, 2)
    ming2 = name.substring(2, 3)
    Assert.assertEquals((new StringBuilder()).append(dict.computeLgLP3(xing, ming1, ming2)).toString(), "2.760293536")
    Assert.assertEquals((new StringBuilder()).append(dict.computeLgLP2(ming1, ming2)).toString(), "0.285352")
    println((new StringBuilder(String.valueOf(dict.computeLgLP3(xing, ming1, ming2)))).append("  ").append(dict.computeLgLP2(ming1, ming2)).toString())
  }
}
