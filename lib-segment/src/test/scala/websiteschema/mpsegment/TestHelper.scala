package websiteschema.mpsegment

import java.io.FileWriter

object TestHelper {

  def createTempFile(text: String) = {
    val file = java.io.File.createTempFile("test", ".txt")
    file.deleteOnExit()
    val output = new FileWriter(file)
    output.write(text)
    output.close()
    file.getAbsolutePath
  }

}
