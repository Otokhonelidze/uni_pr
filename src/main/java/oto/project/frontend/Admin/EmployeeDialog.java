package oto.project.frontend.Admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oto.project.frontend.interfaces.Event;
import oto.project.frontend.interfaces.Structure;
import oto.project.frontend.interfaces.Style;

public class EmployeeDialog extends Stage implements Structure, Style, Event {
    private TextField nameField;
    private TextField lastNameField;
    private TextField emailField;
    private PasswordField passwordField;
    private Button saveButton;
    private Button cancelButton;
    private GridPane grid;
    private HBox buttonBox;
    
    private Employee resultEmployee;
    private Employee originalEmployee;
    private boolean isEditMode;
    
    public EmployeeDialog(Stage owner, Employee employeeToEdit) {
        this.originalEmployee = employeeToEdit;
        this.isEditMode = (employeeToEdit != null);
        
        setTitle(isEditMode ? "Edit Employee" : "Create New Employee");
        initModality(Modality.WINDOW_MODAL);
        initOwner(owner);
        
        setupStructure();
        setupStyles();
        setupEvents();
        
        Scene scene = new Scene(grid, 400, isEditMode ? 250 : 300);
        setScene(scene);
    }
    
    @Override
    public void setupStructure() {
        grid = new GridPane();
        
        nameField = new TextField();
        lastNameField = new TextField();
        emailField = new TextField();
        
        Label nameLabel = new Label("First Name:");
        Label lastNameLabel = new Label("Last Name:");
        Label emailLabel = new Label("Email:");
        
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(lastNameLabel, 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(emailLabel, 0, 2);
        grid.add(emailField, 1, 2);
        
        if (!isEditMode) {
            Label passwordLabel = new Label("Password:");
            passwordField = new PasswordField();
            grid.add(passwordLabel, 0, 3);
            grid.add(passwordField, 1, 3);
        }
        
        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        buttonBox = new HBox(10, saveButton, cancelButton);
        
        grid.add(buttonBox, 1, isEditMode ? 3 : 4);
        
        if (isEditMode) {
            nameField.setText(originalEmployee.getName());
            lastNameField.setText(originalEmployee.getLastName());
            emailField.setText(originalEmployee.getEmail());
        }
    }
    
    @Override
    public void setupStyles() {
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        
        nameField.setPromptText("Enter first name");
        lastNameField.setPromptText("Enter last name");
        emailField.setPromptText("Enter email");
        
        if (passwordField != null) {
            passwordField.setPromptText("Enter password");
        }
        
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        
        saveButton.setDefaultButton(true);
        cancelButton.setCancelButton(true);
    }
    
    @Override
    public void setupEvents() {
        saveButton.setOnAction(e -> Save());
        cancelButton.setOnAction(e -> {
            this.resultEmployee = null;
            close();
        });
        
        nameField.setOnAction(e -> lastNameField.requestFocus());
        lastNameField.setOnAction(e -> emailField.requestFocus());
        emailField.setOnAction(e -> {
            if (passwordField != null) {
                passwordField.requestFocus();
            } else {
                Save();
            }
        });
        
        if (passwordField != null) {
            passwordField.setOnAction(e -> Save());
        }
    }
    
    private void Save() {
        String name = nameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        
        if (name.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Information", "Fill all fields");
            return;
        }
        
        if (isEditMode) {
            this.resultEmployee = new Employee(
                originalEmployee.getId(),
                name,
                lastName,
                email,
                originalEmployee.getPassword(),
                originalEmployee.getCreatedAt()
            );
        } else {
            String password = passwordField.getText();
            if (password.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Missing Information", "Fill all fields");
                return;
            }
            
            String currentDateTime = java.time.LocalDateTime.now().toString().substring(0, 19).replace('T', ' ');
            this.resultEmployee = new Employee(
                0,
                name,
                lastName,
                email,
                password,
                currentDateTime
            );
        }
        
        close();
    }
    
    public Employee getEmployee() {
        return resultEmployee;
    }
    
    public Employee getUpdatedEmployee() {
        return resultEmployee;
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}