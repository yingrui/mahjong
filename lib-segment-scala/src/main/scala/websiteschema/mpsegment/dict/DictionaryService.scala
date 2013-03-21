package websiteschema.mpsegment.dict;

import websiteschema.mpsegment.dict.domain.DomainDictFactory;
import websiteschema.mpsegment.dict.domain.DomainDictionary;

class DictionaryService(useDomainDictionary:Boolean, loadDomainDictionary:Boolean, loadUserDictionary:Boolean) {
    private var the2ndMatchWord : IWord = null
    private var the3rdMatchWord : IWord = null
    private var matchedWordCount = 0;

    private val hashDictionary = DictionaryFactory().getCoreDictionary();


    def lookup(candidateWord: String) : DictionaryLookupResult = {
        val firstWord = getItem(candidateWord)
        val result = new DictionaryLookupResult()
        result.firstMatchWord = firstWord;
        result.the2ndMatchWord = the2ndMatchWord;
        result.the3rdMatchWord = the3rdMatchWord;
        result.matchedWordCount = matchedWordCount;
        return result;
    }

    private def get1stMatchWord(words: Array[IWord]) : IWord = {
        if(null != words && words.length > 0) words(0) else null
    }

    private def get2ndMatchWord(words: Array[IWord]) : IWord = {
        if(null != words && words.length > 1) words(1) else null
    }

    private def get3rdMatchWord(words: Array[IWord]) : IWord = {
        if (null != words && words.length > 2) words(2) else null
    }

    private def getDomainDictionary() : DomainDictionary = {
        return DomainDictFactory().getDomainDictionary();
    }

    private def getMultiWordsInHashDictionary(candidateWord: String) : IWord = {
        var word: IWord = null
        val words = hashDictionary.getWords(candidateWord)
        matchedWordCount = if(null != words) words.length else 0
        if (null != words) {
            word = get1stMatchWord(words);
            if (word != null) {
                the2ndMatchWord = get2ndMatchWord(words);
                the3rdMatchWord = get3rdMatchWord(words);
            }
        }
        return word;
    }

    private def getItem(candidateWord: String) : IWord = {
        var word: IWord = null
        if (useDomainDictionary && (loadDomainDictionary || loadUserDictionary) && candidateWord.length() > 1) {
            word = getDomainDictionary().getWordItem(candidateWord);
        }
        if (word == null) {
            //如果在领域词典中没有找到对应的词
            //则在核心词典中继续查找
            word = getMultiWordsInHashDictionary(candidateWord);
        } else {
            //如果在领域词典中找到对应的词
            //继续在核心词典中查找，并排除在返回结果中和领域词典里相同的词
            val words = hashDictionary.getWords(candidateWord)
            matchedWordCount = if (null != words) words.length else 0;
            the2ndMatchWord = get1stMatchWord(words);
            if (the2ndMatchWord != null) {
                if (word.getWordName().length == the2ndMatchWord.getWordName().length) {
                    //如果用户辞典或领域辞典的长度和普通辞典相同
                    the2ndMatchWord = get2ndMatchWord(words);
                    the3rdMatchWord = get3rdMatchWord(words);
                    if (the2ndMatchWord != null) {
                        if (word.getWordName().length == the2ndMatchWord.getWordName().length) {
                            the2ndMatchWord = null;
                        }
                        if (the3rdMatchWord != null && word.getWordName().length() == the3rdMatchWord.getWordName().length()) {
                            the3rdMatchWord = null;
                        }
                        if (the2ndMatchWord == null) {
                            the2ndMatchWord = the3rdMatchWord;
                            the3rdMatchWord = null;
                        }
                    }
                } else {
                    //如果用户辞典或领域辞典的长度和普通辞典不相同
                    the3rdMatchWord = get2ndMatchWord(words);
                    if (the3rdMatchWord != null) {
                        if (word.getWordName().length == the3rdMatchWord.getWordName().length) {
                            the3rdMatchWord = get3rdMatchWord(words);
                        }
                        if (the3rdMatchWord != null && word.getWordName().length == the3rdMatchWord.getWordName().length) {
                            the3rdMatchWord = get3rdMatchWord(words);
                        }
                    }
                }
            }
        }
        return word;
    }
}
