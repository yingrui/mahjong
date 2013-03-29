package websiteschema.mpsegment.performance

import org.junit.{Assert, Test}

import websiteschema.mpsegment.core.SegmentEngine
import io.Source

class PerformanceTest {

  warmUp()

  def warmUp() {
    val reader = Source.fromFile(getClass().getClassLoader().getResource("Sophie's_World.txt").toURI, "UTF-8")
    val segmentWorker = SegmentEngine().getSegmentWorker()
    segmentWorker.setRecognizePOS(true)
    var total = 0
    for (line <- reader.getLines()) {
      if (line.trim().length > 0) {
        val result = segmentWorker.segment(line)
        total += result.length()
      }
    }
    reader.close()
  }

  @Test
  def should_segment_Sophies_World_within_3_seconds() {
    val reader = Source.fromFile(getClass().getClassLoader().getResource("Sophie's_World.txt").toURI, "UTF-8")
    val segmentWorker = SegmentEngine().getSegmentWorker()
    segmentWorker.setRecognizePOS(false)
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
    Assert.assertTrue(milSeconds < 3000)
  }

  @Test
  def should_segment_Sophies_World_with_POS_within_3_seconds() {
    val reader = Source.fromFile(getClass().getClassLoader().getResource("Sophie's_World.txt").toURI, "UTF-8")
    val segmentWorker = SegmentEngine().getSegmentWorker()
    segmentWorker.setRecognizePOS(true)
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
    println("should_segment_Sophies_World_with_POS_within_3_seconds")
    println("    Spend total " + milSeconds + " ms.")
    println("    Segment words " + total + " with POS recognition, the velocity is " + (total * 1000 / milSeconds) + " num/sec.")
    Assert.assertTrue(milSeconds < 3000)
  }

  @Test
  def should_segment_Sophies_World_with_POS_and_without_Domain_Dictionary_within_3_seconds() {
    val reader = Source.fromFile(getClass().getClassLoader().getResource("Sophie's_World.txt").toURI, "UTF-8")
    val segmentWorker = SegmentEngine().getSegmentWorker()
    segmentWorker.setRecognizePOS(true)
    segmentWorker.setUseDomainDictionary(false)
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
    segmentWorker.setUseDomainDictionary(true)
    val endTime = System.currentTimeMillis()
    val milSeconds = endTime - beginTime
    println("should_segment_Sophies_World_with_POS_and_without_Domain_Dictionary_within_3_seconds")
    println("    Spend total " + milSeconds + " ms.")
    println("    Segment words " + total + " with POS recognition and without Domain dictionary, the velocity is " + (total * 1000 / milSeconds) + " num/sec.")
    Assert.assertTrue(milSeconds < 3000)
  }

  @Test
  def should_segment_Sophies_World_with_POS_and_Context_within_3_seconds() {
    val reader = Source.fromFile(getClass().getClassLoader().getResource("Sophie's_World.txt").toURI, "UTF-8")
    val segmentWorker = SegmentEngine().getSegmentWorker()
    segmentWorker.setRecognizePOS(true)
    segmentWorker.setUseContextFreqSegment(true)
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
    segmentWorker.setUseContextFreqSegment(false)
    val endTime = System.currentTimeMillis()
    val milSeconds = endTime - beginTime
    println("should_segment_Sophies_World_with_POS_and_Context_within_3_seconds")
    println("    Spend total " + milSeconds + " ms.")
    println("    Segment words " + total + " with POS recognition and context, the velocity is " + (total * 1000 / milSeconds) + " num/sec.")
    Assert.assertTrue(milSeconds < 4000)
  }

  @Test
  def should_spend_memory_within_25_MB() {
    val reader = Source.fromFile(getClass().getClassLoader().getResource("Sophie's_World.txt").toURI, "UTF-8")
    val segmentWorker = SegmentEngine().getSegmentWorker()
    segmentWorker.setRecognizePOS(true)
    segmentWorker.setUseContextFreqSegment(true)
    segmentWorker.segment("世界您好！")
    for (line <- reader.getLines()) {
      if (line.trim().length > 0) {
        segmentWorker.segment(line)
      }
    }
    reader.close()
    segmentWorker.setUseContextFreqSegment(false)
    Runtime.getRuntime().gc()
    Thread.sleep(2000)
    val totalMemory = Runtime.getRuntime().totalMemory()
    val freeMemory = Runtime.getRuntime().freeMemory()
    val memorySize = (totalMemory - freeMemory) / 1024 / 1024
    println("should_spend_memory_within_25_MB")
    println("    Current application has taken " + memorySize + "MB memory size.")
    Assert.assertTrue(memorySize < 25)
  }
}

