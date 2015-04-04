package me.yingrui.segment.tools.ner

import me.yingrui.segment.neural._
import me.yingrui.segment.math.Matrix

object NameEntityRecognitionTest extends App {

  val network = NeuralNetwork()
  network.add(SigmoidLayer(Matrix(6, 6, Array(
    44.63561806120612  , -0.3057653274327397 , 4.254619405430898   , -0.25306486255097693 , -1.7870422744734777 , -2.7833889218535854,
    -2.6349496761437687, -0.8260100050289256 , 1.1071725153275125  , 20.324417048309282   , -3.5978288550420876 , -2.3945207569030735,
    -3.419404006223498 , -3.9437886333049366 , 1.7132749661981994  , -3.386745765192176   , 2.1369591225791726  , -21.308566934814053,
    3.0548383358270796 , -2.2197036754887605 , -4.913094986394171  , 3.5286413994323698   , 2.5309504760707506  , -24.184921569713552,
    -6.927940446777642 , -17.295036856260918 , 2.0252639206879115  , 4.2637059223927345   , -14.215721812677616 , 53.09777424537756  ,
    -1.1927688733868762, 7.480477922535939   , 1.0874207853104525  , 0.21123281033680458  , -21.191598089502524 , -23.247357903941612)),
    Matrix(Array(-35.57740211407981 , 12.81243173901548   , -7.4184731586959884 , -20.123205610679214  , 22.092774937167327  , -22.14872920047769 ))))

  network.add(SigmoidLayer(Matrix(6, 2, Array(
    -3.91207508391144  , 3.9120750839114375 ,
    -8.357193544412432 , 8.357193544412457  ,
    7.577453137171375  , -7.5774531371713785,
    -5.5137237211661345, 5.513723721166135  ,
    -4.315624585110355 , 4.315624585110359  ,
    -4.33076769290651  , 4.33076769290651   )),
    Matrix(Array(8.357440761465732  , -8.357440761465755))))

  var errorCount = 0
  var totalCount = 0

  val normalizer = new Normalizer(
    Array(-0.40833712865470995,-0.3687956385749418,-5.947379414241624,-14.519856665016288,-38.20810986099769,-13.210378045650687),
    Array(5.39923590055081,5.359694410471041,5.931437870372603,5.095529607265829,38.16063368541203,9.704275789273217))

  scala.io.Source.fromFile("src/test/resources/ner-train-set.dat").getLines.foreach { line: String =>
    val array = line.split(",").map(number => number.trim.toDouble)
    val input = normalizer.normalize(Matrix(array.slice(0, 6)))
    val ideal = if (array(6) > 0.0) Matrix(Array(1.0, 0.0)) else Matrix(Array(0.0, 1.0))
    val actual = network.computeOutput(input)

    val errorCalculator = new ErrorCalculator()
    errorCalculator.updateError(actual, ideal)
    if (errorCalculator.loss > 0.5) {
      errorCount += 1
    }
    totalCount += 1
  }

  println(errorCount)
  println(1 - errorCount.toDouble / totalCount.toDouble)
}
