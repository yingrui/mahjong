package me.yingrui.segment.core

import me.yingrui.segment.conf.MPSegmentConfiguration
import me.yingrui.segment.dict.DictionaryService
import me.yingrui.segment.graph.IGraph
import me.yingrui.segment.util.StringUtil

import collection.mutable.Map

class GraphBuilder(graph: IGraph, useDomainDictionary: Boolean, config: MPSegmentConfiguration) {

  private var contextFreqMap: Map[String, Int] = null
  private var segmentMin: Boolean = false
  private var upperCaseAll: Boolean = false
  private var halfShapeAll: Boolean = false
  private var upperCaseOrHalfShapeAll: Boolean = false
  private var loadDomainDictionary: Boolean = false
  private var loadUserDictionary: Boolean = false
  private var useContextFreqSegment: Boolean = false
  private var dictionaryService: DictionaryService = null
  private var sentence: String = ""
  private var maxWordLength: Int = 0

  segmentMin = config.isSegmentMin()
  upperCaseAll = config.isUpperCaseAll()
  halfShapeAll = config.isHalfShapeAll()
  upperCaseOrHalfShapeAll = upperCaseAll || halfShapeAll
  loadDomainDictionary = config.isLoadDomainDictionary()
  loadUserDictionary = config.isLoadUserDictionary()
  maxWordLength = config.getMaxWordLength()
  dictionaryService = new DictionaryService(useDomainDictionary, loadDomainDictionary, loadUserDictionary, config.isLoadEnglishDictionary())

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
    sentence = sen;
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

  def setUseContextFreqSegment(useContextFreqSegment: Boolean) {
    this.useContextFreqSegment = useContextFreqSegment
  }


}

