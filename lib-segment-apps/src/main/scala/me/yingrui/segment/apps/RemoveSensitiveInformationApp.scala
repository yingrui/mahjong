package me.yingrui.segment.apps

import java.io.{FileOutputStream, OutputStreamWriter, PrintWriter}

import me.yingrui.segment.conf.SegmentConfiguration
import me.yingrui.segment.core.SegmentWorker
import me.yingrui.segment.dict.POSUtil

import scala.collection.mutable
import scala.io.Source
import scala.util.parsing.json.{JSON, JSONObject}

object RemoveSensitiveInformationApp extends App {

  if (args.isEmpty) {
    printHelp()
  }


  val inputFile = if (args.indexOf("--input-file") >= 0) args(args.indexOf("--input-file") + 1) else ""
  val outputFile = if (args.indexOf("--output-file") >= 0) args(args.indexOf("--output-file") + 1) else ""
  val fields = if (args.indexOf("--fields") >= 0) args(args.indexOf("--fields") + 1).split(",").toList else List()
  val removeFields = if (args.indexOf("--remove-fields") >= 0) args(args.indexOf("--remove-fields") + 1).split(",").toList else List()
  val removePos = if (args.indexOf("--remove-pos") >= 0) args(args.indexOf("--remove-pos") + 1).toLowerCase.split(",").toList else List()
  val removePatterns = if (args.indexOf("--remove-regex") >= 0) args(args.indexOf("--remove-regex") + 1).toLowerCase.split(",").toList else List()
  if (inputFile.isEmpty || outputFile.isEmpty || fields.isEmpty || removePos.isEmpty) {
    printHelp()
  }

  val config = SegmentConfiguration(Map("separate.xingming" -> "true", "minimize.word" -> "true"))
  val segmentWorker = SegmentWorker(config)

  val writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf-8"))

  for (line <- Source.fromFile(inputFile).getLines()) {
    JSON.parseRaw(line) match {
      case Some(json) => process(json.asInstanceOf[JSONObject])
      case _ => println()
    }
  }
  writer.println()
  writer.flush()
  writer.close()

  private def process(json: JSONObject): Unit = {
    val obj = mutable.Map(json.obj.toSeq: _*)
    for (field <- fields) {
      val fieldValue: String = obj.getOrElse(field, "").asInstanceOf[String]
      if (!fieldValue.isEmpty) {
        val segmentResult = segmentWorker.segment(fieldValue)
        for (i <- 0 until segmentResult.length()) {
          val str = POSUtil.getPOSString(segmentResult.getPOS(i)).toLowerCase
          if (removePos.contains(str)) {
            segmentResult.setWord(i, segmentResult.getWord(i).replaceAll(".", "*"))
          }
        }
        var result = segmentResult.toOriginalString()
        for (regex <- removePatterns) {
          result = result.replaceAll(regex, "**********")
        }

        obj.put(field, result)
      }
    }

    for (field <- removeFields) {
      obj.remove(field)
    }
    writer.println(JSONObject(Map(obj.toSeq: _*)).toString())
  }

  private def printHelp() = {
    println(
      """
        |Usage:
        | --input-file    : input json file which contains sensitive information
        | --output-file   : output file
        | --fields        : which fields contains sensitive information
        | --remove-pos    : what kind of words should be replaced with *
        | --remove-regex  : the words in specified if they are matched would be replaced with *
        | --remove-fields : the fields would be removed
      """.
        stripMargin)
    System.exit(0)
  }

}
