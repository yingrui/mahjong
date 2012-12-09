package websiteschema.mpsegment.filter;

public class ReduplicatingFilter extends AbstractSegmentFilter {

    @Override
    public void doFilter() {
        int length = segmentResult.length();
        for (int i = 1; i < length; i++) {
            String lastWord = getWord(i - 1);
            if (lastWord.length() == 1 && lastWord.equals(getWord(i))) {
                if (i - 2 >= 0) {
                    if (getWord(i - 2).equals("一")) {
                        setWordIndexesAndPOSForMerge(i - 2, i, segmentResult.getPOS(i - 1));
                    }
                } else {
                    setWordIndexesAndPOSForMerge(i - 1, i, segmentResult.getPOS(i - 1));
                }
            }
        }
    }

    private String getWord(int i) {
        return segmentResult.getWord(i);
    }
}
