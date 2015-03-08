package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

public class V001__Create_Table_User implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate.execute("CREATE TABLE USERS (" +
                "id SERIAL PRIMARY KEY," +
                "FirstName varchar(255)," +
                "LastName varchar(255)," +
                "Email varchar(255)," +
                "Password varchar(255)" +
                ")");

        jdbcTemplate.execute("INSERT INTO USERS (FirstName, LastName, Email, Password) VALUES ('Yingrui', 'Feng', 'yingrui.f@gmail.com', 'admin')");
    }
}
