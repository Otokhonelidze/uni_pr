package oto.project.frontend.Admin;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Employee {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty lastName;
    private final StringProperty email;
    private final StringProperty password;
    private final StringProperty createdAt;

    public Employee(int id, String name, String lastName, String email, String password, String createdAt) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.createdAt = new SimpleStringProperty(createdAt);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty emailProperty() { return email; }
    public StringProperty passwordProperty() { return password; }
    public StringProperty createdAtProperty() { return createdAt; }

    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getLastName() { return lastName.get(); }
    public String getEmail() { return email.get(); }
    public String getPassword() { return password.get(); }
    public String getCreatedAt() { return createdAt.get(); }

    public void setId(int id) { this.id.set(id); }
    public void setName(String name) { this.name.set(name); }
    public void setLastName(String lastName) { this.lastName.set(lastName); }
    public void setEmail(String email) { this.email.set(email); }
    public void setPassword(String password) { this.password.set(password); }
    public void setCreatedAt(String createdAt) { this.createdAt.set(createdAt); }
}