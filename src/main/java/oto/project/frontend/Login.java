package oto.project.frontend;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class Login {
    protected VBox form; 
    protected Label usernameLabel;
    protected TextField userNameField;
    protected Label passwordLabel;
    protected PasswordField passwordField; 

    public Login() {
        this.form = new VBox(10);
        this.usernameLabel = new Label("username:");
        this.userNameField = new TextField();
        this.passwordLabel = new Label("password:");
        this.passwordField = new PasswordField();

        this.form.getChildren().addAll(
                this.usernameLabel, this.userNameField,
                this.passwordLabel, this.passwordField
        );
    }

    public VBox getForm() { return form; }
    public String getUsername() { return userNameField.getText(); }
    public String getPassword() { return passwordField.getText(); }
}
