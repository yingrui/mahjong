package websiteschema.mpsegment.tools;

import websiteschema.mpsegment.dict.IWord;
import websiteschema.mpsegment.dict.POSUtil;

//TODO: serialize concept info
public class WordStringConverter {

    private StringBuilder stringBuilder = new StringBuilder();
    private IWord word;

    public WordStringConverter(IWord word) {
        this.word = word;
    }

    public String convertToString() {
        buildHead();
        buildBody(getDomainType(), getPOSTable());
        return stringBuilder.toString();
    }

    private void buildBody(String domainType, String posTable) {
        stringBuilder.append("{");
        stringBuilder.append(domainType).append(",");
        if(!posTable.isEmpty()) stringBuilder.append(posTable).append(",");
        if(endWith(',')) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append("}");
    }

    private boolean endWith(char ch) {
        return stringBuilder.charAt(stringBuilder.length() - 1) == ch;
    }

    private String getPOSTable() {
        StringBuilder stringBuilder = new StringBuilder();
        int[][] POSTable = word.getWordPOSTable();
        if (null != POSTable && POSTable.length > 0) {
            stringBuilder.append("POSTable:{");
            for (int[] POSAndFreq : POSTable) {
                String POS = POSUtil.getPOSString(POSAndFreq[0]);
                int freq = POSAndFreq[1];
                stringBuilder.append(POS).append(":").append(freq).append(",");
            }
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            stringBuilder.append("}");
        }
        return stringBuilder.toString();
    }

    private String getDomainType() {
        return "domainType:" + word.getDomainType();
    }

    private void buildHead() {
        String wordName = word.getWordName();
        wordName = wordName.replaceAll("\"", "%22");
        wordName = wordName.replaceAll("\\(", "%28");
        wordName = wordName.replaceAll("\\)", "%29");
        wordName = wordName.replaceAll("\\[", "%5B");
        wordName = wordName.replaceAll("\\]", "%5D");
        wordName = wordName.replaceAll("\\{", "%7B");
        wordName = wordName.replaceAll("\\}", "%7D");
        stringBuilder.append("\"").append(wordName).append("\" = ");
    }

}
