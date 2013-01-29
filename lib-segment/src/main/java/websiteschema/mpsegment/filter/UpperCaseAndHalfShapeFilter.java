package websiteschema.mpsegment.filter;

import websiteschema.mpsegment.util.StringUtil;

public class UpperCaseAndHalfShapeFilter extends AbstractSegmentFilter {

    public UpperCaseAndHalfShapeFilter(boolean halfShapeAll, boolean upperCaseAll) {
        this.halfShapeAll = halfShapeAll;
        this.upperCaseAll = upperCaseAll;
        this.upperCaseOrHalfShapeAll = halfShapeAll || upperCaseAll;
    }

    @Override
    public void doFilter() {
        int length = segmentResult.length();
        for (int index = 0; index < length; index++) {
            String word = segmentResult.getWord(index);
            segmentResult.setWord(index, filterWord(word));
        }
    }

    private String filterWord(String word) {
        if (upperCaseOrHalfShapeAll) {
            if (halfShapeAll && upperCaseAll) {
                return StringUtil.doUpperCaseAndHalfShape(word);
            }
            if (halfShapeAll) {
                return StringUtil.halfShape(word);
            }
            return StringUtil.toUpperCase(word);
        }
        return word;
    }

    private boolean upperCaseOrHalfShapeAll;
    private boolean upperCaseAll;
    private boolean halfShapeAll;
}
