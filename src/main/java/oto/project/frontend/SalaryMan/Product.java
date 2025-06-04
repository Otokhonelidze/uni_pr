package oto.project.frontend.SalaryMan;

public class Product {
    private int id;
    private String name;
    private double price;
    private String categoryName;
    private String manufacturerName;
    private String supplierName;
    private int quantity;
    private String createdAt;

    public Product(int id, String name, double price, String categoryName, String manufacturerName, String supplierName, int quantity, String createdAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryName = categoryName;
        this.manufacturerName = manufacturerName;
        this.supplierName = supplierName;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategoryName() { return categoryName; }
    public String getManufacturerName() { return manufacturerName; }
    public String getSupplierName() { return supplierName; }
    public int getQuantity() { return quantity; }
    public String getCreatedAt() { return createdAt; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public void setManufacturerName(String manufacturerName) { this.manufacturerName = manufacturerName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
