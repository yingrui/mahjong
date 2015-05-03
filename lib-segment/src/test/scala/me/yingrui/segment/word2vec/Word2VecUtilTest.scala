package me.yingrui.segment.word2vec

import java.lang.Math.abs

import me.yingrui.segment.neural.Sigmoid
import me.yingrui.segment.word2vec.Word2VecUtil._
import org.scalatest.{FunSuite, Matchers}

import scala.util.Random

class Word2VecUtilTest extends FunSuite with Matchers {

  test("simplify sigmoid activation") {
    val sigmoid = Sigmoid()
    for(i <- -6 until 6) {
      abs(simplifiedSigmoid(i.toDouble) - sigmoid.activate(i.toDouble)) shouldBe < (1E-2)
    }
  }

  test("return 1D when input exceed MAX_EXP 6") {
    val doubleGenerator = new Random();
    for (i <- 1 to 100) {
      simplifiedSigmoid(6D + doubleGenerator.nextDouble() * 100D) should be (1D)
    }
  }

  test("return 0D when input less than -6") {
    val doubleGenerator = new Random();
    for (i <- 1 to 100) {
      simplifiedSigmoid(-6D - doubleGenerator.nextDouble() * 100D) should be (0D)
    }
  }

}
