package org.coolbeevip.flyway.cli.application;


import com.beust.jcommander.Parameter;

public class CLIArgs {

    @Parameter(names = "-jdbc_url", description = "JDBC URL", required = true)
    public String url;

    @Parameter(names = "-username", description = "database username", required = true)
    public String username;

    @Parameter(names = "-password", description = "database password", required = true)
    public String password;

    @Parameter(names = "-table", description = "Schema table name")
    public String tableName = "schema_version";

    @Parameter(names = "-location", description = "database schema sql", required = true)
    public String location;
}
