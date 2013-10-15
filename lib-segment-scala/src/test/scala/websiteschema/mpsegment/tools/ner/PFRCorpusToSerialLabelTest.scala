package websiteschema.mpsegment.tools.ner

import org.junit.Assert
import org.junit.Test

import java.io.ByteArrayInputStream
import java.io.InputStream
import websiteschema.mpsegment.tools.PFRCorpusLoader
import websiteschema.mpsegment.core.WordAtom
import websiteschema.mpsegment.tools.accurary.{SegmentResultComparator, SegmentResultCompareHook}

class PFRCorpusToSerialLabelTest {

  @Test
  def should_label_chinese_family_name_as_b() {
    val expect = convertToSegmentResult("19980101-01-003-002/m  江/nr  泽民/nr  李/nr  鹏/nr  乔/nr  石/nr  朱/nr  镕基/nr  李/nr  瑞环/nr  刘/nr  华清/nr  尉/nr  健行/nr  李/nr  岚清/nr")
    val actual = convertToSegmentResult("19980101-01-003-002/m  江/nr  泽/nr 民/nr  李/nr  鹏/nr  乔/nr  石/nr  朱/nr  镕/nr 基/nr  李/nr  瑞/nr 环/nr  刘/nr  华/nr 清/nr  尉/nr  健/nr 行李/n  岚/nr 清/nr")

    val hooker = new PRFCorpusToSerialLabelCompareHooker(expect, actual)
    val comparator = new SegmentResultComparator(hooker)
    comparator.compare(expect, actual)

    Assert.assertEquals("B", hooker.serialLabels(0)._2)
    Assert.assertEquals("B", hooker.serialLabels(3)._2)
    Assert.assertEquals("B", hooker.serialLabels(5)._2)
    Assert.assertEquals("B", hooker.serialLabels(7)._2)
    Assert.assertEquals("B", hooker.serialLabels(10)._2)
    Assert.assertEquals("B", hooker.serialLabels(13)._2)
    Assert.assertEquals("B", hooker.serialLabels(16)._2)
  }

  @Test
  def should_label_chinese_single_last_name_as_e() {
    val expect = convertToSegmentResult("19980101-01-003-002/m  江/nr  泽民/nr  李/nr  鹏/nr  乔/nr  石/nr  朱/nr  镕基/nr  李/nr  瑞环/nr  刘/nr  华清/nr  尉/nr  健行/nr  李/nr  岚清/nr")
    val actual = convertToSegmentResult("19980101-01-003-002/m  江/nr  泽/nr 民/nr  李/nr  鹏/nr  乔/nr  石/nr  朱/nr  镕/nr 基/nr  李/nr  瑞/nr 环/nr  刘/nr  华/nr 清/nr  尉/nr  健/nr 行李/n  岚/nr 清/nr")

    val hooker = new PRFCorpusToSerialLabelCompareHooker(expect, actual)
    val comparator = new SegmentResultComparator(hooker)
    comparator.compare(expect, actual)

    Assert.assertEquals("E", hooker.serialLabels(4)._2)
    Assert.assertEquals("E", hooker.serialLabels(6)._2)
  }

  @Test
  def should_label_chinese_first_name_as_c() {
    val expect = convertToSegmentResult("19980101-01-003-002/m  江/nr  泽民/nr  李/nr  鹏/nr  乔/nr  石/nr  朱/nr  镕基/nr  李/nr  瑞环/nr  刘/nr  华清/nr  尉/nr  健行/nr  李/nr  岚清/nr")
    val actual = convertToSegmentResult("19980101-01-003-002/m  江/nr  泽/nr 民/nr  李/nr  鹏/nr  乔/nr  石/nr  朱/nr  镕/nr 基/nr  李/nr  瑞/nr 环/nr  刘/nr  华/nr 清/nr  尉/nr  健/nr 行李/n  岚/nr 清/nr")

    val hooker = new PRFCorpusToSerialLabelCompareHooker(expect, actual)
    val comparator = new SegmentResultComparator(hooker)
    comparator.compare(expect, actual)

    Assert.assertEquals("C", hooker.serialLabels(1)._2)
    Assert.assertEquals("C", hooker.serialLabels(8)._2)
    Assert.assertEquals("C", hooker.serialLabels(11)._2)
    Assert.assertEquals("C", hooker.serialLabels(14)._2)
    Assert.assertEquals("C", hooker.serialLabels(17)._2)
    Assert.assertEquals("C", hooker.serialLabels(19)._2)
  }

