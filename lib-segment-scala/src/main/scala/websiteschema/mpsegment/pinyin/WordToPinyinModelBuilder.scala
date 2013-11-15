package websiteschema.mpsegment.pinyin

import websiteschema.mpsegment.hmm._
import collection.mutable.HashMap
import collection.mutable.Map
import java.io.{FileInputStream, InputStreamReader, BufferedReader, File}

class WordToPinyinModelBuilder {

    private var ngramLength = 2
    private var pii = HashMap[Int,Int]()
    private var emisMatrix = HashMap[Int, Map[Int,Int]]()
    private var model = new HmmModel()

    private def getStateBank() : NodeRepository = {
        return model.getStateBank
    }

    private def getObserveBank() : NodeRepository = {
        return model.getObserveBank
    }

    private def getNgram() : Trie = {
        return model.getNgram
    }

    def main( args: Array[String]) {
        var folder = "corpus/LCMC"
        var builder = new WordToPinyinModelBuilder()
        if (args.length > 0) {
            folder = args(0)
            if(args.length > 1) {
                builder.ngramLength = args(1).toInt
            }
        } else {
            println("prepare to train a model with corpus in folder: corpus/LCMC.")
        }

        var dir = new File(folder)
        if (dir.isDirectory() && dir.exists()) {
            for (f <- dir.listFiles()) {
                var filename = f.getName()
                if (filename.endsWith(".txt")) {
                    builder.train(f.getAbsolutePath())
                }
            }
            builder.build()
            builder.save("websiteschema/mpsegment/wtp.m")
        }
    }

    def build() {
        getNgram().buildIndex(1)
        model.buildEmission(emisMatrix)
        model.buildPi(pii)

        model.buildViterbi()
        clear()
    }

    private def clear() {
        pii.clear()
        emisMatrix.clear()
        pii = null
        emisMatrix = null
    }

    private def save(file: String) {
        model.save(file)
    }

    private def train(filename: String) {
        var file = new File(filename)
        if (file.exists()) {
            println("[WordToPinyinModelBuilder] train file " + file.getAbsolutePath())
            try {
                val br = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(file), "UTF-8"))
                analysisCorpus(br)
                br.close()
            } catch {
              case ex: Throwable =>
                ex.printStackTrace()
            }
        }
    }

    private def analysisCorpus(br: BufferedReader) {
        val window = new Window[Pair[String,String]](ngramLength)
        var line = br.readLine()
        while (null != line) {
            var cur = parseLine(line.trim())

            if (null == cur) {
                window.clear()
            } else {
                window.add(cur)
                var o = cur.getKey()
                var isHeadOfWord = o.startsWith("Head")
                o = if(isHeadOfWord) o.substring(4) else o
                var observe = Node(o)
                observe = getObserveBank().add(observe)
                var s = cur.getValue()
                var state = Node(s)
                state = getStateBank().add(state)

                //Pii
                if (isHeadOfWord)
                {
                    var index = state.getIndex()
                    var c = if(pii.contains(index)) pii(index) + 1 else 1
                    pii += (index -> c)
                }

                //Transition
                var array = window.toArray()
                statisticNGram(array)

                //Emission
                var si = state.getIndex()
                var o1 = observe.getIndex()
                var row: Map[Int,Int] = null
                if (emisMatrix.contains(si)) {
                    row = emisMatrix(si)
                } else {
                    row = HashMap[Int,Int]()
                    emisMatrix += (si -> row)
                }
                val count = if(row.contains(o1)) row(o1) + 1 else 1
                row += (o1 -> count)
            }
            line = br.readLine()
        }
    }

    private def parseLine(line: String):Pair[String,String] = {
        var ret: Pair[String, String] = null
        if ("".equals(line) || line.startsWith("c")) {
            return ret
        }

        var pair = line.split("\\s")

        if (null != pair && pair.length == 2) {
            ret = new Pair[String,String](pair(0), pair(1))
        }

        return ret
    }

    private def statisticNGram(array: List[Pair[String,String]]) {
        val ch = new Array[Int](array.length)
        for (i <- 0 until ch.length) {
            ch(i) = getStateBank().get(array(i).getValue()).getIndex()
        }
        getNgram().insert(ch)
    }

}

