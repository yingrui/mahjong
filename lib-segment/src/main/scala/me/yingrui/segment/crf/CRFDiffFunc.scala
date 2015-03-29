package me.yingrui.segment.crf

import java.util.concurrent.TimeUnit

import me.yingrui.segment.math.Matrix

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class CRFDiffFunc(corpus: CRFCorpus, model: CRFModel) extends Function {

  def valueAt(x: Matrix): Double = calculate(x)

  val derivative = Matrix(model.featuresCount, model.labelCount)
  val sigma = 1.0D
  val sigmaSq = sigma * sigma

  import scala.concurrent.ExecutionContext.Implicits.global

  private def calculate(weights: Matrix): Double = {
    val E = Matrix(model.featuresCount, model.labelCount)
    var prob = 0D

    var regular = 0D
    val regularFuture = Future {
      regular = Matrix.map(weights, x_i => x_i * x_i / 2 / sigmaSq).flatten.sum
      derivative := corpus.occurrence - Matrix.map(weights, x_i => x_i / sigmaSq)
    }

    corpus.grouped
      .map(docArray => Future {
          val calculator = new Calculator(docArray, model, weights)
          calculator.calculate
          calculator
        })
      .map(future => Await.result(future, Duration.apply(Long.MaxValue, TimeUnit.NANOSECONDS)))
      .foreach(cal => {
        prob += cal.prob
        E += cal.E
      })

    Await.result(regularFuture, Duration.apply(Long.MaxValue, TimeUnit.NANOSECONDS))
    derivative -= E
    regular - prob
  }

  class Calculator(docs: Array[CRFDocument], model: CRFModel, weights: Matrix) {
    val E = Matrix(model.featuresCount, model.labelCount)
    var prob = 0D

    def calculate: Unit = {
      for (doc_i <- docs) {
        val clique = CRFClique(doc_i, model, weights)

        for (t <- 0 until doc_i.data.length) {
          prob += clique.condLogProb(t, doc_i.label(t), Array[Int]())

          for (label <- 0 until model.labelCount) {
            val p = clique.condProb(t, label, Array[Int]())
            for (feature <- doc_i.data(t)) {
              E(feature, label) += p
            }
          }
        }
      }
    }
  }

}

