package oto.project.frontend;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product {
    private final IntegerProperty id;
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty categoryId;
    private final IntegerProperty manufacturerId;
    private final IntegerProperty supplierId;
    private final IntegerProperty quantity;
    private final StringProperty createdAt;

    public Product(int id, String name, double price, int categoryId,
                   int manufacturerId, int supplierId, int quantity,
                   String createdAt, String updatedAt) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.categoryId = new SimpleIntegerProperty(categoryId);
        this.manufacturerId = new SimpleIntegerProperty(manufacturerId);
        this.supplierId = new SimpleIntegerProperty(supplierId);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.createdAt = new SimpleStringProperty(createdAt);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public DoubleProperty priceProperty() { return price; }
    public IntegerProperty categoryIdProperty() { return categoryId; }
    public IntegerProperty manufacturerIdProperty() { return manufacturerId; }
    public IntegerProperty supplierIdProperty() { return supplierId; }
    public IntegerProperty quantityProperty() { return quantity; }
    public StringProperty createdAtProperty() { return createdAt; }

    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public double getPrice() { return price.get(); }
    public int getCategoryId() { return categoryId.get(); }
    public int getManufacturerId() { return manufacturerId.get(); }
    public int getSupplierId() { return supplierId.get(); }
    public int getQuantity() { return quantity.get(); }
    public String getCreatedAt() { return createdAt.get(); }

    public void setId(int id) { this.id.set(id); }
    public void setName(String name) { this.name.set(name); }
    public void setPrice(double price) { this.price.set(price); }
    public void setCategoryId(int categoryId) { this.categoryId.set(categoryId); }
    public void setManufacturerId(int manufacturerId) { this.manufacturerId.set(manufacturerId); }
    public void setSupplierId(int supplierId) { this.supplierId.set(supplierId); }
    public void setQuantity(int quantity) { this.quantity.set(quantity); }
    public void setCreatedAt(String createdAt) { this.createdAt.set(createdAt); }
}
