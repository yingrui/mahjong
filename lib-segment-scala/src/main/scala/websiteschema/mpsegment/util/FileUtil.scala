package websiteschema.mpsegment.util

import java.io._

object FileUtil {

  def getResourceAsStream(resource: String): InputStream = {
    getClass().getClassLoader().getResourceAsStream("websiteschema/mpsegment/" + resource)
  }
}
