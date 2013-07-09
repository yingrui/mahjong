import io.{Source}
import java.io.InputStream
import scala.collection.mutable.Map
import websiteschema.mpsegment.pinyin.WordToPinyinClassfierFactory
import websiteschema.mpsegment.util.FileUtil._

object ChineseNameSpellStudy extends App {
  val pinyinFreq = Map[String,Int]();
  val namePattern = "([FM]) (.+)".r

  val resourceCnNames: InputStream = getResourceAsStream("chinese_names_all.txt")
  Source.fromInputStream(resourceCnNames).getLines().foreach(str => {
    namePattern findFirstIn str match {
      case Some(namePattern(gender, name)) => statistic(name, gender)
      case None =>
    }
  })

  println(pinyinFreq)

  def statistic(name: String, gender: String) {
    val pinyinList = WordToPinyinClassfierFactory().getClassifier().classify(name)
    pinyinList.foreach(pinyin => {
      if(pinyin.matches("[a-z1-9]+")) {
        pinyinFreq(pinyin) = pinyinFreq.getOrElse(pinyin, 0) + 1
      }
    });
  }
}