package websiteschema.mpsegment.dict;

import java.util.Iterator;

public interface IDictionary {

    public IWord getWord(String wordStr);

    public IWord[] getWords(String wordStr);

    public Iterator<IWord> iterator();

}
