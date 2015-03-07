package websiteschema.mpsegment.core

import websiteschema.mpsegment.graph.IGraph
import websiteschema.mpsegment.graph.Path

trait IPOSRecognizer {

    def findPOS(p: Path, graph: IGraph) : Array[Int]

}
