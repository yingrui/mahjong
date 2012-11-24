package websiteschema.mpsegment.pinyin;

import java.io.IOException;

public class WordToPinyinClassfierFactory {

    private final static WordToPinyinClassfierFactory instance = new WordToPinyinClassfierFactory();
    private final WordToPinyinClassifier classifier = new WordToPinyinClassifier();

    private WordToPinyinClassfierFactory() {
        try {
            WordToPinyinModel model = new WordToPinyinModel();
            model.load("wtp.m");
            classifier.setModel(model);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static WordToPinyinClassfierFactory getInstance() {
        return instance;
    }

    public WordToPinyinClassifier getClassifier() {
        return classifier;
    }
}
