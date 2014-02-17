package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;
import scala.collection.immutable.List;
import websiteschema.mpsegment.concept.Concept;
import websiteschema.mpsegment.core.SegmentWorker;
import websiteschema.mpsegment.dict.DictionaryFactory;
import websiteschema.mpsegment.dict.IDictionary;
import websiteschema.mpsegment.dict.IWord;
import websiteschema.mpsegment.dict.POSArray;
import websiteschema.mpsegment.hmm.Emission;
import websiteschema.mpsegment.hmm.Node;
import websiteschema.mpsegment.hmm.NodeRepository;
import websiteschema.mpsegment.pinyin.WordToPinyinClassfierFactory;

import java.util.Collection;

public class WordItemCreator {

    public void createWordItems(JdbcTemplate jdbcTemplate) {
        SegmentWorker.apply();
        IDictionary dict = DictionaryFactory.apply().getCoreDictionary();
        try {
            createWordItemsByDictionary(jdbcTemplate, dict);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        }
    }

    private void createWordItemsByDictionary(JdbcTemplate jdbcTemplate, IDictionary dict) {
        List<IWord> wordList = dict.iterator();
        for (int i = 0; i < wordList.length(); i++) {
            IWord word = wordList.apply(i);
            createWordItem(jdbcTemplate, word);
            System.out.print("\rCreated " + i + " Words.              ");
        }
        System.out.println();
    }

    private void createWordItem(JdbcTemplate jdbcTemplate, IWord word) {
        jdbcTemplate.update("INSERT INTO WordItems (Name, Type, CreateBy, CreateAt, LastModifiedBy, LastModifiedAt) VALUES (?, 'core', 1, 'now', 1, 'now')", word.getWordName());
        int wordId = jdbcTemplate.queryForInt("SELECT Id FROM WordItems WHERE Name=?", word.getWordName());
        createWordPartOfSpeechRelation(jdbcTemplate, wordId, word.getPOSArray());
        createWordConceptRelation(jdbcTemplate, wordId, word.getConcepts());
        createWordPinyinRelation(jdbcTemplate, wordId, word.getWordName());
    }

    private void createWordPinyinRelation(JdbcTemplate jdbcTemplate, int wordId, String wordName) {
        if (wordName.length() == 1) {
            NodeRepository observeBank = WordToPinyinClassfierFactory.apply().getClassifier().model().getObserveBank();
            Node o = observeBank.get(wordName);
            if (o != null) {
                Emission emission = WordToPinyinClassfierFactory.apply().getClassifier().model().getEmission();
                Collection stateProbByObserve = emission.getStatesBy(o.getIndex());
                if (null != stateProbByObserve) {
                    NodeRepository stateBank = WordToPinyinClassfierFactory.apply().getClassifier().model().getStateBank();
                    for (Object i : stateProbByObserve) {
                        Node state = stateBank.get(Integer.parseInt(i.toString()));
                        jdbcTemplate.execute("INSERT INTO Pinyins (WordId, Name) VALUES (" +
                                +wordId + ",'" + state.getName() + "')");
                    }
                }
            }
        } else {
            List<String> pinyinList = WordToPinyinClassfierFactory.apply().getClassifier().classify(wordName);
            String pinyin = join(pinyinList, "'");
            jdbcTemplate.update("INSERT INTO Pinyins (WordId, Name) VALUES (" +
                    +wordId + ", ?)", pinyin);
        }
    }

    private String join(List<String> list, String sep) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.length(); i++) {
            stringBuilder.append(list.apply(i));
            if (i < list.length() - 1) {
                stringBuilder.append(sep);
            }
        }
        return stringBuilder.toString();
    }

    private void createWordConceptRelation(JdbcTemplate jdbcTemplate, int wordId, Concept[] concepts) {
        if (null != concepts) {
            for (Concept concept : concepts) {
                if (!concept.equals(Concept.UNKNOWN())) {
                    int conceptId = jdbcTemplate.queryForInt("SELECT Id FROM Concepts WHERE Name='" + concept.getName() + "'");
                    jdbcTemplate.execute("INSERT INTO WordConcept (WordId, ConceptId) VALUES (" +
                            wordId + ", " + conceptId + ")");
                }
            }
        }
    }

    private void createWordPartOfSpeechRelation(JdbcTemplate jdbcTemplate, int wordId, POSArray posArray) {
        int[][] posTable = posArray.getWordPOSTable();
        for (int i = 0; i < posTable.length; i++) {
            int partOfSpeech = posTable[i][0];
            int freq = posTable[i][1];
            jdbcTemplate.execute("INSERT INTO WordFreq (WordId, PartOfSpeechId, Freq) VALUES (" +
                    wordId + ", " + partOfSpeech + ", " + freq + ")");
        }
    }

}
