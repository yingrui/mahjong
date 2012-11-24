package websiteschema.mpsegment.pinyin;

import websiteschema.mpsegment.hmm.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WordToPinyinModelBuilder {

    private int ngramLength = 2;
    private Map<Integer, Integer> pii = new HashMap<Integer, Integer>();
    private Map<Integer, Map<Integer, Integer>> emisMatrix = new HashMap<Integer, Map<Integer, Integer>>();
    private WordToPinyinModel model = new WordToPinyinModel();

    private NodeRepository getStateBank() {
        return model.getStateBank();
    }

    private NodeRepository getObserveBank() {
        return model.getObserveBank();
    }

    private Trie getNgram() {
        return model.getNgram();
    }

    public static void main(String[] args) throws IOException {
        String folder = "corpus/LCMC";
        WordToPinyinModelBuilder builder = new WordToPinyinModelBuilder();
        if (args.length > 0) {
            folder = args[0];
            if(args.length > 1) {
                builder.ngramLength = Integer.parseInt(args[1]);
            }
        } else {
            System.out.println("prepare to train a model with corpus in folder: corpus/LCMC.");
        }

        File dir = new File(folder);
        if (dir.isDirectory() && dir.exists()) {
            for (File f : dir.listFiles()) {
                String filename = f.getName();
                if (filename.endsWith(".txt")) {
                    builder.train(f.getAbsolutePath());
                }
            }
            builder.build();
            builder.save("websiteschema/mpsegment/wtp.m");
        }
    }

    public void build() {
        getNgram().buildIndex(1);
        model.buildEmission(emisMatrix);
        model.buildPi(pii);

        model.buildViterbi();
        clear();
    }

    private void clear() {
        pii.clear();
        emisMatrix.clear();
        pii = null;
        emisMatrix = null;
    }

    private void save(String file) throws IOException {
        model.save(file);
    }

    private void train(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            System.out.println("[WordToPinyinModelBuilder] train file " + file.getAbsolutePath());
            try {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(file), "UTF-8"));
                analysisCorpus(br);
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void analysisCorpus(BufferedReader br) throws IOException {
        Window<Pair<String, String>> window = new Window<Pair<String, String>>(ngramLength);
        Pair<String, String> cur = null;
        String line = br.readLine();
        while (null != line) {
            cur = parseLine(line.trim());

            if (null == cur) {
                window.clear();
            } else {
                window.add(cur);
                String o = cur.getKey();
                boolean isHeadOfWord = o.startsWith("Head");
                o = isHeadOfWord ? o.substring(4) : o;
                Node observe = new Node(o);
                observe = getObserveBank().add(observe);
                String s = cur.getValue();
                Node state = new Node(s);
                state = getStateBank().add(state);

                //Pii
                if (isHeadOfWord)
                {
                    int index = state.getIndex();
                    int c = pii.containsKey(index) ? pii.get(index) + 1 : 1;
                    pii.put(index, c);
                }

                //Transition
                Pair<String, String> array[] = window.toArray(new Pair[0]);
                statisticNGram(array);

                //Emission
                int si = state.getIndex();
                int o1 = observe.getIndex();
                Map<Integer, Integer> row = null;
                if (emisMatrix.containsKey(si)) {
                    row = emisMatrix.get(si);
                } else {
                    row = new HashMap<Integer, Integer>();
                    emisMatrix.put(si, row);
                }
                int count = row.containsKey(o1) ? row.get(o1) + 1 : 1;
                row.put(o1, count);
            }
            line = br.readLine();
        }
    }

    private Pair<String, String> parseLine(String line) {
        Pair<String, String> ret = null;
        if ("".equals(line) || line.startsWith("c")) {
            return ret;
        }

        String[] pair = line.split("\\s");

        if (null != pair && pair.length == 2) {
            ret = new Pair<String, String>(pair[0], pair[1]);
        }

        return ret;
    }

    private void statisticNGram(Pair<String, String> array[]) {
        int[] ch = new int[array.length];
        for (int i = 0; i < ch.length; i++) {
            ch[i] = getStateBank().get(array[i].getValue()).getIndex();
        }
        getNgram().insert(ch);
    }

}

