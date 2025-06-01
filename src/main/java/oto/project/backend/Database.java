package oto.project.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private String dbUrl;
    private String user;
    private String password;
    private Connection con = null;

    public Database(String serverName, String databaseName, String user, String password) {
        this.dbUrl = String.format("jdbc:sqlserver://%s;databaseName=%s;trustServerCertificate=true;", 
            serverName, databaseName);
        this.user = user;
        this.password = password;
    }

    public void startConnection() {
        try {
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            this.con = DriverManager.getConnection(this.dbUrl, this.user, this.password);
            if (con != null) {
                System.out.println("Connected to database: " + this.dbUrl);
            }
        } catch (SQLException e) {
            System.out.println("Error while connecting to the database: " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (this.con != null && !this.con.isClosed()) {
                this.con.close();
                System.out.println("Connection closed");
            }
        } catch (SQLException e) {
            System.out.println("Can't close the database: " + e.getMessage());
        }
    }

    public String getDbUrl() {
        return this.dbUrl;
    }

    public String getUser() {
        return this.user;
    }

    public String getPassword() {
        return this.password;
    }

    public Connection getCon() {
        return this.con;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
