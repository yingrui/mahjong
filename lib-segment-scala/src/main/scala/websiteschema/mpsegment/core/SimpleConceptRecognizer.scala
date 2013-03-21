//package websiteschema.mpsegment.core
//
//import websiteschema.mpsegment.concept.Concept
//import websiteschema.mpsegment.dict.IWord
//import websiteschema.mpsegment.dict.POSUtil
//
//class SimpleConceptRecognizer extends IConceptRecognizer {
//    private var words : Array[IWord] = null
//    private var posArray : Array[Int] = null
//
//    override def setWordArray(wordArray: Array[IWord]) {
//        this.words = wordArray
//    }
//
//    override def setPosArray(posArray: Array[Int]) {
//        this.posArray = posArray
//    }
//
//    override def getConcepts() : Array[String] = {
//        assert (words != null && posArray != null && words.length == posArray.length)
//        var length = words.length
//        var concepts = new Array[String](length)
//        for(Int i = 0; i < length; i++) {
//            concepts(i) = getConcept(words(i), posArray(i))
//        }
//        return concepts
//    }
//
//    private def getConcept(word: IWord, pos: Int) : String = {
//        var concepts = word.getConcepts()
//
//        if(pos == POSUtil.POS_NS) {
//            return "n-location"
//        }
//
//        if(pos == POSUtil.POS_NR) {
//            return "n-name"
//        }
//
//        if (null != concepts) {
//            var primaryPOS = POSUtil.getPOSString(pos).substring(0, 1).toLowerCase()
//            for (Int i = 0; i < concepts.length; i++) {
//                if (concepts(i).getName().startsWith(primaryPOS)) {
//                    return concepts(i).getName()
//                }
//            }
//        }
//        return Concept.UNKNOWN.getName()
//    }
//
//    override def reset() {
//        words = null
//        posArray = null
//    }
//
//
//}
