package com.example.quanlyquancf.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.Toast;

import com.example.quanlyquancf.DoiTuong.Bill;
import com.example.quanlyquancf.DoiTuong.BillInsert;
import com.example.quanlyquancf.DoiTuong.Billkey;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class Database extends SQLiteAssetHelper {
    private  static  final String DB_Name= "ca_phe_sieu_khung.db";
    private  static  final int DB_Ver=1;
    public static SQLiteDatabase db;
    public  Database(Context context)
    {
        super(context,DB_Name,null,DB_Ver);
    }
    public ArrayList<Bill> getBill()
    {
        db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelct = {"ProductID","ProductName","Quantity","Price"};
        String sqlTable = "Bill";

        qb.setTables(sqlTable);
        Cursor c=  qb.query(db,sqlSelct,null,null,null,null,null);

        final  ArrayList<Bill> result= new ArrayList<>();
        if(c.moveToFirst())
        {
            do {
                    result.add(new Bill(c.getString(c.getColumnIndex("ProductID")),
                            c.getString(c.getColumnIndex("ProductName")),
                                    c.getString(c.getColumnIndex("Quantity")),
                                            c.getString(c.getColumnIndex("Price"))
                    ));
            }while (c.moveToNext());
        }

        return result;
    }
    public  void AddToCart(Bill bill)
    {
        db = getReadableDatabase();
        String query = String.format("INSERT INTO Bill(ProductID,ProductName,Quantity,Price) VALUES('%s','%s','%s','%s');",

                bill.getProductID(),
                bill.getProductName(),
                bill.getQuantity(),
                bill.getPrice()
                );
        db.execSQL(query);

        db.close();
    }

    public void ClearBill()
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Bill");
        db.execSQL(query);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + "Bill" + "'");
    }
    public  void AddKey(String IDtable,String key)
    {
        db = getReadableDatabase();
        String query = String.format("INSERT INTO BillFire(IDTable,Key) VALUES('%s','%s');",

                IDtable,
                key
        );
        db.execSQL(query);

        db.close();
    }
    public ArrayList<Billkey> getKey()
    {
        db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelct = {"IDTable","Key"};
        String sqlTable = "BillFire";

        qb.setTables(sqlTable);
        Cursor c=  qb.query(db,sqlSelct,null,null,null,null,null);

        final  ArrayList<Billkey> result= new ArrayList<>();
        if(c.moveToFirst())
        {
            do {
                result.add(new Billkey(c.getString(c.getColumnIndex("IDTable")),
                        c.getString(c.getColumnIndex("Key"))
                ));
            }while (c.moveToNext());
        }

        return result;
    }
    public void DeleteKey(int idTable)
    {
        String id = String.valueOf(idTable);
         db.delete("BillFire", "IDTable" + "=" + id, null) ;
    }
    public void DeleteRow(int vitri)
    {
        db = getWritableDatabase();
        String temp = String.valueOf(vitri);

        db.delete("Bill", "ID" + "=" + temp, null);
    }

    public boolean CheckNameFoodEquals(String id)
    {
        String countQuery = "SELECT  * FROM " + "Bill" + " WHERE ProductID = '" +id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        if (count > 0)
            return true;
        return false;
    }
    public  void UpdateQuantityFood(String id,int quantity)
    {
        db = getReadableDatabase();
        String query = "UPDATE Bill SET Quantity = '" +String.valueOf(quantity)+"'" + " WHERE ProductID = '"+ id + "'";
        db.execSQL(query);
    }
    public  int GetQuantity(String id)
    {
        String countQuery = "SELECT Quantity FROM " + "Bill" + " WHERE ProductID = '" +id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor!=null)
            cursor.moveToFirst();
       int soluong = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Quantity")));
       return  soluong;
    }
    public boolean isNotEmptyBill()
    {
        String countQuery = "SELECT * FROM " + "Bill";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        if (count > 0)
            return true;
        return false;
    }

    public long getGia()
    {
        long gia = 0;
        db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelct = {"ProductID","ProductName","Quantity","Price"};
        String sqlTable = "Bill";

        qb.setTables(sqlTable);
        Cursor c=  qb.query(db,sqlSelct,null,null,null,null,null);

        if(c.moveToFirst())
        {
            do {
                gia += Long.parseLong(c.getString(c.getColumnIndex("Quantity")) ) *
                 Long.parseLong(c.getString(c.getColumnIndex("Price")));
            }while (c.moveToNext());
        }
        return gia;
    }
    public  String GetKeyInTable(int idTable)
    {
        String countQuery = "SELECT  * FROM " + "BillFire" + " WHERE IDTable = '" + idTable + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor!=null)
            cursor.moveToFirst();
        String key = cursor.getString(cursor.getColumnIndex("Key"));
        cursor.close();
        return key;
    }

    // Co --------------------------
    public void addCo(int idTable,int co)
    {
        db = getReadableDatabase();
        String query = "INSERT INTO CheckTable (IDTable,Co) VALUES ('"+  idTable + "','"+ co +"'  );  ";
        db.execSQL(query);
    }
    public void updateCo(int idTable,int co)
    {
        db = getReadableDatabase();
        String query = "UPDATE CheckTable SET Co = '" +String.valueOf(co)+"'" + " WHERE IDTable = '"+ idTable + "'";
        db.execSQL(query);
    }
    public String getco(int idTable)
    {
        String countQuery = "SELECT  * FROM " + "CheckTable" + " WHERE IDTable = '" + idTable + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor!=null)
            cursor.moveToFirst();
        String co = cursor.getString(cursor.getColumnIndex("Co"));
        cursor.close();
        return co;
    }
    public void DeleteCo(int idTable)
    {
        String id = String.valueOf(idTable);
        db.delete("CheckTable", "IDTable" + "=" + id, null) ;
    }


    public  void AddBillAfterInsert(Bill bill,int idTable)
    {
        db = getReadableDatabase();
        String query = String.format("INSERT INTO BillInsert(IDTable,ProductID,ProductName,Quantity,Price) VALUES('%s','%s','%s','%s','%s');",
                String.valueOf(idTable),
                bill.getProductID(),
                bill.getProductName(),
                bill.getQuantity(),
                bill.getPrice()
        );
        db.execSQL(query);

        db.close();
    }
    public  boolean CheckEmptyBillInsert(int idTable)
    {

        String countQuery = "SELECT  * FROM " + "BillInsert" + " WHERE IDTable = '" + idTable + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        if (count < 1)
            return true;
        return false;
    }
    public ArrayList<Bill> getBillAfterInsert(int idTable)
    {
        db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelct = {"IDTable","ProductID","ProductName","Quantity","Price"};
        String sqlTable = "BillInsert";

        qb.setTables(sqlTable);
        Cursor c=  qb.query(db,sqlSelct,"IDTable =?",new String[]{String.valueOf(idTable)},null,null,null);



        final  ArrayList<Bill> result= new ArrayList<>();
        if(c.moveToFirst())
        {
            do {
                result.add(new Bill(
                        c.getString(c.getColumnIndex("ProductID")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price"))
                ));
            }while (c.moveToNext());
        }

        return result;
    }
    public void DeleteBillInsert(int idTable)
    {
        String id = String.valueOf(idTable);
        db.delete("BillInsert", "IDTable" + "=" + id, null) ;
    }
    public void DeleteAllBillInsert()
    {
        db.delete("BillInsert", null, null) ;
    }
    //-------------------------------------------------------------
    public long getTongTien(int idTable)
    {
        long gia = 0;
        db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelct = {"IDTable,ProductID","ProductName","Quantity","Price"};
        String sqlTable = "BillInsert";

        qb.setTables(sqlTable);
        Cursor c=  qb.query(db,sqlSelct,"IDTable =?",new String[]{String.valueOf(idTable)},null,null,null);

        if(c.moveToFirst())
        {
            do {
                gia += Long.parseLong(c.getString(c.getColumnIndex("Quantity")) ) *
                        Long.parseLong(c.getString(c.getColumnIndex("Price")));
            }while (c.moveToNext());
        }
        return gia;
    }
}
