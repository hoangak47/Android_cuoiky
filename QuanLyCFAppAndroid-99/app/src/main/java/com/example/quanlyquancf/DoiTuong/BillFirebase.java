package com.example.quanlyquancf.DoiTuong;

import java.util.ArrayList;

public class BillFirebase {
    private  String IDTable ;
    private  String DateCheckIn;
    private String DateCheckOut = "null";
    private ArrayList<Bill> LstBill;
    private String Total;
    private String Status = "0";

    public BillFirebase(String IDTable, String dateCheckIn, String dateCheckOut, ArrayList<Bill> lstBill, String total, String status) {
        this.IDTable = IDTable;
        DateCheckIn = dateCheckIn;
        DateCheckOut = dateCheckOut;
        LstBill = lstBill;
        Total = total;
        Status = status;
    }
    public  BillFirebase(){}
    public String getIDTable() {
        return IDTable;
    }

    public void setIDTable(String IDTable) {
        this.IDTable = IDTable;
    }

    public String getDateCheckIn() {
        return DateCheckIn;
    }

    public void setDateCheckIn(String dateCheckIn) {
        DateCheckIn = dateCheckIn;
    }

    public String getDateCheckOut() {
        return DateCheckOut;
    }

    public void setDateCheckOut(String dateCheckOut) {
        DateCheckOut = dateCheckOut;
    }

    public ArrayList<Bill> getLstBill() {
        return LstBill;
    }

    public void setLstBill(ArrayList<Bill> lstBill) {
        LstBill = lstBill;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
