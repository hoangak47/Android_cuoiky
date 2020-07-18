package com.example.quanlyquancf.Lop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlyquancf.Adapter.BillAdapter;
import com.example.quanlyquancf.Adapter.FoodAdapter;
import com.example.quanlyquancf.Database.Database;
import com.example.quanlyquancf.DoiTuong.Bill;
import com.example.quanlyquancf.DoiTuong.BillFirebase;
import com.example.quanlyquancf.DoiTuong.Billkey;
import com.example.quanlyquancf.DoiTuong.Food;
import com.example.quanlyquancf.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BillUpdate extends AppCompatActivity  {

    ArrayList<Bill> lstBill;
    public static  ArrayList<Bill> billthanhtoan = new ArrayList<>();
    RecyclerView recyclerView;
    DatabaseReference billFire,table;
    Database db;
    long total = 0 ;
    TextView txttong;
    Button btnCapNhatTS, btnClearTS;
    public  int sl = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_bill_update);



        String coSQL = new Database(getBaseContext()).getco(GetIDTable());
        db = new Database(getBaseContext());
            lstBill = db.getBill(); // = Themon.arrBill;

        init();

        for (Bill bill : lstBill)
            total += (Long.parseLong(bill.getPrice())) * Integer.parseInt(bill.getQuantity());
        final long tt = total;
        Locale locale = new Locale("vi","VN");
        final NumberFormat ft = NumberFormat.getCurrencyInstance(locale);

        // Run function every 1s //
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(BillAdapter.tong > 1){
//                    txtgia.setText(ft.format(BillAdapter.tong));
                    txttong.setText(ft.format(BillAdapter.tong));

                }
                else{

//                    txtgia.setText(ft.format(total));
                    txttong.setText(ft.format(tt));
                }

                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
        btnCapNhatTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(BillUpdate.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Xác nhận")
                        .setContentText("Bạn có muốn cập nhật hóa đơn này không ?")
                        .setConfirmText("Có")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                billFire.child(Table.keys).child("lstBill").setValue(lstBill);
                                BillAdapter.tong = 0;
                                Locale locale = new Locale("vi","VN");
                                Number n= 0;
                                final NumberFormat ft = NumberFormat.getCurrencyInstance(locale);
                                try {
                                    n = ft.parse(txttong.getText().toString());
                                }
                                catch (Exception e){}
                                billFire.child(Table.keys).child("total").setValue(String.valueOf(n));
                                new Database(getBaseContext()).updateCo(GetIDTable(),1);
                                if(new Database(getBaseContext()).CheckEmptyBillInsert(GetIDTable())) {
                                    for (int i = 0; i < lstBill.size(); i++) {
                                        new Database(getBaseContext()).AddBillAfterInsert(lstBill.get(i), GetIDTable());
                                    }
                                }
                                else
                                {
                                    new Database(getBaseContext()).DeleteBillInsert(GetIDTable());
                                    for (int i = 0; i < lstBill.size(); i++) {
                                        new Database(getBaseContext()).AddBillAfterInsert(lstBill.get(i), GetIDTable());
                                    }
                                }

                                billthanhtoan = new Database(getBaseContext()).getBill();
                                Themon.sl = 0;
                                new Database(getBaseContext()).ClearBill();
                                Intent s= new Intent(getApplicationContext(),Table.class);
                                Bundle b = new Bundle();
                                b.putSerializable("user1", Table.temp);
                                s.putExtras(b);
                                startActivity(s);
                            }
                        })
                        .setCancelButton("Không", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();


//                AlertDialog alertDialog = new AlertDialog.Builder(BillUpdate.this)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setTitle("Thông báo")
//                        .setMessage("Bạn có muốn cập nhật hóa đơn này không ?")
//                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                billFire.child(Table.keys).child("lstBill").setValue(lstBill);
//                                Locale locale = new Locale("vi","VN");
//                                Number n= 0;
//                                final NumberFormat ft = NumberFormat.getCurrencyInstance(locale);
//                                try {
//                                    n = ft.parse(txtgia.getText().toString());
//                                }
//                                catch (Exception e){}
//                                billFire.child(Table.keys).child("total").setValue(String.valueOf(n));
//                                billthanhtoan = new Database(getBaseContext()).getBill();
//                                new Database(getBaseContext()).ClearBill();
//                                Intent s= new Intent(getApplicationContext(),Table.class);
//                                Bundle b = new Bundle();
//                                b.putSerializable("user1", Table.temp);
//                                s.putExtras(b);
//                                startActivity(s);
//                            }
//                        })
//                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.cancel();
//                            }
//                        })
//                        .show();

            }
        });


        btnClearTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(BillUpdate.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Xác nhận")
                        .setContentText("Bạn có muốn cập nhật hóa đơn này không ?")
                        .setConfirmText("Có")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                new Database(getBaseContext()).ClearBill();
                                Themon.sl = 0;
                                Intent s= new Intent(getApplicationContext(),Table.class);
                                Bundle b = new Bundle();
                                b.putSerializable("user1", Table.temp);
                                s.putExtras(b);
                                startActivity(s);
                            }
                        })
                        .setCancelButton("Không", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

//                AlertDialog alertDialog = new AlertDialog.Builder(BillUpdate.this)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setTitle("Thông báo")
//                        .setMessage("Bạn có muốn xóa hóa đơn này không ?")
//                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                new Database(getBaseContext()).ClearBill();
//                                Intent s= new Intent(getApplicationContext(),Table.class);
//                                Bundle b = new Bundle();
//                                b.putSerializable("user1", Table.temp);
//                                s.putExtras(b);
//                                startActivity(s);
//                            }
//                        })
//                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.cancel();
//                            }
//                        })
//                        .show();

            }
        });
    }
    private void init()
    {
        billFire = FirebaseDatabase.getInstance().getReference("Bill");
        txttong = (TextView)findViewById(R.id.txttongtienTS);

        recyclerView = (RecyclerView)findViewById(R.id.lstbillTS);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        btnCapNhatTS =(Button)findViewById(R.id.btnCapNhatTS);
        btnClearTS = (Button)findViewById(R.id.btnClearTS);
        BillAdapter billAdapter= new BillAdapter(lstBill,getApplicationContext());
        table = FirebaseDatabase.getInstance().getReference("Table");
        recyclerView.setAdapter(billAdapter);
    }
    private int GetIDTable()
    {
        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        return bundle.getInt("id");
        // Toast.makeText(getApplicationContext(),"id: " + idTable,Toast.LENGTH_SHORT).show();
    }
}
