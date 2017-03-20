package me.yingrui.segment.lucene5

import java.io.File

import org.apache.commons.io.FileUtils
import org.apache.lucene.document.{Document, Field, TextField}
import org.apache.lucene.index.IndexWriterConfig.OpenMode
import org.apache.lucene.index.{DirectoryReader, IndexWriter, IndexWriterConfig}
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.{IndexSearcher, TopDocs}
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version
import org.junit.Assert._
import org.junit.{Before, Test}

class IndexTest {

  val version = Version.LUCENE_5_5_4
  val indexPath = "test_index_dir"
  val analyzer = new MPSegmentAnalyzer
  var searcher: IndexSearcher = null
  var results: TopDocs = null

  @Before
  def setup {
    FileUtils.deleteDirectory(new File(indexPath))
  }

  @Test
  def should_retrieve_document_by_keyword {
    val writer = createIndexWriter

    writer.addDocument(createDocument("希尔伯特", "我们必须知道，我们将要知道！"))
    writer.addDocument(createDocument("Hilbert", "We must know, we will know!"))
    writer.close()

    searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(indexPath).toPath)))

    results = retrieve("content", "知道")
    verifySearchResult(0)

    results = retrieve("content", "know")
    verifySearchResult(1)
  }

  def verifySearchResult(expectDocId: Int) {
    val numTotalHits = results.totalHits
    assertEquals(1, numTotalHits)

    assertEquals(expectDocId, results.scoreDocs(0).doc)
  }

  def retrieve(field: String, keyword: String) = {
    val parser = new QueryParser(field, analyzer)
    val query = parser parse keyword
    searcher.search(query, null, 100)
  }

  def createDocument(title: String, content: String): Document = {
    val document = new Document
    document add new TextField("title", title, Field.Store.YES)
    document add new TextField("content", content, Field.Store.YES)
    document
  }

  def createIndexWriter = {
    val dir = FSDirectory.open(new File(indexPath).toPath)
    val iwc = new IndexWriterConfig(analyzer)
    iwc.setOpenMode(OpenMode.CREATE_OR_APPEND)

    new IndexWriter(dir, iwc)
  }
}
