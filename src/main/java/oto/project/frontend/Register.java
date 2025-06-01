package oto.project.frontend;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Register implements Structure, Style, Event {

    private Scene scene;
    private Stage stage;
    private Label firstNameLabel;
    private TextField firstNameField;
    private Label lastNameLabel;
    private TextField lastNamefield;
    private Label passwordLabel;
    private PasswordField passwordField;
    private Label confirmPasswordLabel;
    private PasswordField confirmPasswordField;
    private Label emailLabel;
    private TextField emailField;
    private Button registerButton;

    public Register() {
        this.stage = new Stage();
        this.firstNameLabel = new Label("firstname");
        this.firstNameField = new TextField();
        this.lastNameLabel = new Label("lastname");
        this.lastNamefield = new TextField();
        this.passwordLabel = new Label("password");
        this.passwordField = new PasswordField();
        this.confirmPasswordLabel = new Label("confirm:");
        this.confirmPasswordField = new PasswordField();
        this.emailLabel = new Label("email:");
        this.emailField = new TextField();
        this.registerButton = new Button("register");

        this.setupStructure();
        this.setupStyles();
        this.setupEvents();
    }

    @Override
    public void setupStructure() {
        VBox root = new VBox(10);
        root.getStyleClass().add("register-form");
        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(
                firstNameLabel, firstNameField,
                lastNameLabel, lastNamefield,
                passwordLabel, passwordField,
                confirmPasswordLabel, confirmPasswordField,
                emailLabel, emailField,
                registerButton
        );

        this.scene = new Scene(root, 300, 450);

        this.stage.setScene(this.scene);
        this.stage.setTitle("User Registration");
    }

    @Override
    public void setupStyles() {
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        firstNameLabel.getStyleClass().add("form-label");
        lastNameLabel.getStyleClass().add("form-label");
        passwordLabel.getStyleClass().add("form-label");
        confirmPasswordLabel.getStyleClass().add("form-label");
        emailLabel.getStyleClass().add("form-label");

        firstNameField.getStyleClass().add("form-field");
        lastNamefield.getStyleClass().add("form-field");
        passwordField.getStyleClass().add("form-field");
        confirmPasswordField.getStyleClass().add("form-field");
        emailField.getStyleClass().add("form-field");

        registerButton.getStyleClass().add("register-button");
    }

    @Override
    public void setupEvents() {

    }

    public void show() {
        this.stage.show();
    }
}
