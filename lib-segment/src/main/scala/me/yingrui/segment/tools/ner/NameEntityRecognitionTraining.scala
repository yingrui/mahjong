package me.yingrui.segment.tools.ner

import me.yingrui.segment.neural.BackPropagation
import me.yingrui.segment.math.Matrix

object NameEntityRecognitionTraining extends App {
  val trainer = BackPropagation(5, 1, 0.1, 0.0)
  BackPropagation.debug = true

  scala.io.Source.fromFile("src/test/resources/ner-train-set.dat").getLines.foreach { line: String =>
    val array = line.split(",").map(number => number.trim.toDouble)
    val input = Matrix(array.slice(0, 5))
    val ideal = Matrix(array.slice(5, 6))
    trainer.addSample(input, ideal)
  }

  println("train set has been loaded.")

  trainer takeARound 3

  println("Precision rate: " + trainer.testWithTrainSet)
}
