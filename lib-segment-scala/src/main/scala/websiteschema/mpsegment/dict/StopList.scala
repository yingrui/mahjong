package websiteschema.mpsegment.dict

import websiteschema.mpsegment.conf.MPSegmentConfiguration

import io.Source
import collection.mutable

object StopList {

  val posStops = new Array[Int](20);
  val stopWordHashMap = mutable.HashMap[String, Int]();
  var numPosStop = 0;

  def loadStopList(s: String) {
    val encoding = MPSegmentConfiguration().getDefaultFileEncoding();
    try {
      var i = 0;

      val uri = getClass.getClassLoader().getResource(s).toURI
      val source = Source.fromFile(uri, encoding)
      for (str <- source.getLines; line = str.trim()) {
        i += 1;
        if (line.length() >= 1 && !line.startsWith("#")) {
          if (!line.equalsIgnoreCase("\\space")) {
            stopWordHashMap += (line -> 2);
          } else {
            stopWordHashMap += (" " -> 2);
          }
        }
      }
      source.close()
    } catch {
      case exception: Throwable =>
        println("[StopWord] exception:" + exception.getMessage());
    }

    var posStopList = MPSegmentConfiguration().getStopPosList();
    posStopList = posStopList.trim();
    if (posStopList.length() > 0) {
      val as = posStopList.split(",");
      if (as.length > 0) {
        for (k <- 0 until as.length) {
          val j = POSUtil.getPOSIndex(as(k));
          if (j > 0) {
            posStops(numPosStop) = j;
            numPosStop += 1;
          }
        }

      }
    }
  }

  def isStopWord(s: String): Boolean = {
    return stopWordHashMap.getOrElse(s, -1) > 0;
  }

  def isPosStopWord(posIndex: Int): Boolean = {
    if (numPosStop > 0) {
      for (j <- 0 until numPosStop) {
        if (posIndex == posStops(j)) {
          return true;
        }
      }
    }
    return false
  }

  def isStopWord(s: String, posIndex: Int): Boolean = {
    if (numPosStop > 0) {
      for (j <- 0 until numPosStop) {
        if (posIndex == posStops(j)) {
          return true
        }
      }
    }
    if (stopWordHashMap.getOrElse(s, -1) > 0) {
      return true;
    }
    return false
  }
}
