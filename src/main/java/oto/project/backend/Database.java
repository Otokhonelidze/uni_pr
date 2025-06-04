package oto.project.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private String dbUrl;
    private String user;
    private String password;

    public Database(String serverName, String databaseName, String user, String password) {
        this.dbUrl = String.format("jdbc:sqlserver://%s;databaseName=%s;trustServerCertificate=true;", 
            serverName, databaseName);
        this.user = user;
        this.password = password;
        
        try {
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(this.dbUrl, this.user, this.password);
        return conn;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public String getDbUrl() { return this.dbUrl; }
    public String getUser() { return this.user; }
    public String getPassword() { return this.password; }
    public void setDbUrl(String dbUrl) { this.dbUrl = dbUrl; }
    public void setUser(String user) { this.user = user; }
    public void setPassword(String password) { this.password = password; }
}