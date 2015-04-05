package me.yingrui.segment.util

object Logger {

  private val containsLogger = false
  private var consoleOutput = false

  def enableConsoleOutput {
    consoleOutput = true
  }

  def disableConsoleOutput {
    consoleOutput = false
  }

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
