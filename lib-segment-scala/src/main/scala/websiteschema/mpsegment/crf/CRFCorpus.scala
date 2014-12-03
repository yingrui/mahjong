package websiteschema.mpsegment.crf

import scala.io.Source
import scala.collection.mutable._

case class CRFDocument(val data: Array[Array[Int]], val label: Array[Int])

class CRFCorpus(val docs: Array[CRFDocument], featuresCount: Int, labelCount: Int) {

  val Ehat: Array[Array[Double]] = {
    val featureArray = CRFUtils.empty2DArray(featuresCount, labelCount)

    for(doc_i <- docs) {
      for(t <- 0 until doc_i.data.length; k <- doc_i.data(t)) {
        featureArray(k)(doc_i.label(t)) += 1.0D
      }
    }

    featureArray
  }

}

object CRFCorpus {

  def apply(file: String): CRFCorpus = {
    val featureRepository = Map[String, Int]()
    val labelRepository = Map[String, Int]()

    val rowData = ListBuffer[String]()
    Source.fromFile(file).getLines().foreach(line => {
      if(line.trim().isEmpty) {
        val doc = createDocument(rowData)
        rowData.clear()
      } else {
        rowData += line
      }
    })

    throw new RuntimeException
  }

  private def createDocument(data: ListBuffer[String]) = {

  }
}
