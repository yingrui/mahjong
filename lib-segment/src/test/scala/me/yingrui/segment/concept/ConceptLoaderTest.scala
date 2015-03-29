package me.yingrui.segment.concept

import org.junit.Assert
import org.junit.Test

import java.io.ByteArrayInputStream

class ConceptLoaderTest {

  val text = "1 名词 n-noun\n" +
    "1.1 事物 n-object\n" +
    "1.1.1 具体事物 n-entity\n" +
    "1.1.1.1 生物 n-creature\n" +
    "1.4 时间 n-time\n" +
    "1.4.1 绝对时间 n-absolute-time\n" +
    "1.4.10 相对时间 n-relative-time\n" +
    "1.1.1.2.1.14.3 武器 n-weapon"

  var conceptTree: ConceptTree = null

  try {
    val inputStream = new ByteArrayInputStream(text.getBytes("utf-8"))
    val loader = ConceptLoader(inputStream)
    conceptTree = loader.getConceptTree()
  } catch {
    case ex: Exception =>
      ex.printStackTrace()
  }

  @Test
  def should_load_concept_tree_correctly() {
    val root = conceptTree.getRootConcept()
    Assert.assertEquals(1, root.getId())

    val children = root.getChildren()
    Assert.assertEquals(3, children.size)
    Assert.assertEquals("n-object", children.last.getName())
    Assert.assertEquals(101, children.last.getId())
    Assert.assertEquals("n-time", children(1).getName())

    val grandChildren = root.getChildren().last.getChildren()
    Assert.assertEquals(1, grandChildren.size)
    Assert.assertEquals(10101, grandChildren.last.getId())
    Assert.assertEquals("n-entity", grandChildren.last.getName())
  }
}
