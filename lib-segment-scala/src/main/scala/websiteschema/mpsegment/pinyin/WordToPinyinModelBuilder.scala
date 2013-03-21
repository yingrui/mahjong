//package websiteschema.mpsegment.pinyin
//
//import websiteschema.mpsegment.hmm.*
//
//import java.io.*
//import java.util.HashMap
//import java.util.Map
//
//class WordToPinyinModelBuilder {
//
//    private var ngramLength : Int = 2
//    private Map[Int,Int] pii = new HashMap[Int,Int]()
//    private Map<Int, Map[Int,Int]> emisMatrix = new HashMap<Int, Map[Int,Int]>()
//    private var model : WordToPinyinModel = new WordToPinyinModel()
//
//    private def getStateBank() : NodeRepository = {
//        return model.getStateBank()
//    }
//
//    private def getObserveBank() : NodeRepository = {
//        return model.getObserveBank()
//    }
//
//    private def getNgram() : Trie = {
//        return model.getNgram()
//    }
//
//    public static void main(Array[String] args) throws IOException {
//        var folder = "corpus/LCMC"
//        var builder = new WordToPinyinModelBuilder()
//        if (args.length > 0) {
//            folder = args(0)
//            if(args.length > 1) {
//                builder.ngramLength = Int.parseInt(args(1))
//            }
//        } else {
//            println("prepare to train a model with corpus in folder: corpus/LCMC.")
//        }
//
//        var dir = new File(folder)
//        if (dir.isDirectory() && dir.exists()) {
//            for (f <- dir.listFiles()) {
//                var filename = f.getName()
//                if (filename.endsWith(".txt")) {
//                    builder.train(f.getAbsolutePath())
//                }
//            }
//            builder.build()
//            builder.save("websiteschema/mpsegment/wtp.m")
//        }
//    }
//
//    def build() {
//        getNgram().buildIndex(1)
//        model.buildEmission(emisMatrix)
//        model.buildPi(pii)
//
//        model.buildViterbi()
//        clear()
//    }
//
//    private def clear() {
//        pii.clear()
//        emisMatrix.clear()
//        pii = null
//        emisMatrix = null
//    }
//
//    private def save(file: String) throws IOException {
//        model.save(file)
//    }
//
//    private def train(filename: String) {
//        var file = new File(filename)
//        if (file.exists()) {
//            println("[WordToPinyinModelBuilder] train file " + file.getAbsolutePath())
//            try {
//                BufferedReader br = new BufferedReader(
//                        new InputStreamReader(
//                                new FileInputStream(file), "UTF-8"))
//                analysisCorpus(br)
//                br.close()
//            } catch {
//                ex.printStackTrace()
//            }
//        }
//    }
//
//    private def analysisCorpus(br: BufferedReader) throws IOException {
//        Window<Pair[String,String]> window = new Window<Pair[String,String]>(ngramLength)
//        var cur = null
//        var line = br.readLine()
//        while (null != line) {
//            cur = parseLine(line.trim())
//
//            if (null == cur) {
//                window.clear()
//            } else {
//                window.add(cur)
//                var o = cur.getKey()
//                var isHeadOfWord = o.startsWith("Head")
//                o = isHeadOfWord ? o.substring(4) : o
//                var observe = new Node(o)
//                observe = getObserveBank().add(observe)
//                var s = cur.getValue()
//                var state = new Node(s)
//                state = getStateBank().add(state)
//
//                //Pii
//                if (isHeadOfWord)
//                {
//                    var index = state.getIndex()
//                    var c = pii.containsKey(index) ? pii.get(index) + 1 : 1
//                    pii.put(index, c)
//                }
//
//                //Transition
//                var Array[array] = window.toArray(new Pair[0])
//                statisticNGram(array)
//
//                //Emission
//                var si = state.getIndex()
//                var o1 = observe.getIndex()
//                var row = null
//                if (emisMatrix.containsKey(si)) {
//                    row = emisMatrix.get(si)
//                } else {
//                    row = new HashMap[Int,Int]()
//                    emisMatrix.put(si, row)
//                }
//                var count = row.containsKey(o1) ? row.get(o1) + 1 : 1
//                row.put(o1, count)
//            }
//            line = br.readLine()
//        }
//    }
//
//    private Pair[String,String] parseLine(String line) {
//        var ret = null
//        if ("".equals(line) || line.startsWith("c")) {
//            return ret
//        }
//
//        var pair = line.split("\\s")
//
//        if (null != pair && pair.length == 2) {
//            ret = new Pair[String,String](pair(0), pair(1))
//        }
//
//        return ret
//    }
//
//    private def statisticNGram(Array[array]: Pair[String,String]) {
//        var ch = new Int[array.length]
//        for (Int i = 0; i < ch.length; i++) {
//            ch(i) = getStateBank().get(array(i).getValue()).getIndex()
//        }
//        getNgram().insert(ch)
//    }
//
//}
//
