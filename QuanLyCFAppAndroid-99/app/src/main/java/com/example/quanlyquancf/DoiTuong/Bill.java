package com.example.quanlyquancf.DoiTuong;

import java.io.Serializable;

public class Bill implements Serializable {
    private String ProductID;
    private String ProductName;
    private String  Quantity;
    private String Price;

    public Bill(String productID, String productName, String quantity, String price) {
        ProductID = productID;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
    }
    public  Bill(){}
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
