package me.yingrui.segment.neural

import me.yingrui.segment.math.Matrix

import scala.io.Source

trait WisconsinBreastCancerDataSet {

  val trainDataSet = loadData("train")
  val testDataSet = loadData("test")

  private def loadData(dataSet: String) = {
    val classLoader = getClass.getClassLoader()
    val inputSource = Source.fromURL(classLoader.getResource(s"wisconsin-breast-cancer/${dataSet}X.txt"))
    val outputSource = Source.fromURL(classLoader.getResource(s"wisconsin-breast-cancer/${dataSet}Y.txt"))

    val inputs = inputSource.getLines().map(line => Matrix(line.split("\\s+").map(_.toDouble))).toArray

    val outputs = outputSource.getLines().map(result =>
      if(result.toDouble > 0.0D)
        Matrix(Array(0.0D, 1.0D))
      else
        Matrix(Array(1.0D, 0.0D))
    ).toArray

    (0 until inputs.length).map(i => (inputs(i), outputs(i)))
  }

}
