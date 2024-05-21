package com.example;

import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import software.amazon.jdbc.PropertyDefinition;
import software.amazon.jdbc.ds.AwsWrapperDataSource;

public class DatabaseMigrationHandler {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "mysecretpassword";

//    private final String host;
//    private final String port;
//    private final String user;
//    private final String database;
//    private final String dbSchema;
//
//    public DatabaseMigrationHandler() {
//        this.host = "jdbc:postgresql://db:5432/postgres";
//        this.port = "5432";
//        this.user = "postgres";
//        this.database = "postgres";
//        this.dbSchema = "postgres";
//    }

    public static void main(String[] args) {
        // Test database connection
        if (testConnection()) {
            System.out.println("Database connection successful!");

            // Run Flyway migrations
            runMigrations();
        } else {
            System.out.println("Failed to connect to the database.");
        }
    }

    private static boolean testConnection() {
        try (Connection connection = getDataSource().getConnection()) {
            return connection != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void runMigrations() {
        try{
            Flyway flyway = Flyway.configure()
                    .dataSource(getDataSource())
                    .schemas("myschema")
                    .load();
            flyway.migrate();
            System.out.println("Completed Database migration!");
        } catch (Exception e) {
            System.out.println("Database migration failed!");
            e.printStackTrace();
        }
    }

    private static AwsWrapperDataSource getDataSource() {
        Properties targetDataSourceProps = new Properties();
        targetDataSourceProps.setProperty("ssl", "false");
        targetDataSourceProps.setProperty("password", DB_PASSWORD);

        AwsWrapperDataSource ds = new AwsWrapperDataSource();
        ds.setJdbcProtocol("jdbc:postgresql:");
        ds.setTargetDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        ds.setServerName("localhost");
        ds.setDatabase("postgres");
        ds.setServerPort("5432");
        ds.setUser("postgres");
        ds.setTargetDataSourceProperties(targetDataSourceProps);

        return ds;
    }
}
