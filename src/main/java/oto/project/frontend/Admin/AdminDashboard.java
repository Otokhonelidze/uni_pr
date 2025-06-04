package oto.project.frontend.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import oto.project.backend.Database;
import oto.project.frontend.SalaryMan.User;
import oto.project.frontend.interfaces.Event;
import oto.project.frontend.interfaces.Structure;

public class AdminDashboard implements Structure, Event {

    private Scene scene;
    private Stage stage;
    private Button createEmployeeBtn;
    private Button editEmployeeBtn;
    private Button deleteEmployeeBtn;
    private ObservableList<Employee> employeeData;
    private User currentUser;
    private Database db;
    private TableView<Employee> employeeTableView;

    public AdminDashboard(Database db, User currentUser) {
        this.stage = new Stage();
        this.createEmployeeBtn = new Button("Create Employee");
        this.editEmployeeBtn = new Button("Edit Employee");
        this.deleteEmployeeBtn = new Button("Delete Employee");
        this.employeeData = FXCollections.observableArrayList();
        this.db = db;
        this.currentUser = currentUser;
        this.loadEmployees();
        this.setupStructure();
        this.setupEvents();
    }

    @Override
    public void setupStructure() {
        BorderPane root = new BorderPane();

        HBox header = new HBox(20);
        Label text = new Label("Admin Management System");
        header.getChildren().addAll(text);
        root.setTop(header);

        VBox mainContent = new VBox(15);

        VBox status = new VBox(5);
        mainContent.getChildren().add(status);

        Label employeeManagementLabel = new Label("Employee Management");

        employeeTableView = new TableView<>();
        employeeTableView.setItems(employeeData);

        TableColumn<Employee, Integer> employeeIdColumn = new TableColumn<>("ID");
        employeeIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<Employee, String> employeeNameColumn = new TableColumn<>("Name");
        employeeNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<Employee, String> employeeLastNameColumn = new TableColumn<>("Last Name");
        employeeLastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        TableColumn<Employee, String> employeeEmailColumn = new TableColumn<>("Email");
        employeeEmailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        employeeTableView.getColumns().addAll(
                employeeIdColumn, employeeNameColumn, employeeLastNameColumn, employeeEmailColumn
        );

        HBox employeeActions = new HBox(10);
        employeeActions.getChildren().addAll(createEmployeeBtn, editEmployeeBtn, deleteEmployeeBtn);

        mainContent.getChildren().addAll(employeeManagementLabel, employeeTableView, employeeActions);

        root.setCenter(mainContent);

        this.scene = new Scene(root, 1000, 700);
        this.stage.setScene(scene);
        this.stage.setTitle("Admin Dashboard");
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void show() {
        this.stage.show();
    }

    @Override
    public void setupEvents() {
        editEmployeeBtn.setOnAction(event -> {
            Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
            if (selectedEmployee != null) {
                EmployeeDialog dialog = new EmployeeDialog(stage, selectedEmployee);
                dialog.showAndWait();

                Employee updatedEmployee = dialog.getEmployee();
                if (updatedEmployee != null) {
                    updateEmployee(selectedEmployee, updatedEmployee);
                }
            } else {
                showAlert(AlertType.WARNING, "No Employee Selected", "Select employee");
            }
        });

        deleteEmployeeBtn.setOnAction(event -> {
            Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
            if (selectedEmployee != null) {
                Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Deletion");
                confirmAlert.setHeaderText("Delete Employee?");
                confirmAlert.setContentText("Delete employee?: " + selectedEmployee.getName() + " " + selectedEmployee.getLastName() + "?");

                Optional<ButtonType> result = confirmAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    deleteEmployee(selectedEmployee);
                }
            } else {
                showAlert(AlertType.WARNING, "No Employee Selected", "Select employee");
            }
        });

        createEmployeeBtn.setOnAction(event -> {
            EmployeeDialog dialog = new EmployeeDialog(stage, null);
            dialog.showAndWait();

            Employee newEmployee = dialog.getEmployee();
            if (newEmployee != null) {
                createEmployee(newEmployee);
            }
        });
    }

    private void updateEmployee(Employee selectedEmployee, Employee updatedEmployee) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = db.getConnection();
            String sql = "UPDATE Employee SET Username = ?, LastName = ?, Email = ? WHERE ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, updatedEmployee.getName());
            pstmt.setString(2, updatedEmployee.getLastName());
            pstmt.setString(3, updatedEmployee.getEmail());
            pstmt.setInt(4, updatedEmployee.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                int index = employeeData.indexOf(selectedEmployee);
                if (index != -1) {
                    employeeData.set(index, updatedEmployee);
                }
                showAlert(AlertType.INFORMATION, "Employee Updated", "Employee " + updatedEmployee.getName());
            } else {
                showAlert(AlertType.ERROR, "Update Failed", "Can't update employee");
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Error updating employee: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Database.closeConnection(conn);
        }
    }

    private void deleteEmployee(Employee selectedEmployee) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = db.getConnection();
            String sql = "DELETE FROM Employee WHERE ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, selectedEmployee.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                employeeData.remove(selectedEmployee);
            } else {
                showAlert(AlertType.ERROR, "Deletion Failed", "can't delete employee");
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "DB ERROR: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Database.closeConnection(conn);
        }
    }

    private void createEmployee(Employee newEmployee) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = db.getConnection();
            String sql = "INSERT INTO Employee (Username, Lastname, Email, Password, CreatedAt) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, newEmployee.getName());
            pstmt.setString(2, newEmployee.getLastName());
            pstmt.setString(3, newEmployee.getEmail());
            pstmt.setString(4, newEmployee.getPassword());
            pstmt.setString(5, newEmployee.getCreatedAt());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    newEmployee.setId(generatedId);
                    employeeData.add(newEmployee);
                    showAlert(AlertType.INFORMATION, "Employee Created", "New employee " + newEmployee.getName());
                }
            } else {
                showAlert(AlertType.ERROR, "Creation Failed", "can't create new employee");
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Error creating employee: " + e.getMessage());
            System.err.println("Error creating employee: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Database.closeConnection(conn);
        }
    }

    private void loadEmployees() {
        employeeData.clear();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = db.getConnection();
            String sql = "SELECT ID, Username, Lastname, Email, Password, CreatedAt FROM Employee";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("username");
                String lastName = rs.getString("LastName");
                String email = rs.getString("Email");
                String password = rs.getString("Password");
                String createdAt = rs.getString("CreatedAt");
                employeeData.add(new Employee(id, name, lastName, email, password, createdAt));
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Database.closeConnection(conn);
        }
    }
}