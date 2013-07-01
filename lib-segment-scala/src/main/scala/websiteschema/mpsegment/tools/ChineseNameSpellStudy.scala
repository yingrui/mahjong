import io.{Source}
import java.io.InputStream
import websiteschema.mpsegment.pinyin.WordToPinyinClassfierFactory
import websiteschema.mpsegment.util.FileUtil._

object ChineseNameSpellStudy extends App {
  val resourceCnNames: InputStream = getResourceAsStream("chinese_names_all.txt")
  for (str <- Source.fromInputStream(resourceCnNames).getLines()) {
    val namePattern = "([FM]) (.+)".r
    namePattern findFirstIn str match {
      case Some(namePattern(gender, name)) => {
        val pinyinList = WordToPinyinClassfierFactory().getClassifier().classify(name)
//        println(str + " " + pinyinList)
      }
      case None =>
    }
  }
}