package websiteschema.mpsegment.conf;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public final class MPSegmentConfiguration {

    private static Logger l = Logger.getLogger("segment");
    private static final MPSegmentConfiguration INSTANCE = new MPSegmentConfiguration();
    private static int SECTION_SIZE = 1024;
    private Properties properties;

    private MPSegmentConfiguration() {
        properties = new Properties();
        loadFromConfiguration();
        initialize();
    }

    private void loadFromConfiguration() {
        try {
            l.debug("load maxprob-default.cfg: " + MPSegmentConfiguration.class.getClassLoader().getResource("maxprob-default.cfg"));
            properties.load(MPSegmentConfiguration.class.getClassLoader().getResourceAsStream("maxprob-default.cfg"));
            if (null != MPSegmentConfiguration.class.getClassLoader().getResource("maxprob.cfg")) {
                l.debug("load maxprob.cfg: " + MPSegmentConfiguration.class.getClassLoader().getResource("maxprob.cfg"));
                properties.load(MPSegmentConfiguration.class.getClassLoader().getResourceAsStream("maxprob.cfg"));
            }
        } catch (IOException ex) {
            l.error("error when loading Chinese NLP INSTANCE files", ex);
        }
    }

    public MPSegmentConfiguration(Map<String, String> config) {
        properties = new Properties(INSTANCE.properties);
        for(String key : config.keySet()) {
            properties.put(key, config.get(key));
        }
        initialize();
    }

    public static MPSegmentConfiguration getINSTANCE() {
        return INSTANCE;
    }

    private String getHomePath() {
        return homePath;
    }

    public static int SectionSize() {
        return SECTION_SIZE;
    }

    public void setHomePath(String path) {
        int i1 = path.length();
        if (i1 >= 1) {
            if (path.charAt(i1 - 1) == '/' || path.charAt(i1 - 1) == '\\') {
                homePath = path;
            } else {
                homePath = (new StringBuilder(String.valueOf(path))).append("/").toString();
            }
        }
    }

    private void initialize() {
        stopwordfilter = Boolean.valueOf(properties.getProperty("segment.stopwordfilter", "false")).booleanValue();
        querysyntax = Boolean.valueOf(properties.getProperty("segment.querysyntax", "false")).booleanValue();
        loaduserdictionary = Boolean.valueOf(properties.getProperty("user.loaduserdictionary", "false")).booleanValue();
        loaddomaindictionary = Boolean.valueOf(properties.getProperty("domain.loaddomaindictionary", "true")).booleanValue();
        segment_min = Boolean.valueOf(properties.getProperty("segment.min", "false")).booleanValue();
        chinesenameidentify = Boolean.valueOf(properties.getProperty("segment.chinesenameidentify", "true")).booleanValue();
        xingmingseparate = Boolean.valueOf(properties.getProperty("segment.xingmingseparate", "true")).booleanValue();
        halfshapeall = Boolean.valueOf(properties.getProperty("segment.halfshapeall", "false")).booleanValue();
        uppercaseall = Boolean.valueOf(properties.getProperty("segment.uppercaseall", "false")).booleanValue();
        ExtendPOSInDomainDictionary = Boolean.valueOf(properties.getProperty("domain.extendposindomaindictionary", "false")).booleanValue();
    }

    public String getDefaultFileEncoding() {
        return defaultFileEncoding;
    }

    public String getSegmentDict() {
        return new StringBuilder(getHomePath()).
                append(properties.getProperty("cnnlp.lexical.segment.MPSegment", "segment.dict")).toString();
    }

    public String getPOSMatrix() {
        return new StringBuilder(getHomePath()).
                append(properties.getProperty("cnnlp.lexical.segment.POSTagging", "pos.dat")).toString();
    }

    public String getNamePOSMatrix() {
        return new StringBuilder(getHomePath()).
                append(properties.getProperty("cnnlp.lexical.segment.NameTagging", "NamePOSMatrix.fre")).toString();
    }

    public String getChNameDict() {
        return new StringBuilder(getHomePath()).
                append(properties.getProperty("cnnlp.lexical.segment.ChNameDict", "ChName.dict")).toString();
    }

    public boolean isLoadDomainDictionary() {
        return loaddomaindictionary;
    }

    public boolean isLoadUserDictionary() {
        return loaduserdictionary;
    }

    public String getUserDictionaryFile() {
        return new StringBuilder(getHomePath()).append(properties.getProperty("user.dictionaryfile", "userdict.txt")).toString();
    }

    public boolean isSupportQuerySyntax() {
        return querysyntax;
    }

    public String getStopPosList() {
        return properties.getProperty("user.stoplist", "");
    }

    public String getStopWordFile() {
        return new StringBuilder(getHomePath()).append(properties.getProperty("user.stopwordfile", "")).toString();
    }

    public boolean isSegmentMin() {
        return segment_min;
    }

    public void setSegmentMin(boolean segmentMin) {
        this.segment_min = segmentMin;
    }

    public String getGlueChar() {
        String gluechar = properties.getProperty("segment.gluechar", "*?~/_[]:");
        return gluechar;
    }

    public int getMaxQueryLength() {
        int maxquerylength = Integer.parseInt(properties.getProperty("segment.maxquerylength", "512").trim());
        return maxquerylength;
    }

    public boolean isFilterStopWord() {
        return stopwordfilter;
    }

    public boolean isChineseNumberToDigital() {
        return chinesenumbertodigital;
    }

    public boolean isChineseNameIdentify() {
        return chinesenameidentify;
    }

    public boolean isXingMingSeparate() {
        return xingmingseparate;
    }

    public void setXingmingSeparate(boolean separate) {
        xingmingseparate = separate;
    }

    public boolean isHalfShapeAll() {
        return halfshapeall;
    }

    public boolean isUpperCaseAll() {
        return uppercaseall;
    }

    public int getMaxWordLength() {
        int maxwordlength = Integer.parseInt(properties.getProperty("segment.maxwordlength", "8").trim());
        return maxwordlength;
    }

    public String getDomainDictLoader() {
        String domaindictloader = properties.getProperty("domain.dictloader", "");
        return domaindictloader;
    }

    public boolean isExtendPOSInDomainDictionary() {
        return ExtendPOSInDomainDictionary;
    }

    private String homePath = "";
    private String defaultFileEncoding = "gbk";
    private boolean loaddomaindictionary = false;
    private boolean loaduserdictionary = false;
    private boolean segment_min = false;
    private boolean querysyntax = false;
    //TODO: add test case for stop word filter.
    private boolean stopwordfilter = false;
    private boolean chinesenumbertodigital = false;
    private boolean chinesenameidentify = true;
    private boolean xingmingseparate = true;
    private boolean halfshapeall = false;
    private boolean uppercaseall = false;
    //领域词典中的词是否扩展其在普通词典中的词性
    private boolean ExtendPOSInDomainDictionary = false;
    public final static double LOG_CORPUS = Math.log(8000000) * 100;
}
