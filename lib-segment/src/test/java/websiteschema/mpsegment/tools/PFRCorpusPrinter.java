package websiteschema.mpsegment.tools;

import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.dict.POSUtil;
import websiteschema.mpsegment.pinyin.WordToPinyinClassfierFactory;

import java.io.*;

public class PFRCorpusPrinter {

    public static void main(String[] args) throws IOException {
        InputStream inputStream = getInputStream(args);
        PrintStream out = new PrintStream(new File("corpus.txt"));
        PFRCorpusLoader loader = new PFRCorpusLoader(inputStream);
        loader.eliminateDomainType(POSUtil.POS_NR);
        SegmentResult result = loader.readLine();
        while (result != null) {
            print(result, out);
            result = loader.readLine();
        }
    }

    private static void print(SegmentResult result, PrintStream out) {
        WordToPinyinClassfierFactory.getInstance().getClassifier().classify(result);
        boolean containsDomainWord = false;
        for (int i = 0; i < result.length(); i++) {
            if (result.getDomainType(i) != 0) {
                containsDomainWord = true;
                break;
            }
        }
        if (containsDomainWord) {
            for (int i = 0; i < result.length(); i++) {
                int domainType = result.getDomainType(i);
                out.println(result.getWord(i) + "\t" +
                        result.getPinyin(i) + "\t" +
                        POSUtil.getPOSString(result.getPOS(i)) + "\t" +
                        result.getConcept(i) + "\t" +
                        (domainType != 0 ? POSUtil.getPOSString(domainType) : "O"));
            }
            out.println();
        }
    }

    private static InputStream getInputStream(String[] args) throws FileNotFoundException {
        InputStream inputStream = null;
        if (args.length > 0) {
            inputStream = new FileInputStream(args[0]);
        } else {
            inputStream = PFRCorpusPrinter.class.getClassLoader().getResourceAsStream("PFR-199801-utf-8.txt");
        }
        return inputStream;
    }
}
