    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.tools;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
class WordHead implements Serializable {

    private String headWord; //首字词
    private int wordOccuredSum;//首字开头的词共出现了多少次
    private ArrayList<WordHeadNode> wordItems;
    private int sizePre;

    WordHead(String str, int j) {
        this.headWord = str;
        this.wordOccuredSum = j;
        wordItems = new ArrayList<WordHeadNode>();
    }

    public void addtemp(WordHeadNode temp) {
        this.wordItems.add(temp);
    }

    public int getSizePre() {
        return this.wordItems.size();
    }

    public void setSizePre(int sizePre) {

        this.sizePre = sizePre;
    }

    public ArrayList<WordHeadNode> getWordItems() {
        return wordItems;
    }

    public void setWordItems(ArrayList<WordHeadNode> wordItems) {
        this.wordItems = wordItems;
    }

    public String getHeadWord() {
        return headWord;
    }

    public void setHeadWord(String headWord) {
        this.headWord = headWord;
    }

    public int getWordOccuredSum() {
        return wordOccuredSum;
    }

    public void setWordOccuredSum(int wordOccuredSum) {
        this.wordOccuredSum = wordOccuredSum;
    }
}
