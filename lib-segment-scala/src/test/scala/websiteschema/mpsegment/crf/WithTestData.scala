package websiteschema.mpsegment.crf

import java.io.FileWriter

trait WithTestData {
  // data = int[X][Features]
  val data = Array(
    Array(7, 10, 0, 12, 14, 15, 16, 17, 2, 19, 3, 21, 23, 24, 25),
    Array(8, 9, 11, 13, 15, 1, 17, 18, 20, 21, 4, 22, 5, 24, 6, 27)
  )

  val testData = Array(
    Array(7, 10, 0, 12, 14, 15, 16, 17, 2, 19, 3, 21, 23, 24, 25),
    Array(8, 9, 11, 13, 15, 1, 17, 18, 20, 21, 4, 22, 5, 24, 6)
  )

  val featureRepository = new FeatureRepository(true)
  for(feature <- 0 until 29) {
    featureRepository.add(feature.toString)
  }

  val labelRepository = new FeatureRepository(false)
  labelRepository.add("0")
  labelRepository.add("1")

  val doc = new CRFDocument(data, Array[Int](0, 1))

  def populateTrainingFile(text: String) = {
    val file = java.io.File.createTempFile("crf-corpus", "txt")
    file.deleteOnExit()
    val output = new FileWriter(file)
    output.write(text)
    output.close()
    file.getAbsolutePath
  }
}
