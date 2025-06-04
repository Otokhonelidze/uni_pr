package oto.project.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oto.project.frontend.SalaryMan.User;

public class Auth {

    private final Database db;

    public Auth(Database db) {
        this.db = db;
    }

    public User authUser(String username, String password) {
        User user = null;

        user = authUser("Admin", username, password, "ADMIN");
        if (user != null) {
            return user;
        }

        user = authUser("Employee", username, password, "EMPLOYEE");
        return user;
    }

    private User authUser(String tableName, String username, String password, String role) {
        String sql = "SELECT Id, LastName, Password FROM " + tableName + " WHERE Username = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = db.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String savedPassword = rs.getString("Password");
                if (savedPassword.equals(password)) {
                    user = new User(rs.getInt("Id"), username, rs.getString("Lastname"), role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.closeConnection(conn);
        }
        return user;
    }
}