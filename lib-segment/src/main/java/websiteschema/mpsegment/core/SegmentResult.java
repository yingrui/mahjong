package websiteschema.mpsegment.core;

import websiteschema.mpsegment.dict.POSUtil;

public class SegmentResult {

    public SegmentResult(int size) {
        initWordElements(size);
    }

    public void setWords(String words[]) {
        for(int i = 0; i < words.length; i++) {
            wordAtoms[i].word = words[i];
        }
    }

    private void initWordElements(int size) {
        wordAtoms = new WordAtom[size];
        for(int i = 0; i < size; i++) {
            wordAtoms[i] = new WordAtom();
        }
    }

    public void setPOSArray(int tags[]) {
        for(int i = 0; i < tags.length; i++) {
            wordAtoms[i].pos = tags[i];
        }
    }

    public void setConcepts(int marks[]) {
        for(int i = 0; i < marks.length; i++) {
            wordAtoms[i].concept = marks[i];
        }
    }

    public String getWord(int i) {
        return wordAtoms[i].word;
    }

    public int getWordIndexInOriginalString(int index) {
        int wordIndexInOriginalString = 0;
        for(int i = 0; i < index; i++) {
            wordIndexInOriginalString += wordAtoms[i].word.length();
        }
        return wordIndexInOriginalString;
    }

    public int getPOS(int i) {
        return wordAtoms[i].pos;
    }

    public int getConcept(int i) {
        return wordAtoms[i].concept;
    }

    public int length() {
        if (wordAtoms != null) {
            return wordAtoms.length;
        } else {
            return 0;
        }
    }

    public WordAtom getWordAtom(int i) {
        return wordAtoms[i];
    }

    private int compactLength() {
        if (wordAtoms != null) {
            int i = wordAtoms.length;
            if (i > 1 && wordAtoms[i - 1].word.length() <= 0) {
                int j;
                for (j = i - 1; j > 1; j--) {
                    if (wordAtoms[j].word.length() > 0) {
                        break;
                    }
                }
                i = j + 1;
            }
            return i;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder retString = new StringBuilder();
        int length = compactLength();
        for (int j = 0; j < length; j++) {
            retString.append(getWord(j)).append("/").append(POSUtil.getPOSString(getPOS(j))).append(" ");
        }

        return retString.toString();
    }

    public String toOriginalString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < length(); i ++) {
            stringBuilder.append(getWord(i));
        }
        return stringBuilder.toString();
    }

    public void setWord(int index, String word, int tag) {
        wordAtoms[index].word = word;
        wordAtoms[index].pos = tag;
        wordAtoms[index].concept = 0;
    }

    public void setPOS(int index, int pos) {
        wordAtoms[index].pos = pos;
    }

    public void letWord1EqualWord2(int wordIndex1, int wordIndex2) {
        wordAtoms[wordIndex1].word = wordAtoms[wordIndex2].word;
        wordAtoms[wordIndex1].pos = wordAtoms[wordIndex2].pos;
        wordAtoms[wordIndex1].concept = wordAtoms[wordIndex2].concept;
    }

    public void cutTail(final int end) {
        WordAtom[] atoms = new WordAtom[end];
        for (int i = 0; i < end; i++) {
            atoms[i] = wordAtoms[i];
        }
        this.wordAtoms = atoms;
    }

    public void append(SegmentResult segmentResult) {
        int total = length() + segmentResult.length();
        WordAtom[] atoms = new WordAtom[total];

        for (int i = 0; i < length(); i++) {
            atoms[i] = wordAtoms[i];
        }

        for (int i = length(); i < total; i++) {
            atoms[i] = segmentResult.getWordAtom(i - length());
        }
        wordAtoms = atoms;
    }

    private WordAtom[] wordAtoms;
}
