///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package websiteschema.mpsegment.core
//
//import org.junit.Assert
//import org.junit.Test
//import websiteschema.mpsegment.dict.IWord
//import websiteschema.mpsegment.dict.POSUtil
//import websiteschema.mpsegment.dict.WordImpl
//
//class ConceptRecognizeTest {
//
//    @Test
//    def should_return_n_name_when_pos_of_word_is_nr() {
//        var word1 = new WordImpl("word1")
//        var words = new Array[IWord]{word1}
//        var posArray = new Array[Int]{POSUtil.POS_NR}
//
//        var conceptRecognizer = new SimpleConceptRecognizer()
//        conceptRecognizer.setPosArray(posArray)
//        conceptRecognizer.setWordArray(words)
//
//        var concepts = conceptRecognizer.getConcepts()
//        Assert.assertArrayEquals(new Array[String]{"n-name"}, concepts)
//    }
//
//    @Test
//    def should_return_n_location_when_pos_of_word_is_ns() {
//        var word1 = new WordImpl("word1")
//        var words = new Array[IWord]{word1}
//        var posArray = new Array[Int]{POSUtil.POS_NS}
//
//        var conceptRecognizer = new SimpleConceptRecognizer()
//        conceptRecognizer.setPosArray(posArray)
//        conceptRecognizer.setWordArray(words)
//
//        var concepts = conceptRecognizer.getConcepts()
//        Assert.assertArrayEquals(new Array[String]{"n-location"}, concepts)
//    }
//
//    @Test
//    def should_return_na_when_word_is_not_exists_in_concept_repository() {
//        var word1 = new WordImpl("word1")
//        var words = new Array[IWord]{word1}
//        var posArray = new Array[Int]{POSUtil.POS_N}
//
//        var conceptRecognizer = new SimpleConceptRecognizer()
//        conceptRecognizer.setPosArray(posArray)
//        conceptRecognizer.setWordArray(words)
//
//        var concepts = conceptRecognizer.getConcepts()
//        Assert.assertArrayEquals(new Array[String]{"N/A"}, concepts)
//    }
//}
