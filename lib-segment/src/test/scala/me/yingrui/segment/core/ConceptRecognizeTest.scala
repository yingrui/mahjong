/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package me.yingrui.segment.core

import org.junit.Assert
import org.junit.Test
import me.yingrui.segment.dict.IWord
import me.yingrui.segment.dict.POSUtil
import me.yingrui.segment.dict.WordImpl

class ConceptRecognizeTest {

    @Test
    def should_return_n_name_when_pos_of_word_is_nr() {
        val word1 = new WordImpl("word1")
        val words = List[IWord](word1).toArray
        val posArray = List[Int](POSUtil.POS_NR).toArray

        val conceptRecognizer = new SimpleConceptRecognizer()
        conceptRecognizer.setPosArray(posArray)
        conceptRecognizer.setWordArray(words)

        val concepts = conceptRecognizer.getConcepts()
        Assert.assertEquals("n-name", concepts(0))
    }

    @Test
    def should_return_n_location_when_pos_of_word_is_ns() {
        val word1 = new WordImpl("word1")
        val words = List[IWord](word1).toArray
        val posArray = List[Int](POSUtil.POS_NS).toArray

        val conceptRecognizer = new SimpleConceptRecognizer()
        conceptRecognizer.setPosArray(posArray)
        conceptRecognizer.setWordArray(words)

        val concepts = conceptRecognizer.getConcepts()
        Assert.assertEquals("n-location", concepts(0))
    }

    @Test
    def should_return_na_when_word_is_not_exists_in_concept_repository() {
        val word1 = new WordImpl("word1")
        val words = List[IWord](word1).toArray
        val posArray = List[Int](POSUtil.POS_N).toArray

        val conceptRecognizer = new SimpleConceptRecognizer()
        conceptRecognizer.setPosArray(posArray)
        conceptRecognizer.setWordArray(words)

        val concepts = conceptRecognizer.getConcepts()
        Assert.assertEquals("N/A", concepts(0))
    }
}
