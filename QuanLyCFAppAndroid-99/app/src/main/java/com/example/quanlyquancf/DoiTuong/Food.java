package com.example.quanlyquancf.DoiTuong;

import android.graphics.Bitmap;

public class Food {
    private String Detail;
    private String Discount;
    private String ID;
    private String IDCategory;
    private String Image;
    private String Name;
    private Long Price;

    public Food(){}

    public Food(String detail, String discount, String ID, String IDCategory, String image, String name, Long price) {
        Detail = detail;
        Discount = discount;
        this.ID = ID;
        this.IDCategory = IDCategory;
        Image = image;
        Name = name;
        Price = price;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIDCategory() {
        return IDCategory;
    }

    public void setIDCategory(String IDCategory) {
        this.IDCategory = IDCategory;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Long getPrice() {
        return Price;
    }

    public void setPrice(Long price) {
        Price = price;
    }
}