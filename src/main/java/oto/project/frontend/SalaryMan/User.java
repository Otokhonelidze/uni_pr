package oto.project.frontend.SalaryMan;

public class User {
    private int id;
    private String username;
    private String name;
    private String role;

    public User(int id, String username, String name, String role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }

    public boolean isEmployee() {
        return "EMPLOYEE".equals(role);
    }
}