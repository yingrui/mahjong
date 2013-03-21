package websiteschema.mpsegment.dict

object POSArrayFactory {

  private val posArray = new POSArray()

  var pos = POS(POSUtil.getPOSString(POSUtil.POS_UNKOWN), 1)
  posArray.add(pos)

  def getUnknownWordPOSArray() = posArray

}
