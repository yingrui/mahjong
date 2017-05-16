package me.yingrui.segment.core

import me.yingrui.segment.conf.MPSegmentConfiguration
import me.yingrui.segment.dict.DictionaryService
import me.yingrui.segment.graph.IGraph
import me.yingrui.segment.util.StringUtil

import collection.mutable.Map

class GraphBuilder(graph: IGraph, useDomainDictionary: Boolean, config: MPSegmentConfiguration) {

  private val segmentMin = config.isSegmentMin()
  private val loadDomainDictionary: Boolean = config.isLoadDomainDictionary()
  private val loadUserDictionary: Boolean = config.isLoadUserDictionary()
  private val useContextFreqSegment: Boolean = config.isUseContextFreqSegment()
  private val dictionaryService: DictionaryService = new DictionaryService(useDomainDictionary, loadDomainDictionary, loadUserDictionary, config.isLoadEnglishDictionary())
  private val maxWordLength: Int = config.getMaxWordLength()
  private var sentence: String = ""
  private var contextFreqMap: Map[String, Int] = null

  def scanContextFreq(startPos: Int) {
    val scanner = getContextFrequencyScanner()
    scanner.startScanningAt(startPos)
    this.contextFreqMap = scanner.getContextFreqMap()
  }

  private def getContextFrequencyScanner(): ContextFrequencyScanner = {
    val scanner = new ContextFrequencyScanner()
    scanner.setSentence(sentence)
    scanner.setDictionaryService(dictionaryService)
    scanner.setMaxWordLength(maxWordLength)
    return scanner
  }

  def setSentence(sen: String) {
    sentence = sen
  }

  def buildGraph(sen: String, startPos: Int) {
    setSentence(sen)
    if (useContextFreqSegment) {
      scanContextFreq(startPos)
    }
    val length = sentence.length()
    for (i <- 0 until length) {
      graph.addVertex()
    }

    val scanner = getSegmentWordScanner()
    scanner.startScanningAt(startPos)
  }

  private def getSegmentWordScanner(): SegmentWordScanner = {
    val scanner = new SegmentWordScanner(segmentMin, useContextFreqSegment, graph, contextFreqMap)
    scanner.setSentence(sentence)
    scanner.setMaxWordLength(maxWordLength)
    scanner.setDictionaryService(dictionaryService)
    return scanner
  }

  def getContextFreqMap(): Map[String, Int] = {
    return contextFreqMap
  }
}

