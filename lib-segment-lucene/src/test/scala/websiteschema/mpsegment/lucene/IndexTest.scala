package websiteschema.mpsegment.lucene

import java.io.File
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.{TextField, Field, Document}
import org.apache.lucene.index.{DirectoryReader, IndexWriter, IndexWriterConfig}
import org.apache.lucene.index.IndexWriterConfig.OpenMode
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.FSDirectory
import org.junit.{After, Assert, Before, Test}
import org.apache.lucene.util.Version
import org.apache.commons.io.FileUtils

class IndexTest {

  val version = Version.LUCENE_46
  val indexPath = "test_index_dir"
  val analyzer = new MPSegmentAnalyzer

  @Before
  def setup {
    FileUtils.deleteDirectory(new File(indexPath))
  }

  @Test
  def should_retrieve_document_by_keyword {
    val writer = createIndexWriter

    writer addDocument createDocument("希尔伯特", "我们必须知道，我们将要知道！")
    writer addDocument createDocument("Hilbert", "We must know, we will know!")
    writer.close()

    val searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(indexPath))))

    val results = retrieve(searcher, "content", "知道")

    val numTotalHits = results.totalHits
    Assert.assertEquals(1, numTotalHits)

    val docId = results.scoreDocs(0).doc
    println(searcher.doc(docId))
  }


  def retrieve(searcher: IndexSearcher, field: String, keyword: String) = {
    val parser = new QueryParser(version, field, analyzer)
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
    val dir = FSDirectory.open(new File(indexPath))
    val iwc = new IndexWriterConfig(version, analyzer)
    iwc.setOpenMode(OpenMode.CREATE_OR_APPEND)

    new IndexWriter(dir, iwc)
  }
}
