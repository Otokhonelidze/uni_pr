package oto.project.frontend.SalaryMan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import oto.project.backend.Database;
import oto.project.frontend.interfaces.Event;
import oto.project.frontend.interfaces.Structure;

public class SalaryManDashboard implements Structure, Event {

    private Scene scene;
    private Stage stage;
    private Button createProductBtn;
    private Button editProductBtn;
    private Button deleteProductBtn;
    private ObservableList<Product> productData;
    private User currentUser;
    private Database db;

    private TableView<Product> productTableView;
    private Label totalUsersLabel;
    private Label totalProductsLabel;

    public SalaryManDashboard(Database db, User currentUser) {
        this.stage = new Stage();
        this.createProductBtn = new Button("Create product");
        this.editProductBtn = new Button("Edit product");
        this.deleteProductBtn = new Button("Delete product");
        this.productData = FXCollections.observableArrayList();
        this.db = db;
        this.currentUser = currentUser;
        this.setupStructure();
        this.loadProducts();
        this.loadOverviewData();
        this.setupEvents();
    }

    @Override
    public void setupStructure() {
        BorderPane root = new BorderPane();

        HBox header = new HBox(20);
        Label text = new Label("Salary Management System");
        header.getChildren().addAll(text);
        root.setTop(header);

        VBox mainContent = new VBox(15);

        productTableView = new TableView<>();
        VBox.setVgrow(productTableView, Priority.ALWAYS);

        Label overViewLabel = new Label("Overview");
        VBox status = new VBox(5);
        totalUsersLabel = new Label("Total users: Loading...");
        totalProductsLabel = new Label("Total products: Loading...");
        status.getChildren().addAll(overViewLabel, totalUsersLabel, totalProductsLabel);
        mainContent.getChildren().add(status);

        Label productManagementLabel = new Label("Product Management");
        mainContent.getChildren().add(productManagementLabel);

        productTableView.setItems(productData);

        TableColumn<Product, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(50);

        TableColumn<Product, String> nameColumn = new TableColumn<>("Product Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(150);

        TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setPrefWidth(80);

        TableColumn<Product, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        categoryColumn.setPrefWidth(120);

        TableColumn<Product, String> manufacturerColumn = new TableColumn<>("Manufacturer");
        manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturerName"));
        manufacturerColumn.setPrefWidth(150);

        TableColumn<Product, String> supplierColumn = new TableColumn<>("Supplier");
        supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        supplierColumn.setPrefWidth(150);

        TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setPrefWidth(80);

        TableColumn<Product, String> createdAtColumn = new TableColumn<>("Created At");
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        createdAtColumn.setPrefWidth(150);

        productTableView.getColumns().addAll(idColumn, nameColumn, priceColumn, categoryColumn,
                                             manufacturerColumn, supplierColumn, quantityColumn,
                                             createdAtColumn);

        mainContent.getChildren().add(productTableView);

        HBox actionButtons = new HBox(10);
        actionButtons.getStyleClass().add("action-buttons");
        actionButtons.getChildren().addAll(createProductBtn, editProductBtn, deleteProductBtn);
        mainContent.getChildren().add(actionButtons);

        root.setCenter(mainContent);

        this.scene = new Scene(root, 1200, 800);
        this.stage.setScene(scene);
        this.stage.setTitle("Product Management Dashboard");
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
        createProductBtn.setOnAction(event -> createProduct());
        editProductBtn.setOnAction(event -> editProduct());
        deleteProductBtn.setOnAction(event -> deleteProduct());
    }

    private void createProduct() {
        ProductFormDialog dialog = new ProductFormDialog(stage, db, null);
        dialog.showAndWait();

        Product newProduct = dialog.getResultProduct();
        if (newProduct != null) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                conn = db.getConnection();
                String sql = "INSERT INTO Products (Name, Price, CategoryId, ManufacturerId, SupplierId, Quantity, CreatedAt) VALUES (?, ?, ?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, newProduct.getName());
                pstmt.setDouble(2, newProduct.getPrice());
                pstmt.setInt(3, dialog.getSelectedCategoryId());
                pstmt.setInt(4, dialog.getSelectedManufacturerId());
                pstmt.setInt(5, dialog.getSelectedSupplierId());
                pstmt.setInt(6, newProduct.getQuantity());
                pstmt.setString(7, newProduct.getCreatedAt());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int newId = generatedKeys.getInt(1);
                            newProduct.setId(newId);
                            showAlert(AlertType.INFORMATION, "Success", "Product created");
                            loadProducts();
                            loadOverviewData();
                        }
                    }
                } else {
                    showAlert(AlertType.ERROR, "Error", "can't create product");
                }
            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                Database.closeConnection(conn);
            }
        }
    }

    private void editProduct() {
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert(AlertType.WARNING, "No Product Selected", "Please select a product to edit.");
            return;
        }

        ProductFormDialog dialog = new ProductFormDialog(stage, db, selectedProduct);
        dialog.showAndWait();

        Product updatedProduct = dialog.getResultProduct();
        if (updatedProduct != null) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                conn = db.getConnection();
                String sql = "UPDATE Products SET Name = ?, Price = ?, CategoryId = ?, ManufacturerId = ?, SupplierId = ?, Quantity = ?, UpdatedAt = ? WHERE Id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, updatedProduct.getName());
                pstmt.setDouble(2, updatedProduct.getPrice());
                pstmt.setInt(3, dialog.getSelectedCategoryId());
                pstmt.setInt(4, dialog.getSelectedManufacturerId());
                pstmt.setInt(5, dialog.getSelectedSupplierId());
                pstmt.setInt(6, updatedProduct.getQuantity());
                pstmt.setInt(8, updatedProduct.getId());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    showAlert(AlertType.INFORMATION, "Success", "Product updated successfully!");
                    loadProducts();
                } else {
                    showAlert(AlertType.ERROR, "Error", "Failed to update product.");
                }
            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Database Error", "Error updating product: " + e.getMessage());
                e.printStackTrace();
            } finally {
                Database.closeConnection(conn);
            }
        }
    }

    private void deleteProduct() {
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert(AlertType.WARNING, "No Product Selected", "Please select a product to delete.");
            return;
        }

        Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Product: " + selectedProduct.getName());
        confirmAlert.setContentText("Are you sure you want to delete this product?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                conn = db.getConnection();
                String sql = "DELETE FROM Products WHERE Id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, selectedProduct.getId());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    productData.remove(selectedProduct);
                    showAlert(AlertType.INFORMATION, "Success", "Product deleted successfully!");
                    loadOverviewData();
                } else {
                    showAlert(AlertType.ERROR, "Error", "Failed to delete product.");
                }
            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Database Error", "Error deleting product: " + e.getMessage());
                e.printStackTrace();
            } finally {
                Database.closeConnection(conn);
            }
        }
    }

    private void loadProducts() {
        productData.clear();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = db.getConnection();
            String sql = "SELECT " +
                         "p.Id, p.Name AS ProductName, p.Price, p.Quantity, p.CreatedAt, p.UpdatedAt, " +
                         "c.Name AS CategoryName, " +
                         "m.Name AS ManufacturerName, " +
                         "s.Name AS SupplierName " +
                         "FROM Products p " +
                         "LEFT JOIN Categories c ON p.CategoryId = c.Id " +
                         "LEFT JOIN Manufacturer m ON p.ManufacturerId = m.Id " +
                         "LEFT JOIN Suppliers s ON p.SupplierId = s.Id";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Id");
                String productName = rs.getString("ProductName");
                double price = rs.getDouble("Price");
                int quantity = rs.getInt("Quantity");
                String createdAt = rs.getTimestamp("CreatedAt") != null ?
                                   rs.getTimestamp("CreatedAt").toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A";
                String categoryName = rs.getString("CategoryName");
                String manufacturerName = rs.getString("ManufacturerName");
                String supplierName = rs.getString("SupplierName");

                productData.add(new Product(id, productName, price, categoryName, manufacturerName, supplierName, quantity, createdAt));
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Error loading products: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Database.closeConnection(conn);
        }
    }

    private void loadOverviewData() {
        Connection conn = null;
        PreparedStatement pstmtUsers = null;
        PreparedStatement pstmtProducts = null;
        ResultSet rsUsers = null;
        ResultSet rsProducts = null;
        try {
            conn = db.getConnection();

            String sqlUsers = "SELECT COUNT(*) AS TotalUsers FROM Employee";
            pstmtUsers = conn.prepareStatement(sqlUsers);
            rsUsers = pstmtUsers.executeQuery();
            if (rsUsers.next()) {
                int totalUsers = rsUsers.getInt("TotalUsers");
                totalUsersLabel.setText("Total users: " + totalUsers);
            }

            String sqlProducts = "SELECT COUNT(*) AS TotalProducts FROM Products";
            pstmtProducts = conn.prepareStatement(sqlProducts);
            rsProducts = pstmtProducts.executeQuery();
            if (rsProducts.next()) {
                int totalProducts = rsProducts.getInt("TotalProducts");
                totalProductsLabel.setText("Total products: " + totalProducts);
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Error loading overview data: " + e.getMessage());
            System.err.println("Error loading overview data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Database.closeConnection(conn);
        }
    }
}