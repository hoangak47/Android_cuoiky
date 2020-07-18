package com.example.quanlyquancf.Lop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancf.Database.Database;
import com.example.quanlyquancf.DoiTuong.BillFirebase;
import com.example.quanlyquancf.DoiTuong.Billkey;
import com.example.quanlyquancf.DoiTuong.TableBan;
import com.example.quanlyquancf.R;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlyquancf.Adapter.BillAdapter;
import com.example.quanlyquancf.DoiTuong.Bill;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BillActivity extends AppCompatActivity {

    ArrayList<Bill> lstBill;
    RecyclerView recyclerView;
    DatabaseReference billFire,table;
    BillFirebase biF;
    Billkey bk;
    String mili;
    int idTables;
    long total = 0 ;
    Button btnXacNhanBill, btnClear;
    int countItem;
    TextView txtTt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_bill);

        lstBill = new Database(getBaseContext()).getBill();
        GetIDTable();
        init();

        for (Bill bill : lstBill)
            total += (Long.parseLong(bill.getPrice())) * Integer.parseInt(bill.getQuantity());
//        txtGt = (TextView)findViewById(R.id.txtgiatien);
        txtTt = (TextView)findViewById(R.id.txttongtien);
        final long tt = total;

        Locale locale = new Locale("vi","VN");
        final NumberFormat ft = NumberFormat.getCurrencyInstance(locale);

        // Run function every 1s //
         final Handler handler = new Handler();
         Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(BillAdapter.tong > 1){

//                    txtGt.setText(ft.format(BillAdapter.tong));
                    txtTt.setText(ft.format(BillAdapter.tong));

                }
                else{
//                txtGt.setText(ft.format(total));
                txtTt.setText(ft.format(tt));
                }

//                long tongt = new Database(getBaseContext()).getGia();
//                txtGt.setText(ft.format(tongt));
//                txtTt.setText(ft.format(tongt + 2000));
                if(FoodActivity.co == 1)
                {
                    long tongt = new Database(getBaseContext()).getGia();
//                    txtGt.setText(ft.format(tongt));
                    txtTt.setText(ft.format(tongt));
                }
                if(BillAdapter.co == 1)
                {
                    new SweetAlertDialog(BillActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Vui lòng chọn xóa hóa đơn!")
                            .show();
                }
                BillAdapter.co = 0;

                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);


        ////
        btnXacNhanBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new SweetAlertDialog(BillActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Xác nhận")
                        .setContentText("Bạn có muốn chốt hóa đơn này không")
                        .setConfirmText("Có")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                addBill();
                                FoodActivity.sl =0;
                                BillAdapter.tong = 0;
                                Themon.sl =0;
                                new Database(getBaseContext()).addCo(idTables,0);
                                new Database(getBaseContext()).ClearBill(); // Important
                                AddKey(String.valueOf(idTables),mili);
                                table.child(String.valueOf(idTables)).child("Status").setValue("Có người");
                                Intent s= new Intent(getApplicationContext(),Table.class);
                                Bundle b = new Bundle();
                                b.putSerializable("user1", Table.temp);
                                s.putExtras(b);
                                startActivity(s);;
                            }
                        })
                        .setCancelButton("Không", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();


//                androidx.appcompat.app.AlertDialog alertDialog = new AlertDialog.Builder(BillActivity.this)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setTitle("Thông báo")
//                        .setMessage("Bạn có muốn chốt hóa đơn này không ?")
//                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                addBill();
//                                new Database(getBaseContext()).ClearBill(); // Important
//                                AddKey(String.valueOf(idTables),mili);
//                                table.child(String.valueOf(idTables)).child("Status").setValue("Có người");
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

             //   finish();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database db1 = new Database(getBaseContext());
                db1.ClearBill();
                FoodActivity.sl =0;
                Themon.sl =0;

                finish();
            }
        });


    }
    private void init()
    {
        recyclerView = (RecyclerView)findViewById(R.id.lstbill);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        btnXacNhanBill =(Button)findViewById(R.id.btnXacNhanBill);
        btnClear = (Button)findViewById(R.id.btnClearBill);
        BillAdapter billAdapter= new BillAdapter(lstBill,getApplicationContext());
        table = FirebaseDatabase.getInstance().getReference("Table");
        recyclerView.setAdapter(billAdapter);

        countItem = billAdapter.getItemCount();
    }
    private void addBill() {

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        lstBill = new Database(getBaseContext()).getBill();
        billFire = FirebaseDatabase.getInstance().getReference("Bill");
        mili = String.valueOf(System.currentTimeMillis());
        Locale locale = new Locale("vi","VN");
         Number n= 0;
        final NumberFormat ft = NumberFormat.getCurrencyInstance(locale);
        try {
            n = ft.parse(txtTt.getText().toString());
        }
       catch (Exception e){}
        String date = currentDate +"_" +currentTime + "_" + mili;
        biF = new BillFirebase(
                String.valueOf(idTables),
                date,
                "rong",
                lstBill,
                String.valueOf(n),
                "0"
        );
        billFire.child(mili).setValue(biF);
        new Database(getBaseContext()).ClearBill();
        finish();
    }
    private void GetIDTable()
    {
        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        idTables = bundle.getInt("idT");
    }
    private void AddKey(String idTable,String key)
    {
        Database billk = new Database(getBaseContext());
        billk.AddKey(idTable,key);
    }
}