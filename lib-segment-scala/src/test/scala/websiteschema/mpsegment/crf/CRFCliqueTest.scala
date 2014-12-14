package websiteschema.mpsegment.crf

import org.junit.Test
import websiteschema.mpsegment.Assertion._

class CRFCliqueTest {

  @Test
  def should_calculate_log_prob_when_weights_are_zero {
    val crfDoc = new CRFDocument(Array(Array(0, 1, 2)), Array(0))
    val factors = Array(new Factor(Array(0D, 0D, 0D)))
    val clique = new CRFClique(crfDoc, 3, factors)

    shouldBeEqual(-1.0986122886681098D, clique.condLogProb(0, 0, Array[Int]()))
  }

  @Test
  def should_calculate_prob_when_weights_are_zero {
    val crfDoc = new CRFDocument(Array(Array(0, 1, 2)), Array(0))
    val factors = Array(new Factor(Array(0D, 0D, 0D)))
    val clique = new CRFClique(crfDoc, 3, factors)

    shouldBeEqual(0.333333333D, clique.condProb(0, 0, Array[Int]()))
  }

  @Test
  def should_calculate_prob_when_weights_ {
    val crfDoc = new CRFDocument(Array(Array(0, 1, 2)), Array(0))
    val factors = Array(new Factor(Array(10D, 30D, 51D)))
    val clique = new CRFClique(crfDoc, 3, factors)

    println(clique.condProb(0, 0, Array[Int]()))
    println(clique.condProb(0, 1, Array[Int]()))
    println(clique.condProb(0, 2, Array[Int]()))
  }

}
