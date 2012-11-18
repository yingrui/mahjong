/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.core;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.dict.IWord;
import websiteschema.mpsegment.dict.POSUtil;
import websiteschema.mpsegment.dict.WordImpl;

public class ConceptRecognizeTest {

    @Test
    public void should_return_n_name_when_pos_of_word_is_nr() {
        WordImpl word1 = new WordImpl("word1");
        IWord[] words = new IWord[]{word1};
        int posArray[] = new int[]{POSUtil.POS_NR};

        IConceptRecognizer conceptRecognizer = new SimpleConceptRecognizer();
        conceptRecognizer.setPosArray(posArray);
        conceptRecognizer.setWordArray(words);

        String concepts[] = conceptRecognizer.getConcepts();
        Assert.assertArrayEquals(new String[]{"n-name"}, concepts);
    }

    @Test
    public void should_return_n_location_when_pos_of_word_is_ns() {
        WordImpl word1 = new WordImpl("word1");
        IWord[] words = new IWord[]{word1};
        int posArray[] = new int[]{POSUtil.POS_NS};

        IConceptRecognizer conceptRecognizer = new SimpleConceptRecognizer();
        conceptRecognizer.setPosArray(posArray);
        conceptRecognizer.setWordArray(words);

        String concepts[] = conceptRecognizer.getConcepts();
        Assert.assertArrayEquals(new String[]{"n-location"}, concepts);
    }

    @Test
    public void should_return_na_when_word_is_not_exists_in_concept_repository() {
        WordImpl word1 = new WordImpl("word1");
        IWord[] words = new IWord[]{word1};
        int posArray[] = new int[]{POSUtil.POS_N};

        IConceptRecognizer conceptRecognizer = new SimpleConceptRecognizer();
        conceptRecognizer.setPosArray(posArray);
        conceptRecognizer.setWordArray(words);

        String concepts[] = conceptRecognizer.getConcepts();
        Assert.assertArrayEquals(new String[]{"N/A"}, concepts);
    }
}
