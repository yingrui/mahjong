//package websiteschema.mpsegment.tools;
//
//import websiteschema.mpsegment.dict.DictionaryFactory;
//import websiteschema.mpsegment.dict.IDictionary;
//import websiteschema.mpsegment.dict.IWord;
//
//import java.io.*;
//import java.util.Iterator;
//
//class DictionaryWriter {
//
//    private var writer : BufferedWriter = null
//
//    public DictionaryWriter(File file) throws IOException {
//        this.writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
//    }
//
//    def write(dictionary: IDictionary) throws IOException {
//        var iterator = dictionary.iterator()
//        if(null != dictionary) {
//            while (iterator.hasNext()) {
//                var word = iterator.next()
//                var converter = new WordStringConverter(word)
//                writer.write(converter.convertToString() + "\n");
//            }
//        }
//    }
//
//    def close() throws IOException {
//        writer.close();
//    }
//
//    public static void main(Array[String] args) throws IOException {
//        var file = "dict.txt"
//        if(null != args && args.length > 0) {
//            file = args(0);
//        }
//
//        println("All words will output to file: " + file);
//        var writer1 = new DictionaryWriter(new File(file))
//        DictionaryFactory.getInstance().loadDictionary();
//        writer1.write(DictionaryFactory.getInstance().getCoreDictionary());
//        writer1.close();
//    }
//}
