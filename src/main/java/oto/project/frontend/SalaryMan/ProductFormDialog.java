package oto.project.frontend.SalaryMan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oto.project.backend.Database;

public class ProductFormDialog extends Stage {

    private TextField nameField;
    private TextField priceField;
    private TextField categoryField;
    private TextField manufacturerField;
    private TextField supplierField;
    private TextField quantityField;

    private Product resultProduct;
    private final Database db;
    private final Product originalProduct;

    private int selectedCategoryId;
    private int selectedManufacturerId;
    private int selectedSupplierId;

    public ProductFormDialog(Stage owner, Database db, Product productToEdit) {
        this.db = db;
        this.originalProduct = productToEdit;

        setTitle(productToEdit == null ? "Create New Product" : "Edit Product");
        initModality(Modality.WINDOW_MODAL);
        initOwner(owner);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Product Name:"), 0, 0);
        nameField = new TextField();
        nameField.setPromptText("Enter product name");
        grid.add(nameField, 1, 0);

        grid.add(new Label("Price:"), 0, 1);
        priceField = new TextField();
        priceField.setPromptText("Enter price");
        grid.add(priceField, 1, 1);

        grid.add(new Label("Category:"), 0, 2);
        categoryField = new TextField();
        categoryField.setPromptText("Enter Category");
        grid.add(categoryField, 1, 2);

        grid.add(new Label("Manufacturer:"), 0, 3);
        manufacturerField = new TextField();
        manufacturerField.setPromptText("Enter Manufacturer");
        grid.add(manufacturerField, 1, 3);

        grid.add(new Label("Supplier:"), 0, 4);
        supplierField = new TextField();
        supplierField.setPromptText("Enter Supplier");
        grid.add(supplierField, 1, 4);

        grid.add(new Label("Quantity:"), 0, 5);
        quantityField = new TextField();
        quantityField.setPromptText("Enter quantity");
        grid.add(quantityField, 1, 5);

        if (originalProduct != null) {
            nameField.setText(originalProduct.getName());
            priceField.setText(String.valueOf(originalProduct.getPrice()));
            quantityField.setText(String.valueOf(originalProduct.getQuantity()));
            categoryField.setText(originalProduct.getCategoryName());
            manufacturerField.setText(originalProduct.getManufacturerName());
            supplierField.setText(originalProduct.getSupplierName());
        }

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> handleSave());

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> handleCancel());

        HBox buttonBar = new HBox(10, saveButton, cancelButton);
        buttonBar.setPadding(new Insets(10, 0, 0, 0));
        grid.add(buttonBar, 1, 6);

        Scene scene = new Scene(grid);
        setScene(scene);
    }

    private void handleSave() {
        String name = nameField.getText();
        String priceText = priceField.getText();
        String quantityText = quantityField.getText();
        String categoryName = categoryField.getText();
        String manufacturerName = manufacturerField.getText();
        String supplierName = supplierField.getText();

        if (name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty() ||
            categoryName.isEmpty() || manufacturerName.isEmpty() || supplierName.isEmpty()) {
            showAlert(AlertType.WARNING, "Missing Information", "Fill all fields");
            return;
        }

        double price;
        int quantity;
        try {
            price = Double.parseDouble(priceText);
            quantity = Integer.parseInt(quantityText);
            if (price <= 0) {
                showAlert(AlertType.WARNING, "Invalid Price", "Price must be greater than zero.");
                return;
            }
            if (quantity < 0) {
                showAlert(AlertType.WARNING, "Invalid Quantity", "Quantity can't be negative");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.WARNING, "Invalid Input", "Price and Quantity must be valid numbers");
            return;
        }

        selectedManufacturerId = getEntityId("Manufacturer", manufacturerName);
        selectedSupplierId = getEntityId("Suppliers", supplierName);

        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (originalProduct == null) {
            resultProduct = new Product(0, name, price,
                    categoryName,
                    manufacturerName,
                    supplierName,
                    quantity, currentTime);
        } else {
            resultProduct = new Product(originalProduct.getId(), name, price,
                    categoryName,
                    manufacturerName,
                    supplierName,
                    quantity, originalProduct.getCreatedAt());
        }
        close();
    }

    private void handleCancel() {
        resultProduct = null;
        close();
    }

    public Product getResultProduct() {
        return resultProduct;
    }

    public int getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public int getSelectedManufacturerId() {
        return selectedManufacturerId;
    }

    public int getSelectedSupplierId() {
        return selectedSupplierId;
    }

    private int getEntityId(String tableName, String name) {
        if (name == null || name.isEmpty()) {
            return 0;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = db.getConnection();
            String sql = "SELECT Id FROM " + tableName + " WHERE Name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Id");
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Can't get ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Database.closeConnection(conn);
        }
        return 0;
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}