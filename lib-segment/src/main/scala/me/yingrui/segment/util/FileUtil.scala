package me.yingrui.segment.util

import java.io._

object FileUtil {

  def getResourceAsStream(resource: String): InputStream = {
    val file = new File(resource)
    if (file.exists()) {
      return new FileInputStream(file)
    }

    val inputStream = getClass().getClassLoader().getResourceAsStream(resource)
    if (null != inputStream) {
      return inputStream
    }

    val internalResource = getClass().getClassLoader().getResourceAsStream("me/yingrui/segment/" + resource)
    if (internalResource != null) {
      return internalResource
    }

    return getClass().getClassLoader().getParent().getResourceAsStream(resource)
  }
}
