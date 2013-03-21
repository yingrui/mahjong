package websiteschema.mpsegment.dict.domain

import websiteschema.mpsegment.conf.MPSegmentConfiguration

object DomainDictFactory{
  val instance = new DomainDictFactory()

  def apply() = instance
}

class DomainDictFactory {

    private var domainDict: DomainDictionary = null;
    private val config : MPSegmentConfiguration = MPSegmentConfiguration();
    private val loadDomainDictionary : Boolean = config.isLoadDomainDictionary();

    def buildDictionary() {
        if (loadDomainDictionary) {
            val dict = new DomainDictionary();
            initializeDictionaryLoaderThenLoad(dict);
            domainDict = dict;
        } else {
            domainDict = new DomainDictionary();
        }
    }

    def getDomainDictionary() : DomainDictionary = {
        return domainDict;
    }

    def initializeDictionaryLoaderThenLoad(dict: DomainDictionary) {
        try {
            initializeAndLoad(dict);
        } catch {
          case ex: Throwable =>
            System.err.println("Exception thrown when load domain dictionary. " + ex.getMessage());
        }
    }

    private def initializeAndLoad(dict: DomainDictionary) {
        val classNames = config.getDomainDictLoader().split("[,; ]+");
        for (className <- classNames) {
            val loader = createDictLoader(className);
            if (null != loader) {
                loader.load(dict);
            }
        }
    }

    private def createDictLoader(className: String) : DomainDictLoader = {
        try {
            val clazz = Class.forName(className);
            val loader = clazz.newInstance();
            return loader.asInstanceOf[DomainDictLoader];
        } catch {
          case ex: Throwable =>
            ex.printStackTrace()
        }
        return null;
    }
}
