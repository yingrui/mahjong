//package websiteschema.mpsegment.filter;
//
//import websiteschema.mpsegment.util.StringUtil;
//
//class UpperCaseAndHalfShapeFilter extends AbstractSegmentFilter {
//
//    public UpperCaseAndHalfShapeFilter(Boolean halfShapeAll, Boolean upperCaseAll) {
//        this.halfShapeAll = halfShapeAll;
//        this.upperCaseAll = upperCaseAll;
//        this.upperCaseOrHalfShapeAll = halfShapeAll || upperCaseAll;
//    }
//
//    override def doFilter() {
//        var length = segmentResult.length()
//        for (Int index = 0; index < length; index++) {
//            var word = segmentResult.getWord(index)
//            segmentResult.setWord(index, filterWord(word));
//        }
//    }
//
//    private def filterWord(word: String) : String = {
//        if (upperCaseOrHalfShapeAll) {
//            if (halfShapeAll && upperCaseAll) {
//                return StringUtil.doUpperCaseAndHalfShape(word);
//            }
//            if (halfShapeAll) {
//                return StringUtil.halfShape(word);
//            }
//            return StringUtil.toUpperCase(word);
//        }
//        return word;
//    }
//
//    private var upperCaseOrHalfShapeAll : Boolean = null
//    private var upperCaseAll : Boolean = null
//    private var halfShapeAll : Boolean = null
//}
