package com.project.Event_Hub.Auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseConstraintCleaner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        try {
            // Find all unique indexes on column 'email' in table 'users'
            String sql = "SELECT DISTINCT INDEX_NAME FROM INFORMATION_SCHEMA.STATISTICS " +
                         "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'users' " +
                         "AND COLUMN_NAME = 'email' AND NON_UNIQUE = 0 AND INDEX_NAME != 'PRIMARY'";

            List<String> indexNames = jdbcTemplate.queryForList(sql, String.class);

            for (String indexName : indexNames) {
                try {
                    String dropSql = "ALTER TABLE users DROP INDEX " + indexName;
                    jdbcTemplate.execute(dropSql);
                    System.out.println("Successfully dropped unique constraint/index on email: " + indexName);
                } catch (Exception e) {
                    System.out.println("Could not drop index " + indexName + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("DatabaseConstraintCleaner execution failed: " + e.getMessage());
        }
    }
}
