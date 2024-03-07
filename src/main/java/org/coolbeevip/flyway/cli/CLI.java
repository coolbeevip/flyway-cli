package org.coolbeevip.flyway.cli;

import com.beust.jcommander.JCommander;
import org.flywaydb.core.Flyway;

public class CLI {
    public static void main(String[] args) {
        CLIArgs cliArgs = new CLIArgs();
        JCommander.newBuilder().addObject(cliArgs).build().parse(args);

        Flyway flyway = Flyway.configure()
                .table(cliArgs.tableName)
                .baselineOnMigrate(true)
                .group(true)
                .cleanDisabled(true)
                .baselineVersion("0.0.0")
                .locations("filesystem:" + cliArgs.location)
                .dataSource(cliArgs.url, cliArgs.username, cliArgs.password).load();
        flyway.migrate();
    }

}
