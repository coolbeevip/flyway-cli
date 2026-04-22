package org.coolbeevip.flyway.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import java.io.File;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;

public class CLI {
    public static void main(String[] args) {
        System.exit(run(args));
    }

    static int run(String[] args) {
        CLIArgs cliArgs = new CLIArgs();
        JCommander commander = JCommander.newBuilder()
                .programName("flyway-cli")
                .addObject(cliArgs)
                .build();

        try {
            commander.parse(args);
            if (cliArgs.help) {
                commander.usage();
                return 0;
            }

            validate(cliArgs);
            migrate(cliArgs);
            return 0;
        } catch (ParameterException | IllegalArgumentException ex) {
            System.err.println("Argument error: " + ex.getMessage());
            commander.usage();
            return 2;
        } catch (Exception ex) {
            System.err.println("Migration failed: " + ex.getMessage());
            return 1;
        }
    }

    static void migrate(CLIArgs cliArgs) {
        buildConfiguration(cliArgs).load().migrate();
    }

    static FluentConfiguration buildConfiguration(CLIArgs cliArgs) {
        FluentConfiguration configuration = Flyway.configure()
                .table(cliArgs.tableName)
                .baselineOnMigrate(cliArgs.baselineOnMigrate)
                .group(cliArgs.group)
                .cleanDisabled(true)
                .locations("filesystem:" + cliArgs.location)
                .dataSource(cliArgs.url, cliArgs.username, resolvePassword(cliArgs));
        if (cliArgs.baselineOnMigrate) {
            configuration.baselineVersion(cliArgs.baselineVersion);
        }
        return configuration;
    }

    static String resolvePassword(CLIArgs cliArgs) {
        if (cliArgs.password != null) {
            return cliArgs.password;
        }
        if (cliArgs.passwordEnv != null) {
            String envPassword = System.getenv(cliArgs.passwordEnv);
            if (envPassword == null) {
                throw new IllegalArgumentException("Environment variable not found: " + cliArgs.passwordEnv);
            }
            return envPassword;
        }
        throw new IllegalArgumentException("One of -password or -password_env is required");
    }

    static void validate(CLIArgs cliArgs) {
        File location = new File(cliArgs.location);
        if (!location.exists() || !location.isDirectory()) {
            throw new IllegalArgumentException(
                    "Migration location does not exist or is not a directory: " + cliArgs.location);
        }
        if (!cliArgs.baselineOnMigrate && cliArgs.baselineVersion != null && !"0.0.0".equals(cliArgs.baselineVersion)) {
            throw new IllegalArgumentException("-baseline_version requires -baseline_on_migrate");
        }
    }
}
