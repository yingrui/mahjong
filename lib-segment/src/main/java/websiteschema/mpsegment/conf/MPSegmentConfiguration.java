package websiteschema.mpsegment.conf;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public final class MPSegmentConfiguration {


    private MPSegmentConfiguration() {
        properties = new Properties();
        loadFromConfiguration();
        initialize();
    }

    private void loadFromConfiguration() {
        try {
            System.out.println("load maxprob-default.cfg: " + MPSegmentConfiguration.class.getClassLoader().getResource("maxprob-default.cfg"));
            properties.load(MPSegmentConfiguration.class.getClassLoader().getResourceAsStream("maxprob-default.cfg"));
            if (null != MPSegmentConfiguration.class.getClassLoader().getResource("maxprob.cfg")) {
                System.out.println("load maxprob.cfg: " + MPSegmentConfiguration.class.getClassLoader().getResource("maxprob.cfg"));
                properties.load(MPSegmentConfiguration.class.getClassLoader().getResourceAsStream("maxprob.cfg"));
            }
        } catch (IOException ex) {
            System.err.println("error when loading Chinese NLP instance files: " + ex.getMessage());
        }
    }

    public MPSegmentConfiguration(Map<String, String> config) {
        properties = new Properties(instance.properties);
        if (null != config) {
            for (String key : config.keySet()) {
                properties.put(key, config.get(key));
            }
        }
        initialize();
    }

    public static MPSegmentConfiguration getInstance() {
        return instance;
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
        stopwordfilter = Boolean.valueOf(properties.getProperty("filter.stopword", "false")).booleanValue();
        querysyntax = Boolean.valueOf(properties.getProperty("support.querysyntax", "false")).booleanValue();
        loaduserdictionary = Boolean.valueOf(properties.getProperty("load.userdictionary", "false")).booleanValue();
        loaddomaindictionary = Boolean.valueOf(properties.getProperty("load.domaindictionary", "true")).booleanValue();
        segment_min = Boolean.valueOf(properties.getProperty("minimize.word", "false")).booleanValue();
        chinesenameidentify = Boolean.valueOf(properties.getProperty("recognize.chinesename", "true")).booleanValue();
        xingmingseparate = Boolean.valueOf(properties.getProperty("separate.xingming", "true")).booleanValue();
        halfshapeall = Boolean.valueOf(properties.getProperty("convert.tohalfshape", "false")).booleanValue();
        uppercaseall = Boolean.valueOf(properties.getProperty("convert.touppercase", "false")).booleanValue();
        withPinyin = Boolean.valueOf(properties.getProperty("recognize.pinyin", "false")).booleanValue();
    }

    public boolean is(String property) {
        return Boolean.valueOf(properties.getProperty(property));
    }

    public String getDefaultFileEncoding() {
        return defaultFileEncoding;
    }

    public String getPOSMatrix() {
        return new StringBuilder(getHomePath()).
                append(properties.getProperty("resource.pos", "pos.dat")).toString();
    }

    public String getChNameDict() {
        return new StringBuilder(getHomePath()).
                append(properties.getProperty("resource.chinesename", "ChName.dict")).toString();
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
        return properties.getProperty("filter.wordbypos", "");
    }

    public boolean isSegmentMin() {
        return segment_min;
    }

    public String getGlueChar() {
        return properties.getProperty("glue.queryoperater", "*?~/_[]:");
    }

    public int getMaxQueryLength() {
        return Integer.parseInt(properties.getProperty("maximum.querylength", "512").trim());
    }

    public boolean isChineseNameIdentify() {
        return chinesenameidentify;
    }

    public boolean isXingMingSeparate() {
        return xingmingseparate;
    }

    public boolean isHalfShapeAll() {
        return halfshapeall;
    }

    public boolean isUpperCaseAll() {
        return uppercaseall;
    }

    public int getMaxWordLength() {
        return Integer.parseInt(properties.getProperty("maximum.wordlength", "8").trim());
    }

    public String getDomainDictLoader() {
        return properties.getProperty("dictionary.loaders", "");
    }

    public boolean isWithPinyin() {
        return withPinyin;
    }

    public String getPinyinModel() {
        return properties.getProperty("pinyin.model", "wtp.m");
    }

    private static final MPSegmentConfiguration instance = new MPSegmentConfiguration();
    private static int SECTION_SIZE = 1024;
    private Properties properties;
    private String homePath = "";
    private String defaultFileEncoding = "gbk";
    private boolean withPinyin = false;
    private boolean loaddomaindictionary = false;
    private boolean loaduserdictionary = false;
    private boolean segment_min = false;
    private boolean querysyntax = false;
    //TODO: add test case for stop word filter.
    private boolean stopwordfilter = false;
    private boolean chinesenameidentify = true;
    private boolean xingmingseparate = true;
    private boolean halfshapeall = false;
    private boolean uppercaseall = false;
    public final static double LOG_CORPUS = Math.log(8000000) * 100;
}
