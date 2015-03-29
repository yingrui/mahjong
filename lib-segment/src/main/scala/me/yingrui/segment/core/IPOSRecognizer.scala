package me.yingrui.segment.core

import me.yingrui.segment.graph.IGraph
import me.yingrui.segment.graph.Path

trait IPOSRecognizer {

    def findPOS(p: Path, graph: IGraph) : Array[Int]

}
