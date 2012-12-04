package websiteschema.mpsegment.tools;

import websiteschema.mpsegment.concept.Concept;
import websiteschema.mpsegment.dict.IWord;
import websiteschema.mpsegment.dict.POSUtil;

public class WordStringConverter {

    private StringBuilder stringBuilder = new StringBuilder();
    private IWord word;

    public WordStringConverter(IWord word) {
        this.word = word;
    }

    public String convertToString() {
        buildHead();
        buildBody();
        return stringBuilder.toString();
    }

    private void buildBody() {
        stringBuilder.append("{");
        stringBuilder.append(buildDomainType()).append(",");
        String posTable = buildPOSTable();
        if (!posTable.isEmpty()) {
            stringBuilder.append(posTable).append(",");
        }
        stringBuilder.append(buildConcepts());
        if (endWith(',')) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append("}");
    }

    private boolean endWith(char ch) {
        return stringBuilder.charAt(stringBuilder.length() - 1) == ch;
    }

    private String buildPOSTable() {
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

    private String buildDomainType() {
        return "domainType:" + word.getDomainType();
    }

    private String buildConcepts() {
        return buildConceptArray(word.getConcepts());
    }

    private String buildConceptArray(Concept[] concepts) {
        StringBuilder stringBuilder = new StringBuilder();
        if (null != concepts && concepts.length > 0 && concepts[0] != Concept.UNKNOWN) {
            stringBuilder.append("concepts:");
            stringBuilder.append("[");
            for (Concept concept : concepts) {
                stringBuilder.append(concept.getName()).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("]");
        }
        return stringBuilder.toString();
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
