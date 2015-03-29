package me.yingrui.segment.hmm

import me.yingrui.segment.util.ISerialize
import me.yingrui.segment.util.SerializeHandler

class Trie extends ISerialize {

  var key = -1
  var count = 0
  var prob = 0.0D
  var descendant:Array[Trie] = null

  def getKey(): Int = {
    return key
  }

  def buildIndex(c: Int) {
    prob = count.toDouble /(c.toDouble + 1.0D)
    if (null != descendant) {
      for (node <- descendant.toList) {
        node.buildIndex(count)
      }
      Trie.sortor.sort(descendant)
    }
  }

  def insert(ngram: Array[Int]): Trie = {
    return insert(ngram, 1)
  }

  def insert(ngram: Array[Int], freq: Int): Trie = {
    count += freq
    if (ngram.length > 0) {
      val k = ngram(0)
      var n:Trie = if (null != descendant) binarySearch(descendant, descendant.length, k) else null
      if (null == n) {
        n = new Trie()
        n.key = k
        add(n)
        descendant = Trie.sortor.sort(descendant)
      }

      val rec = new Array[Int](ngram.length - 1)
      for (i <- 1 until ngram.length)
      {
        rec(i - 1) = ngram(i)
      }
      return n.insert(rec)
    } else {
      return this
    }
  }

  def add(e: Trie) {
    var i = 0
    if (null == descendant) {
      descendant = new Array[Trie](1)
    } else {
      val tmp = new Array[Trie](descendant.length + 1)
      System.arraycopy(descendant, 0, tmp, 0, descendant.length)
      i = descendant.length
      descendant = tmp
    }
    descendant(i) = e
  }

  def searchNode(ngram: Array[Int]): Trie = {
    val k = ngram(0)
    val n = searchNode(k)
    if (null != n && ngram.length > 1) {
      val rec = new Array[Int](ngram.length - 1)
      for (i <- 1 until ngram.length)
      {
        rec(i - 1) = ngram(i)
      }
      return n.searchNode(rec)
    }
    return n
  }

  def searchNode(k: Int): Trie = {
    return if (null != descendant) binarySearch(descendant, descendant.length, k) else null
  }

  def getCount(): Int = {
    return count
  }

  def setCount(count: Int) {
    this.count = count
  }

  def getProb(): Double = {
    return prob
  }

  def setProb(prob: Double) {
    this.prob = prob
  }

  def binarySearch(list: Array[Trie], listLength: Int, searchItem: Int): Trie = {
    if (null == list) {
      return null
    }
    var first = 0
    var last = listLength - 1
    var mid = -1

    var found = false
    while (first <= last && !found) {
      mid = (first + last) / 2

      val i = list(mid).key - searchItem

      if (i == 0) {
        found = true
      } else {
        if (i > 0) {
          last = mid - 1
        } else {
          first = mid + 1
        }
      }
    }

    if (found) {
      return list(mid)
    } else {
      return null
    }
  }

  def printTreeNode(indent: String) {
    println(indent + key + " - " + count + " - " + prob)
    if (null != descendant) {
      for (node <- descendant) {
        node.printTreeNode(indent + "  ")
      }
    }
  }


  def getNumberOfNodeWhichCountLt(lt: Int): Int = {
    var c = if(count < lt) 1 else 0

    if (null != descendant) {
      for (node <- descendant) {
        c += node.getNumberOfNodeWhichCountLt(lt)
      }
    }

    return c
  }

  def cutCountLowerThan(lt: Int) {
    if (lt == 1) {
      return
    }
    if (null != descendant) {
      var l = List[Trie]()
      for (i <- 0 until descendant.length)
      {
        val node = descendant(i)
        if (node.getCount() >= lt) {
          l = l ++ List(node)
          node.cutCountLowerThan(lt)
        }
      }

      descendant = l.toArray
    }
  }

  override def save(writeHandler: SerializeHandler) {
    writeHandler.serializeInt(key)
    writeHandler.serializeInt(count)
    writeHandler.serializeDouble(prob)
    if (null != descendant) {
      writeHandler.serializeInt(descendant.length)
      for (child <- descendant.toList)
      {
        child.save(writeHandler)
      }
    } else {
      writeHandler.serializeInt(0)
    }
  }

  override def load(readHandler: SerializeHandler) {
    key = readHandler.deserializeInt()
    count = readHandler.deserializeInt()
    prob = readHandler.deserializeDouble()
    val numberOfDescendant = readHandler.deserializeInt()
    if (numberOfDescendant > 0) {
      descendant = new Array[Trie](numberOfDescendant)
      for (i <- 0 until numberOfDescendant)
      {
        val child = new Trie()
        child.load(readHandler)
        descendant(i) = child
      }
    }
  }
}

object Trie {
  var sortor: TrieNodeSortor = new TrieNodeBinarySort()

  def setTreeNodeSorter(trieNodeSortor: TrieNodeSortor) {
    sortor = trieNodeSortor
  }
}
