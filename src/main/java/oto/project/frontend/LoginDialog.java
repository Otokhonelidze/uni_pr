package oto.project.frontend;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oto.project.backend.Auth;
import oto.project.backend.Database;
import oto.project.frontend.SalaryMan.User;

public class LoginDialog extends Stage {

    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button cancelButton;

    private User authenticatedUser;
    private Auth auth;

    public LoginDialog(Stage owner, Database db) {
        this.auth = new Auth(db);

        setTitle("Login");
        initModality(Modality.WINDOW_MODAL);
        initOwner(owner);
        setResizable(false);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Username:"), 0, 0);
        usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        grid.add(usernameField, 1, 0);

        grid.add(new Label("Password:"), 0, 1);
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        grid.add(passwordField, 1, 1);

        loginButton = new Button("Login");
        loginButton.setDefaultButton(true);
        loginButton.setOnAction(e -> handleLogin());

        cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            authenticatedUser = null;
            close();
        });

        HBox buttonBar = new HBox(10, loginButton, cancelButton);
        buttonBar.setPadding(new Insets(10, 0, 0, 0));
        grid.add(buttonBar, 1, 2);

        Scene scene = new Scene(grid);
        setScene(scene);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.WARNING, "Login Error", "Please enter both username and password.");
            return;
        }

        authenticatedUser = auth.authUser(username, password);

        if (authenticatedUser != null) {
            close();
        } else {
            showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
            usernameField.clear();
            passwordField.clear();
            usernameField.requestFocus();
        }
    }

    public User getAuthUser() {
        return authenticatedUser;
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}