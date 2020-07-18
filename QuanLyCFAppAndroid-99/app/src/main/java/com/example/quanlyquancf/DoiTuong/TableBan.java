package com.example.quanlyquancf.DoiTuong;

public class TableBan {
    private String ID;
    private String Name;
    private String Status;

    public  TableBan(){}
    public TableBan(String ID, String name, String status) {
        this.ID = ID;
        Name = name;
        Status = status;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
