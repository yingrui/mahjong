package websiteschema.mpsegment.filter;

import websiteschema.mpsegment.util.CharCheckUtil;

public class ReduplicatingFilter extends AbstractSegmentFilter {

    @Override
    public void doFilter() {
        int length = segmentResult.length();
        for (int index = 1; index < length; index++) {
            String lastWord = getWord(index - 1);
            String word = getWord(index);
            if (isChineseWord(lastWord) && lastWord.equals(word)) {
                if (index - 2 >= 0) {
                    if (getWord(index - 2).equals("一")) {
                        setWordIndexesAndPOSForMerge(index - 2, index, segmentResult.getPOS(index - 1));
                    }
                } else {
                    setWordIndexesAndPOSForMerge(index - 1, index, segmentResult.getPOS(index - 1));
                }
            }

            if(word.length() == 2 && word.charAt(0) == word.charAt(1)) {
                if(lastWord.equals("一")) {
                    setWordIndexesAndPOSForMerge(index - 1, index, segmentResult.getPOS(index));
                }
            }
        }
    }

    private boolean isChineseWord(String lastWord) {
        return lastWord.length() == 1 && CharCheckUtil.isChinese(lastWord);
    }

    private String getWord(int i) {
        return segmentResult.getWord(i);
    }
}
