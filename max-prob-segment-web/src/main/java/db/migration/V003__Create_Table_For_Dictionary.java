package db.migration;

import com.googlecode.flyway.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

public class V003__Create_Table_For_Dictionary implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        createTableForPartOfSpeech(jdbcTemplate);

        createTableForPinyin(jdbcTemplate);

        createTableForConcept(jdbcTemplate);

        createTableForWordItems(jdbcTemplate);
    }

    private void createTableForWordItems(JdbcTemplate jdbcTemplate) {
        System.out.println("createTableForWordItems:");
        jdbcTemplate.execute("CREATE TABLE WordItems (" +
                "Id SERIAL PRIMARY KEY," +
                "Name varchar(255)," +
                "Type varchar(255)," +
                "CreateBy integer," +
                "CreateAt timestamp," +
                "LastModifiedBy integer," +
                "LastModifiedAt timestamp" +
                ")");

        jdbcTemplate.execute("ALTER TABLE WordItems ADD UNIQUE (Name)");
        System.out.println("created Table: WordItems");
        new WordItemCreator().createWordItems(jdbcTemplate);
        System.out.println("created all WordItems");
    }

    private void createTableForConcept(JdbcTemplate jdbcTemplate) {
        System.out.println("createTableForConcept:");
        jdbcTemplate.execute("CREATE TABLE Concepts (" +
                "Id SERIAL PRIMARY KEY," +
                "ParentId integer NULL," +
                "Name varchar(255)," +
                "PartOfSpeech integer," +
                "Note varchar(255)," +
                "CreateAt timestamp" +
                ")");
        jdbcTemplate.execute("CREATE TABLE WordConcept (" +
                "WordId integer," +
                "ConceptId integer" +
                ")");
        System.out.println("created Table: Concepts, WordConcept");
        new ConceptCreator().createConcepts(jdbcTemplate);
        System.out.println("created all Concepts");
    }

    private void createTableForPinyin(JdbcTemplate jdbcTemplate) {
        System.out.println("createTableForPinyin:");
        jdbcTemplate.execute("CREATE TABLE Pinyins (" +
                "Id SERIAL PRIMARY KEY," +
                "WordId integer," +
                "Name varchar(255)" +
                ")");
        System.out.println("created Table: Pinyin");
    }

    private void createTableForPartOfSpeech(JdbcTemplate jdbcTemplate) {
        System.out.println("createTableForPartOfSpeech:");
        jdbcTemplate.execute("CREATE TABLE PartOfSpeeches (" +
                "Id integer," +
                "Name varchar(255)," +
                "Note varchar(255)," +
                "CreateAt timestamp" +
                ")");
        jdbcTemplate.execute("CREATE TABLE WordFreq (" +
                "Id SERIAL PRIMARY KEY," +
                "WordId integer," +
                "PartOfSpeechId integer," +
                "Freq integer" +
                ")");
        System.out.println("created Table: PartOfSpeech, WordFreq");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (1, 'N', '名词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (2, 'T', '时间词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (3, 'S', '处所词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (4, 'F', '方位词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (5, 'M', '数词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (6, 'Q', '量词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (7, 'B', '区别词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (8, 'R', '代词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (9, 'V', '动词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (10, 'A', '形容词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (11, 'Z', '状态词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (12, 'D', '副词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (13, 'P', '介词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (14, 'C', '连词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (15, 'U', '其他助词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (16, 'Y', '语气词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (17, 'E', '叹词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (18, 'O', '拟声词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (19, 'I', '成语', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (20, 'L', '习用语', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (21, 'J', '简称略语', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (22, 'H', '前接成分', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (23, 'K', '后接成分', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (24, 'G', '语素', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (25, 'X', '非语素字', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (26, 'W', '其他标点符号', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (27, 'NR', '人名', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (28, 'NS', '地名', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (29, 'NT', '机构团体', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (30, 'NZ', '其他专名', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (31, 'NX', '外文词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (33, 'NG', '名语素', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (34, 'VG', '动语素', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (35, 'AG', '形语素', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (36, 'TG', '时语素', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (37, 'DG', '副语素', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (38, 'OG', '拟声语素', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (39, 'AUX', '虚词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (40, 'VN', '名动词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (41, 'AN', '名形词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (42, 'VD', '副动词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (43, 'AD', '副形词', 'now')");
        jdbcTemplate.execute("INSERT INTO PartOfSpeeches (Id, Name, Note, CreateAt) VALUES (44, 'UN', '未登录词', 'now')");
    }
}
