package me.yingrui.segment.performance

import org.junit.{Ignore, Assert, Test}

import me.yingrui.segment.core.SegmentWorker
import io.Source

class PerformanceTest {

  warmUp()

  def warmUp() {
    val reader = Source.fromFile(getClass().getClassLoader().getResource("Sophie's_World.txt").toURI, "UTF-8")
    val segmentWorker = SegmentWorker("recognize.partOfSpeech" -> "false")
    for (line <- reader.getLines()) {
      if (line.trim().length > 0) {
        segmentWorker.segment(line)
      }
    }
    reader.close()
  }

  @Test
  def should_segment_Sophies_World_within_2_seconds() {
    val reader = Source.fromFile(getClass().getClassLoader().getResource("Sophie's_World.txt").toURI, "UTF-8")
    val segmentWorker = SegmentWorker("recognize.partOfSpeech" -> "false", "load.englishdictionary" -> "false")
    segmentWorker.segment("世界您好！")
    val beginTime = System.currentTimeMillis()
    var total = 0
    for (line <- reader.getLines()) {
      if (line.trim().length > 0) {
        val result = segmentWorker.segment(line)
        total += result.length()
      }
    }
    reader.close()
    val endTime = System.currentTimeMillis()
    val milSeconds = endTime - beginTime
    println("should_segment_Sophies_World_within_2_seconds")
    println("    Spend total " + milSeconds + " ms.")
    println("    Segment words " + total + ", the velocity is " + (total * 1000 / milSeconds) + " num/sec.")
    Assert.assertTrue(milSeconds < 2000 * 1.05)
  }

  @Ignore
  @Test
  def should_segment_Sophies_World_with_POS_within_3000_milliseconds() {
    val reader = Source.fromFile(getClass().getClassLoader().getResource("Sophie's_World.txt").toURI, "UTF-8")
    val segmentWorker = SegmentWorker()
    segmentWorker.segment("世界您好！")
    val beginTime = System.currentTimeMillis()
    var total = 0
    for (line <- reader.getLines()) {
      if (line.trim().length > 0) {
        val result = segmentWorker.segment(line)
        total += result.length()
      }
    }
    reader.close()
    val endTime = System.currentTimeMillis()
    val milSeconds = endTime - beginTime
    println("should_segment_Sophies_World_with_POS_within_3000_milliseconds")
    println("    Spend total " + milSeconds + " ms.")
    println("    Segment words " + total + " with POS recognition, the velocity is " + (total * 1000 / milSeconds) + " num/sec.")
    Assert.assertTrue(milSeconds < 3000 * 1.05)
  }

  @Ignore
  @Test
  def should_segment_Sophies_World_with_POS_and_without_Domain_Dictionary_within_3000_milliseconds() {
    val reader = Source.fromFile(getClass().getClassLoader().getResource("Sophie's_World.txt").toURI, "UTF-8")
    val segmentWorker = SegmentWorker("load.domaindictionary" -> "true")
    segmentWorker.segment("世界您好！")
    val beginTime = System.currentTimeMillis()
    var total = 0
    for (line <- reader.getLines()) {
      if (line.trim().length > 0) {
        val result = segmentWorker.segment(line)
        total += result.length()
      }
    }
    reader.close()
    val endTime = System.currentTimeMillis()
    val milSeconds = endTime - beginTime
    println("should_segment_Sophies_World_with_POS_and_without_Domain_Dictionary_within_3_seconds")
    println("    Spend total " + milSeconds + " ms.")
    println("    Segment words " + total + " with POS recognition and without Domain dictionary, the velocity is " + (total * 1000 / milSeconds) + " num/sec.")
    Assert.assertTrue(milSeconds < 3000 * 1.05)
  }

  @Ignore
  @Test
  def should_segment_Sophies_World_with_POS_and_Context_within_3000_milliseconds() {
    val reader = Source.fromFile(getClass().getClassLoader().getResource("Sophie's_World.txt").toURI, "UTF-8")
    val segmentWorker = SegmentWorker("segment.context" -> "false", "recognize.partOfSpeech" -> "true")
    segmentWorker.segment("世界您好！")
    val beginTime = System.currentTimeMillis()
    var total = 0
    for (line <- reader.getLines()) {
      if (line.trim().length > 0) {
        val result = segmentWorker.segment(line)
        total += result.length()
      }
    }
    reader.close()
    val endTime = System.currentTimeMillis()
    val milSeconds = endTime - beginTime
    println("should_segment_Sophies_World_with_POS_and_Context_within_3_seconds")
    println("    Spend total " + milSeconds + " ms.")
    println("    Segment words " + total + " with POS recognition and context, the velocity is " + (total * 1000 / milSeconds) + " num/sec.")
    Assert.assertTrue(milSeconds < 3000 * 1.05)
  }

  @Test
  def should_spend_memory_within_95_MB() {
    val reader = Source.fromFile(getClass().getClassLoader().getResource("Sophie's_World.txt").toURI, "UTF-8")
    val segmentWorker = SegmentWorker("segment.context" -> "false", "recognize.partOfSpeech" -> "false")
    segmentWorker.segment("世界您好！")
    for (line <- reader.getLines()) {
      if (line.trim().length > 0) {
        segmentWorker.segment(line)
      }
    }
    reader.close()
    Runtime.getRuntime().gc()
    Thread.sleep(2000)
    val totalMemory = Runtime.getRuntime().totalMemory()
    val freeMemory = Runtime.getRuntime().freeMemory()
    val memorySize = (totalMemory - freeMemory) / 1024 / 1024
    println("should_spend_memory_within_95_MB")
    println("    Current application has taken " + memorySize + "MB memory size.")
    Assert.assertTrue(memorySize <= 95)
  }
}

