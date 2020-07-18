package com.example.quanlyquancf.DoiTuong;

public class Billkey {
    String IDTable;
    String Key;

    public Billkey(String IDTable, String key) {
        this.IDTable = IDTable;
        Key = key;
    }

    public String getIDTable() {
        return IDTable;
    }

    public void setIDTable(String IDTable) {
        this.IDTable = IDTable;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
