package com.example.quanlyquancf.DoiTuong;

public class BillInsert {
    private String IDTable;
    private String ProductID;
    private String ProductName;
    private String  Quantity;
    private String Price;

    public BillInsert(){}

    public BillInsert(String IDTable, String productID, String productName, String quantity, String price) {
        this.IDTable = IDTable;
        ProductID = productID;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
    }

    public String getIDTable() {
        return IDTable;
    }

    public void setIDTable(String IDTable) {
        this.IDTable = IDTable;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
