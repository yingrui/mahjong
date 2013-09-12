package websiteschema.mpsegment.tools.ner

import websiteschema.mpsegment.neural._
import websiteschema.mpsegment.math.Matrix

object NameEntityRecognitionTest extends App {

  val network = NeuralNetwork()
  network.add(new Layer(Matrix(6, 5, Array(
    -4.141400761,0.018112528,-57.186162,80.44327663,-2.094675018,
    -2.531920506,0.227382487,1.027638534,-2.531330319,15.80827461,
    -7.519869936,-18.39169888,-2.346881748,-3.746937762,3.780663397,
    -1.015157254,-0.924787109,-5.680682438,-1.666862523,-7.661014172,
    -6.259515859,3.547185296,-0.636875322,-6.503246733,-4.306727202,
    10.65882014,14.07042171,36.07247241,-74.1869029,-16.74318699)),
    Sigmoid.activation))

  network.add(new Layer(Matrix(6, 2, Array(
    -6.63381223,6.63381223,
    -5.012873814,5.012873814,
    -1.872245589,1.872245589,
    -3.493907698,3.493907698,
    -4.878146907,4.878146907,
    8.472605283,-8.472605283
  )), Sigmoid.activation))

  var errorCount = 0
  var totalCount = 0

  val normalizer = new Normalizer(
    Array(-0.40833712865470995,-0.3687956385749418,-38.20810986099769,6.345953415455609,-13.710378045650685),
    Array(5.39923590055081,5.359694410471041,38.16063368541203,12.051582045835806,10.204275789273215))

  scala.io.Source.fromFile("src/test/resources/ner-train-set.dat").getLines.foreach { line: String =>
    val array = line.split(",").map(number => number.trim.toDouble)
    val input = normalizer.normalize(Matrix(array.slice(0, 5)))
    val ideal = if (array(5) > 0.0) Matrix(Array(1.0, 0.0)) else Matrix(Array(0.0, 1.0))
    val actual = network.computeOutput(input)

    val errorCalculator = new ErrorCalculator()
    errorCalculator.updateError(actual.flatten, ideal.flatten)
    if (errorCalculator.getRootMeanSquare > 0.5) {
      errorCount += 1
    }
    totalCount += 1
  }

  println(errorCount)
  println(1 - errorCount.toDouble / totalCount.toDouble)
}
