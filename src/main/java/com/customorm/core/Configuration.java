package com.customorm.core;

import java.io.InputStream;
import java.util.Properties;

/**
 * Loads configuration from properties file
 */
public class Configuration {
    private Properties properties = new Properties();
    private String url;
    private String username;
    private String password;
    private String driver;
    private boolean showSql;

    public Configuration() {
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find application.properties");
                setDefaultProperties();
                return;
            }
            properties.load(input);

            // Load database properties
            this.url = properties.getProperty("db.url");
            this.username = properties.getProperty("db.username");
            this.password = properties.getProperty("db.password");
            this.driver = properties.getProperty("db.driver");

            // Load ORM properties
            this.showSql = Boolean.parseBoolean(
                    properties.getProperty("orm.show.sql", "true")
            );

            // Load JDBC driver
            Class.forName(driver);

        } catch (Exception e) {
            e.printStackTrace();
            setDefaultProperties();
        }
    }

    private void setDefaultProperties() {
        this.url = "jdbc:mysql://localhost:3306/customorm_demo";
        this.username = "root";
        this.password = "password";
        this.driver = "com.mysql.cj.jdbc.Driver";
        this.showSql = true;
    }

    // Getters
    public String getUrl() { return url; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getDriver() { return driver; }
    public boolean isShowSql() { return showSql; }
}