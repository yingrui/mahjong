package websiteschema.mpsegment.util

object Logger {

  val containsLogger = false

  val consoleOutput = false

  def debug(logger: String, message: String) {
    if (containsLogger) {
      // print to logger
    } else if (consoleOutput) {
      println(message)
    }

  }

  def debug(message: String) {
    debug("", message)
  }
}
