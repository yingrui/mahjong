//package websiteschema.mpsegment.pinyin;
//
//import websiteschema.mpsegment.conf.MPSegmentConfiguration;
//
//import java.io.IOException;
//
//class WordToPinyinClassfierFactory {
//
//    private static WordToPinyinClassfierFactory instance = new WordToPinyinClassfierFactory();
//    private var classifier : WordToPinyinClassifier = new WordToPinyinClassifier();
//
//    private WordToPinyinClassfierFactory() {
//        try {
//            var model = new WordToPinyinModel()
//            model.load(MPSegmentConfiguration.getInstance().getPinyinModel());
//            classifier.setModel(model);
//        } catch {
//            ex.printStackTrace();
//        }
//    }
//
//    public static WordToPinyinClassfierFactory getInstance() {
//        return instance;
//    }
//
//    def getClassifier() : WordToPinyinClassifier = {
//        return classifier;
//    }
//}
