package org.coolbeevip.flyway.cli.application;

import com.beust.jcommander.JCommander;
import org.flywaydb.core.Flyway;

/*
flyway-cli \
-jdbc_url jdbc:postgresql://10.19.83.184:5432/an_copilot_knowledge \
-username an_copilot_knowledge \
-password copilot#@!123 \
-location /Users/zhanglei/Work/github/flyway-cli/schema/postgresql


*/
public class CLI {
    public static void main(String[] args) {
        CLIArgs cliArgs = new CLIArgs();
        JCommander.newBuilder().addObject(cliArgs).build().parse(args);

        Flyway flyway = Flyway.configure()
                .table(cliArgs.tableName)
                .baselineOnMigrate(true)
                .group(true)
                .cleanDisabled(true)
                .baselineVersion("0.0.0.0")
                .locations("filesystem:"+cliArgs.location)
                .dataSource(cliArgs.url, cliArgs.username, cliArgs.password).load();
        flyway.migrate();
    }

}