  @Test
  def should_label_chinese_last_name_as_d() {
    val expect = convertToSegmentResult("19980101-01-003-002/m  江/nr  泽民/nr  李/nr  鹏/nr  乔/nr  石/nr  朱/nr  镕基/nr  李/nr  瑞环/nr  刘/nr  华清/nr  尉/nr  健行/nr  李/nr  岚清/nr")
    val actual = convertToSegmentResult("19980101-01-003-002/m  江/nr  泽/nr 民/nr  李/nr  鹏/nr  乔/nr  石/nr  朱/nr  镕/nr 基/nr  李/nr  瑞/nr 环/nr  刘/nr  华/nr 清/nr  尉/nr  健/nr 行李/n  岚/nr 清/nr")

    val hooker = new PRFCorpusToSerialLabelCompareHooker(expect, actual)
    val comparator = new SegmentResultComparator(hooker)
    comparator.compare(expect, actual)

    Assert.assertEquals("D", hooker.serialLabels(2)._2)
    Assert.assertEquals("D", hooker.serialLabels(9)._2)
    Assert.assertEquals("D", hooker.serialLabels(12)._2)
    Assert.assertEquals("D", hooker.serialLabels(15)._2)
    Assert.assertEquals("D", hooker.serialLabels(20)._2)
  }

  @Test
  def should_label_error_as_u_which_last_word_overlap_with_xing() {
    val expect = convertToSegmentResult("19980101-01-003-002/m  江/nr  泽民/nr  李/nr  鹏/nr  乔/nr  石/nr  朱/nr  镕基/nr  李/nr  瑞环/nr  刘/nr  华清/nr  尉/nr  健行/nr  李/nr  岚清/nr")
    val actual = convertToSegmentResult("19980101-01-003-002/m  江/nr  泽/nr 民/nr  李/nr  鹏/nr  乔/nr  石/nr  朱/nr  镕/nr 基/nr  李/nr  瑞/nr 环/nr  刘/nr  华/nr 清/nr  尉/nr  健/nr 行李/n  岚/nr 清/nr")

    val hooker = new PRFCorpusToSerialLabelCompareHooker(expect, actual)
    val comparator = new SegmentResultComparator(hooker)
    comparator.compare(expect, actual)

    Assert.assertEquals("U", hooker.serialLabels(18)._2)
  }

  @Test
  def should_label_xing_prefix_as_f() {
    val expect = convertToSegmentResult("19980107-09-003-005/m  老刘/nr 小李/nr 如此/r  境遇/n  ，/w ")
    val actual = convertToSegmentResult("19980107-09-003-005/m  老/a 刘/nr 小/a 李/nr 如此/r  境遇/n  ，/w")

    val hooker = new PRFCorpusToSerialLabelCompareHooker(expect, actual)
    val comparator = new SegmentResultComparator(hooker)
    comparator.compare(expect, actual)

    Assert.assertEquals("F", hooker.serialLabels(0)._2)
    Assert.assertEquals("B", hooker.serialLabels(1)._2)
    Assert.assertEquals("F", hooker.serialLabels(2)._2)
    Assert.assertEquals("B", hooker.serialLabels(3)._2)
  }

  @Test
  def should_label_xing_suffix_as_g() {
    val expect = convertToSegmentResult("19980107-09-003-005/m  刘总/nr 张老/nr 如此/r  境遇/n  ，/w ")
    val actual = convertToSegmentResult("19980107-09-003-005/m  刘/nr 总/n 张/nr 老/a 如此/r  境遇/n  ，/w ")

    val hooker = new PRFCorpusToSerialLabelCompareHooker(expect, actual)
    val comparator = new SegmentResultComparator(hooker)
    comparator.compare(expect, actual)

    Assert.assertEquals("B", hooker.serialLabels(0)._2)
    Assert.assertEquals("G", hooker.serialLabels(1)._2)
    Assert.assertEquals("B", hooker.serialLabels(2)._2)
    Assert.assertEquals("G", hooker.serialLabels(3)._2)
  }

  @Test
  def should_label_left_boundary_as_k() {
    val expect = convertToSegmentResult("19980106-12-001-012/m  老/a  队长/n  丁/nr  安庆/nr  说/v")
    val actual = convertToSegmentResult("19980106-12-001-012/m  老/a  队长/n  丁/nr  安庆/nr  说/v")

    val hooker = new PRFCorpusToSerialLabelCompareHooker(expect, actual)
    val comparator = new SegmentResultComparator(hooker)
    comparator.compare(expect, actual)

    Assert.assertEquals("K", hooker.serialLabels(1)._2)
    Assert.assertEquals("B", hooker.serialLabels(2)._2)
  }

  private def convertToSegmentResult(text: String) = PFRCorpusLoader(convertToInputStream(text)).readLine()

  private def convertToInputStream(text: String): InputStream = new ByteArrayInputStream(text.getBytes("utf-8"))
}
