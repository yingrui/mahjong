package websiteschema.mpsegment.tools;

import websiteschema.mpsegment.concept.Concept;
import websiteschema.mpsegment.concept.ConceptRepository;
import websiteschema.mpsegment.concept.ConceptTree;
import websiteschema.mpsegment.dict.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConceptLoader {

    public static void main(String args[]) throws IOException, DictionaryException {
        DictionaryFactory.getInstance().loadDictionary();
        DictionaryFactory.getInstance().loadDomainDictionary();
        DictionaryFactory.getInstance().loadUserDictionary();

        String fileNames[] = new String[]{
                "/Users/twer/workspace/nlp/ccd/noun-list.txt",
                "/Users/twer/workspace/nlp/ccd/verb-list.txt",
                "/Users/twer/workspace/nlp/ccd/adj-list.txt"};
        File file1 = new File(fileNames[0]);
        load(file1, ConceptRepository.getInstance().getNounConceptTree(), "N");

        File file2 = new File(fileNames[1]);
        load(file2, ConceptRepository.getInstance().getVerbConceptTree(), "V");

        File file3 = new File(fileNames[2]);
        load(file3, ConceptRepository.getInstance().getAdjConceptTree(), "A");

        DictionaryWriter writer = new DictionaryWriter(new File("dict.txt"));
        writer.write(DictionaryFactory.getInstance().getCoreDictionary());
        writer.close();
    }

    private static void load(File file, ConceptTree nounConceptTree, String pos) throws IOException, DictionaryException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
        String line = reader.readLine();
        while (null != line) {
            String array[] = line.split(" ");
            if (null != array && array.length == 2) {
                String wordName = array[0];
                String concepts[] = array[1].split("\\|");
                WordImpl word = (WordImpl) DictionaryFactory.getInstance().getCoreDictionary().getWord(wordName);
                if (word == null) {
                    System.out.println("missing word: " + wordName + " " + array[1]);
                    word = new WordImpl(wordName);
                    POSArray posArray = new POSArray();
                    posArray.add(new POS(pos, 50));
                    word.setPosArray(posArray);
                    DictionaryFactory.getInstance().getCoreDictionary().addWord(word);
                }
                if (word != null) {
                    List<Concept> conceptList = new ArrayList<Concept>();
                    for (String conceptStr : concepts) {
                        if (!conceptStr.equals("null")) {
                            Concept concept = nounConceptTree.getConceptByDescription(conceptStr);
                            if (null != concept) {
                                conceptList.add(concept);
                            }
                        }
                    }
                    word.setConcepts(conceptList.toArray(new Concept[0]));
                }
            }
            line = reader.readLine();
        }
        reader.close();
    }
}
