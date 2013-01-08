package websiteschema.mpsegment.filter;

import websiteschema.mpsegment.lang.en.PorterStemmer;
import websiteschema.mpsegment.util.CharCheckUtil;

/**
 * EnglishStemFilter should not be used in search engine. <br/>
 * Because this filter will delete white space characters,<br/>
 * which many search engines already took it into account.
 */
public class EnglishStemFilter extends AbstractSegmentFilter {
    PorterStemmer porterStemmer = new PorterStemmer();

    @Override
    public void doFilter() {
        int length = segmentResult.length();
        for (int index = 0; index < length; index++) {
            String word = segmentResult.getWord(index);
            if (CharCheckUtil.isEnglish(word)) {
                segmentResult.setWord(index, porterStemmer.stem(word));
                deleteNextSpaceWord(index, length);
            }
        }
    }

    private void deleteNextSpaceWord(int index, int length) {
        for (int i = index + 1; i < length; i++) {
            if (CharCheckUtil.isWhiteSpace(segmentResult.getWord(i))) {
                deleteWordAt(i);
            } else {
                break;
            }
        }
    }
}
