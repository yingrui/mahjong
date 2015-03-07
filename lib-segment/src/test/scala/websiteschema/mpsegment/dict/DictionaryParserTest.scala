package websiteschema.mpsegment.dict

import org.junit.Test
import scala.util.parsing.json.JSON

object Parser {

  def parse(json: String) = JSON.parseFull(json)
}

class DictionaryParserTest {

  @Test
  def should_parse_array {
    println(Parser.parse("""{"a":1}"""))
  }

}
