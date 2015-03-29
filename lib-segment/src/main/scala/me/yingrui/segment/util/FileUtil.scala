package me.yingrui.segment.util

import java.io._

object FileUtil {

  def getResourceAsStream(resource: String): InputStream = {
    val file = new File(resource)
    if (file.exists()) {
      new FileInputStream(file)
    } else {
      val inputStream = getClass().getClassLoader().getResourceAsStream(resource)
      if (null != inputStream) {
        inputStream
      } else {
        getClass().getClassLoader().getResourceAsStream("me/yingrui/segment/" + resource)
      }
    }

  }
}
