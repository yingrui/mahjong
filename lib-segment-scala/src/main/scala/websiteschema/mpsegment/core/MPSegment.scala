package websiteschema.mpsegment.core

import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.dict.IWord
import websiteschema.mpsegment.graph.{IShortestPath, IGraph, _}
import websiteschema.mpsegment.pinyin.WordToPinyinClassfierFactory

class MPSegment(config: MPSegmentConfiguration) {
  private var dijk: IShortestPath = null
  private var graph: IGraph = null
  private var posTagging: IPOSRecognizer = null
  private var withPinyin: Boolean = false
  private val conceptRecognizer: IConceptRecognizer = new SimpleConceptRecognizer()
  private var lastSection: Boolean = false
  private var lastSectionStr: String = ""
  private var useDomainDictionary: Boolean = true
  private var useContextFreqSegment: Boolean = false

  initialize()

  def isUseDomainDictionary(): Boolean = {
    return useDomainDictionary
  }

  def setUseDomainDictionary(flag: Boolean) {
    useDomainDictionary = flag
  }

  def isUseContextFreqSegment(): Boolean = {
    return useContextFreqSegment
  }

  def setUseContextFreqSegment(useContextFreqSegment: Boolean) {
    this.useContextFreqSegment = useContextFreqSegment
  }

  private def initialize() {
    withPinyin = config.isWithPinyin()
    initializePOSTagging()
  }

  private def initializeGraph(size: Int) {
    graph = new Graph(size)
    //        dijk = new BigramDijkstra(WordBigram.getInstance("word-bigram.dat"))
    //        dijk = new BigramDijkstra(WordBigram.getInstance("google-bigram.dat"))
    dijk = new DijkstraImpl(size)
  }

  private def initializePOSTagging() {
    posTagging = new POSRecognizer()
  }


  def segmentMP(sentence: String, withPOS: Boolean): SegmentResult = {
    if (sentence == null || sentence.length() < 1) {
      return null
    }
    lastSectionStr = ""
    val totalLength = sentence.length()
    var result: SegmentResult = null
    if (totalLength < 1023) {
      result = segment(sentence, withPOS, false)
    } else {
      lastSection = false
      result = new SegmentResult(0)
      var startIndex = 0
      while (startIndex < totalLength) {
        val section = getSection(sentence, startIndex)
        startIndex += section.length()
        lastSection = startIndex == totalLength
        val sectionResult = segment(section, withPOS, true)
        result.append(sectionResult)
        if (!lastSection && lastSectionStr.length() > 0) {
          startIndex -= lastSectionStr.length()
        }
      }
    }
    return result
  }

  private def buildGraph(sen: String, startPos: Int) {

    val builder = new GraphBuilder(graph, useDomainDictionary, config)
    builder.setUseContextFreqSegment(useContextFreqSegment)
    builder.buildGraph(sen, startPos)
  }

  private def buildSegmentResult(path: Path): SegmentResult = {
    val length = path.getLength()
    val wordNames = new Array[String](length)
    val domainTypes = new Array[Int](length)
    if (length < 1) {
      return null
    }
    val segmentResult = new SegmentResult(length)
    for (index <- 0 until length) {
      val edgeWeight = graph.getEdgeWeight(path.iget(index), path.iget(index + 1))
      if (edgeWeight == 0) {
        val word = graph.getEdgeObject(path.iget(index), path.iget(index + 1))
        wordNames(index) = word.getWordName()
        domainTypes(index) = word.getDomainType()
      } else {
        val word = graph.getEdgeObject(path.iget(index), path.iget(index + 1))
        wordNames(index) = word.getWordName()
        domainTypes(index) = word.getDomainType()
      }
    }

    segmentResult.setWords(wordNames)
    segmentResult.setDomainTypes(domainTypes)
    return segmentResult
  }

  private def getSection(sentence: String, startIndex: Int): String = {
    var sectionedSentence: String = null
    if (sentence.length() - startIndex >= 1000) {
      sectionedSentence = sentence.substring(startIndex, startIndex + 1000)
    } else {
      sectionedSentence = sentence.substring(startIndex)
    }
    return sectionedSentence
  }

  private def lookupStopVertex(sentence: String): Int = {
    val length = sentence.length()
    lastSectionStr = ""
    var endVertex = -2
    if (!lastSection) {
      endVertex = graph.getStopVertex(length - 20, length)
      if (endVertex > 1 && endVertex > length - 20 && endVertex < length) {
        lastSectionStr = sentence.substring(endVertex - 1)
      } else {
        lastSectionStr = ""
        endVertex = length + 1
      }
    } else {
      endVertex = length + 1
    }
    return endVertex
  }

  private def getShortestPathToStopVertex(sentence: String, sectionSegment: Boolean): Path = {
    buildGraph(sentence, 0)
    val sentenceLength = sentence.length()
    dijk.setGraph(graph)
    var p: Path = null
    if (!sectionSegment) {
      p = dijk.getShortestPath(1, sentenceLength + 1)
    } else {
      val stopVertex = lookupStopVertex(sentence)
      if (stopVertex > 1) {
        p = dijk.getShortestPath(1, stopVertex)
      } else {
        p = dijk.getShortestPath(1, sentenceLength + 1)
      }
    }
    return p
  }

  private def segment(sentence: String, withPOS: Boolean, sectionSegment: Boolean): SegmentResult = {
    initializeGraph(sentence.length + 2)
    val path = getShortestPathToStopVertex(sentence, sectionSegment)
    val result = buildSegmentResult(path)
    if (withPinyin) {
      WordToPinyinClassfierFactory().getClassifier().classify(result)
    }
    if (withPOS) {
      result.setPOSArray(posTagging.findPOS(path, graph))
      setConcepts(result, path)
    }
    return result
  }

  private def setConcepts(result: SegmentResult, path: Path) {
    val length = path.getLength()
    if (length == 0) {
      return
    }
    val words = new Array[IWord](length)
    val posArray = new Array[Int](length)
    for (index <- 0 until length) {
      words(index) = graph.getEdgeObject(path.iget(index), path.iget(index + 1))
      posArray(index) = result.getPOS(index)
    }
    conceptRecognizer.reset()
    conceptRecognizer.setPosArray(posArray)
    conceptRecognizer.setWordArray(words)
    result.setConcepts(conceptRecognizer.getConcepts())
  }


}
