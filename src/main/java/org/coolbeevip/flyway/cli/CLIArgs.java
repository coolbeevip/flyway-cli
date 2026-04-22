package org.coolbeevip.flyway.cli;

import com.beust.jcommander.Parameter;

public class CLIArgs {

    @Parameter(names = {"-h", "-help", "--help"}, help = true, description = "Show usage")
    public boolean help;

    @Parameter(names = "-jdbc_url", description = "JDBC URL", required = true)
    public String url;

    @Parameter(names = "-username", description = "database username", required = true)
    public String username;

    @Parameter(names = "-password", description = "database password")
    public String password;

    @Parameter(names = "-password_env", description = "environment variable name for database password")
    public String passwordEnv;

    @Parameter(names = "-table", description = "Schema table name")
    public String tableName = "schema_version";

    @Parameter(names = "-location", description = "database schema sql", required = true)
    public String location;

    @Parameter(names = "-baseline_on_migrate", description = "enable Flyway baselineOnMigrate")
    public boolean baselineOnMigrate;

    @Parameter(names = "-baseline_version", description = "baseline version used when baselineOnMigrate is enabled")
    public String baselineVersion = "0.0.0";

    @Parameter(names = "-group", description = "group all pending migrations in one transaction when supported")
    public boolean group = true;
}
