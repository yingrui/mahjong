package websiteschema.mpsegment.tools;

import websiteschema.mpsegment.dict.DictionaryFactory;
import websiteschema.mpsegment.dict.IDictionary;
import websiteschema.mpsegment.dict.IWord;

import java.io.*;
import java.util.Iterator;

public class DictionaryWriter {

    private BufferedWriter writer;

    public DictionaryWriter(File file) throws IOException {
        this.writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
    }

    public void write(IDictionary dictionary) throws IOException {
        Iterator<IWord> iterator = dictionary.iterator();
        if(null != dictionary) {
            while (iterator.hasNext()) {
                IWord word = iterator.next();
                WordStringConverter converter = new WordStringConverter(word);
                writer.write(converter.convertToString() + "\n");
            }
        }
    }

    public void close() throws IOException {
        writer.close();
    }

    public static void main(String args[]) throws IOException {
        String file = "dict.txt";
        if(null != args && args.length > 0) {
            file = args[0];
        }

        System.out.println("All words will output to file: " + file);
        DictionaryWriter writer1 = new DictionaryWriter(new File(file));
        DictionaryFactory.getInstance().loadDictionary();
        writer1.write(DictionaryFactory.getInstance().getCoreDictionary());
        writer1.close();
    }
}
