package websiteschema.mpsegment.core;

import websiteschema.mpsegment.dict.POSUtil;

import java.util.ArrayList;
import java.util.List;

public class SegmentResult {

    public SegmentResult(int size) {
        initWordElements(size);
    }

    private void initWordElements(int size) {
        wordAtoms = new ArrayList<WordAtom>(size);
        for (int i = 0; i < size; i++) {
            wordAtoms.add(new WordAtom());
        }
    }

    public void setWords(String words[]) {
        for (int i = 0; i < words.length; i++) {
            wordAtoms.get(i).word = words[i];
        }
    }

    public void setPOSArray(int tags[]) {
        for (int i = 0; i < tags.length; i++) {
            wordAtoms.get(i).pos = tags[i];
        }
    }

    public void setDomainTypes(int marks[]) {
        for (int i = 0; i < marks.length; i++) {
            wordAtoms.get(i).domainType = marks[i];
        }
    }

    public void setConcepts(String[] concepts) {
        for (int i = 0; i < concepts.length; i++) {
            wordAtoms.get(i).concept = concepts[i];
        }
    }

    public String getWord(int i) {
        return wordAtoms.get(i).word;
    }

    public int getWordIndexInOriginalString(int index) {
        int wordIndexInOriginalString = 0;
        for (int i = 0; i < index; i++) {
            wordIndexInOriginalString += wordAtoms.get(i).word.length();
        }
        return wordIndexInOriginalString;
    }

    public String getPinyin(int i) {
        return wordAtoms.get(i).pinyin;
    }

    public int getPOS(int i) {
        return wordAtoms.get(i).pos;
    }

    public String getConcept(int i) {
        return wordAtoms.get(i).concept;
    }

    public int getDomainType(int i) {
        return wordAtoms.get(i).domainType;
    }

    public int length() {
        if (wordAtoms != null) {
            return wordAtoms.size();
        } else {
            return 0;
        }
    }

    public WordAtom getWordAtom(int i) {
        return wordAtoms.get(i);
    }

    public void setPOS(int index, int pos) {
        wordAtoms.get(index).pos = pos;
    }

    public void setConcept(int index, String concept) {
        wordAtoms.get(index).concept = concept;
    }

    public void setPinyin(int index, String pinyin) {
        wordAtoms.get(index).pinyin = pinyin;
    }

    public void append(SegmentResult segmentResult) {
        this.wordAtoms.addAll(segmentResult.wordAtoms);
    }

    public void merge(int start, int end, int pos) {
        WordAtom wordAtom = mergedWordAtom(start, end, pos);
        if (null != wordAtom) {
            for (int i = start + 1; i <= end; i++) {
                wordAtoms.set(i, null);
            }
        }
    }

    public void compact() {
        int i = 0;
        while (i < length()) {
            if (wordAtoms.get(i) == null) {
                wordAtoms.remove(i);
            } else {
                i++;
            }
        }
    }

    public String toOriginalString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            stringBuilder.append(getWord(i));
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        StringBuilder retString = new StringBuilder();
        for (int j = 0; j < length(); j++) {
            retString.append(getWord(j)).append("/").append(POSUtil.getPOSString(getPOS(j))).append(" ");
        }

        return retString.toString();
    }

    private WordAtom mergedWordAtom(int start, int end, int pos) {
        StringBuilder wordName = new StringBuilder();
        StringBuilder pinyin = new StringBuilder();
        WordAtom startWordAtom = getStartWordAtom(start);
        appendWordAndPinyin(wordName, pinyin, startWordAtom);
        for (int i = start + 1; i <= end; i++) {
            WordAtom wordAtom = wordAtoms.get(i);
            if (wordAtom != null) {
                appendWordAndPinyin(wordName, pinyin, wordAtom);
            }
        }
        startWordAtom.word = wordName.toString();
        startWordAtom.pinyin = pinyin.toString();
        startWordAtom.pos = pos;
        return startWordAtom;
    }

    private void appendWordAndPinyin(StringBuilder wordName, StringBuilder pinyin, WordAtom startWordAtom) {
        wordName.append(startWordAtom.word);
        if (null != startWordAtom.pinyin) pinyin.append(startWordAtom.pinyin);
    }

    private WordAtom getStartWordAtom(int index) {
        WordAtom wordAtom = getWordAtom(index);
        if (null != wordAtom) {
            return wordAtom;
        } else {
            return lookupWordAtomBefore(index);
        }
    }

    private WordAtom lookupWordAtomBefore(int index) {
        for (int i = index - 1; i >= 0; i--) {
            WordAtom wordAtom = getWordAtom(i);
            if (null != wordAtom) {
                return wordAtom;
            }
        }
        return null;
    }

    private List<WordAtom> wordAtoms;
}
