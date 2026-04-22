package org.coolbeevip.flyway.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CLITest {

    @TempDir
    Path tempDir;

    @Test
    void shouldRejectCustomBaselineVersionWithoutFlag() {
        CLIArgs args = new CLIArgs();
        args.url = "jdbc:sqlite:test.db";
        args.username = "";
        args.password = "";
        args.location = tempDir.toString();
        args.baselineVersion = "1.0.0";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> CLI.validate(args));
        assertTrue(exception.getMessage().contains("-baseline_version requires -baseline_on_migrate"));
    }

    @Test
    void shouldBuildConfigurationWithoutBaselineOnMigrateByDefault() {
        CLIArgs args = new CLIArgs();
        args.url = "jdbc:sqlite:test.db";
        args.username = "";
        args.password = "";
        args.location = tempDir.toString();

        CLI.validate(args);
        FluentConfiguration configuration = CLI.buildConfiguration(args);

        assertFalse(configuration.isBaselineOnMigrate());
        assertEquals("schema_version", configuration.getTable());
        assertNotNull(configuration.getDataSource());
    }

    @Test
    void shouldMigrateSqliteDatabase() throws Exception {
        Path migrationsDir = Files.createDirectories(tempDir.resolve("migrations"));
        Files.write(
                migrationsDir.resolve("V1.0.0__init.sql"),
                ("CREATE TABLE test_table (\n"
                        + "    id VARCHAR(36) PRIMARY KEY,\n"
                        + "    username VARCHAR(50) UNIQUE\n"
                        + ");\n"
                        + "INSERT INTO test_table (id, username) VALUES ('1', 'alice');\n")
                        .getBytes(StandardCharsets.UTF_8));

        Path databasePath = tempDir.resolve("test.sqlite");

        int exitCode = CLI.run(new String[]{
                "-jdbc_url", "jdbc:sqlite:" + databasePath.toAbsolutePath(),
                "-username", "",
                "-password", "",
                "-location", migrationsDir.toString()
        });

        assertEquals(0, exitCode);
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath.toAbsolutePath());
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM test_table");
             ResultSet resultSet = statement.executeQuery()) {
            assertTrue(resultSet.next());
            assertEquals(1, resultSet.getInt(1));
        }
    }
}
