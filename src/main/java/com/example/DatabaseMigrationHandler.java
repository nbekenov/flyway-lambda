package com.example;

import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Objects;
import software.amazon.jdbc.PropertyDefinition;
import software.amazon.jdbc.ds.AwsWrapperDataSource;

public class DatabaseMigrationHandler {
    // instance vars
    private final String dbHost;
    private final String dbPort;
    private final String dbName;
    private final String dbSchema;
    private final String dbUser;
    private final String dbPassword;

    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "5432";
    private static final String DB_NAME = "postgres";
    private static final String DB_SCHEMA = "myschema";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "mysecretpassword";

    // Constructor
    public DatabaseMigrationHandler() {
        this.dbHost = DB_HOST;
        this.dbPort = DB_PORT;
        this.dbName = DB_NAME;
        this.dbSchema = DB_SCHEMA;
        this.dbUser = DB_USER;
        this.dbPassword = DB_PASSWORD;
    }

    private String getSystemProperty(String propName){
        String err_msg = "Required system property '" + propName + "' not found";
        return Objects.requireNonNull(System.getProperty(propName), err_msg);
    }

    public static void main(String[] args) {
        DatabaseMigrationHandler handler = new DatabaseMigrationHandler();
        // Test database connection
        if (handler.testConnection()) {
            System.out.println("Database connection successful!");

            // Run Flyway migrations
            handler.runMigrations();
        } else {
            System.out.println("Failed to connect to the database.");
        }
    }

    private boolean testConnection() {
        try (Connection connection = getDataSource().getConnection()) {
            return connection != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void runMigrations() {
        try{
            Flyway flyway = Flyway.configure()
                    .dataSource(getDataSource())
                    .schemas(this.dbSchema)
                    .load();
            flyway.migrate();
            System.out.println("Completed Database migration!");
        } catch (Exception e) {
            System.out.println("Database migration failed!");
            e.printStackTrace();
        }
    }

    private AwsWrapperDataSource getDataSource() {
        Properties targetDataSourceProps = new Properties();
        targetDataSourceProps.setProperty("ssl", "false");
        targetDataSourceProps.setProperty("password", this.dbPassword);

        AwsWrapperDataSource ds = new AwsWrapperDataSource();
        ds.setJdbcProtocol("jdbc:postgresql:");
        ds.setTargetDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        ds.setServerName(this.dbHost);
        ds.setDatabase(this.dbName);
        ds.setServerPort(this.dbPort);
        ds.setUser(this.dbUser);
        ds.setTargetDataSourceProperties(targetDataSourceProps);

        return ds;
    }
}
