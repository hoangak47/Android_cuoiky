package com.example.quanlyquancf.Lop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlyquancf.Adapter.BillAdapter;
import com.example.quanlyquancf.Adapter.PaymentAdapter;
import com.example.quanlyquancf.Database.Database;
import com.example.quanlyquancf.DoiTuong.Bill;
import com.example.quanlyquancf.DoiTuong.BillFirebase;
import com.example.quanlyquancf.DoiTuong.TableBan;
import com.example.quanlyquancf.PluginEx.MinMaxEdit;
import com.example.quanlyquancf.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ThanhtoanActivity extends AppCompatActivity {

    DatabaseReference billf,tt,tables;
    ArrayList<BillFirebase> lstb = new ArrayList<>();
    BillFirebase bbf;
    ArrayList<Bill> b;
    Button btnThanhToan,btnClearTT;
    EditText edgiamgia;
    int discounts = 0;
    int co = 0;
    List<String> keys = new ArrayList<>();
    TextView txtTongTT,txtGiaTT;
    long tongtt,gia,tongtt1,gia1;
    float giamgia;
    int vitri ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_thanhtoan);
        edgiamgia = (EditText) findViewById(R.id.txtgiamgia1);
        edgiamgia.setFilters(new InputFilter[]{new MinMaxEdit("1", "99")});
        billf = FirebaseDatabase.getInstance().getReference();
        tables = FirebaseDatabase.getInstance().getReference("Table");
        tt = FirebaseDatabase.getInstance().getReference("Bill");
        AnhXa();
        Locale locale = new Locale("vi", "VN");
        final NumberFormat ft = NumberFormat.getCurrencyInstance(locale);

        if (!new Database(getBaseContext()).CheckEmptyBillInsert(GetIDTables())) {
            co = 0;
            ArrayList<Bill> tam = new Database(getBaseContext()).getBillAfterInsert(GetIDTables());
            for (int i = 0; i < tam.size(); i++) {
                gia += Long.parseLong(tam.get(i).getQuantity()) * Long.parseLong(tam.get(i).getPrice());
            }
            tongtt = gia + 2000;
            txtGiaTT.setText(ft.format(gia));
             txtTongTT.setText(ft.format(tongtt));
        }

        GetALLListBill(new ThanhtoanActivity.FirebaseCallBack() {

                @Override
                public void onCallBack(ArrayList<BillFirebase> list) {
                    lstb = list;
                    for (int i = 0; i < lstb.size(); i++) {
                        if (lstb.get(i).getStatus().equals("0") && Integer.parseInt(lstb.get(i).getIDTable()) == GetIDTables()) {
                            b = lstb.get(i).getLstBill();
                            vitri = i;
                            break;
                        }
                    }
                    init();
                    co = 1;
                    String keys = XulyKey(lstb.get(vitri).getDateCheckIn());
//                    if (Themon.cos == 0) {
                        Long tonggia = Long.valueOf(lstb.get(vitri).getTotal());
                        tongtt1 = tonggia + 2000;
                        gia1 = Long.parseLong(lstb.get(vitri).getTotal());
                        txtGiaTT.setText(ft.format(gia1));
                        txtTongTT.setText(ft.format(tongtt1));
//                    }
                }
            });

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(!edgiamgia.getText().toString().isEmpty())
                {
                    giamgia = Integer.parseInt(edgiamgia.getText().toString());
                    if(co == 0) {
                        float temp = gia - (gia * (giamgia /100) ) + 2000f;
                        txtTongTT.setText(ft.format(temp));
                    }
                    else
                    {
                        float temp =gia1 -  (gia1 *  (giamgia /100) ) + 2000f;
                        txtTongTT.setText(ft.format(temp));
                    }

                }
                else
                {
                    if(co == 0) {

                        txtTongTT.setText(ft.format(tongtt));
                    }
                    else
                    {

                        txtTongTT.setText(ft.format(tongtt1));
                    }
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);

        btnThanhToan = (Button)findViewById(R.id.btnXacNhanThanhToan);
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(ThanhtoanActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Xác nhận")
                        .setContentText("Bạn có muốn thanh toán hóa đơn này không?")
                        .setConfirmText("Có")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                String keys = XulyKey(lstb.get(vitri).getDateCheckIn());
                                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                String dateC = currentDate + "_" + currentTime;

                                FoodActivity.sl = 0;
                                billf.child("Bill").child(keys).child("status").setValue("1");
                                billf.child("Bill").child(keys).child("dateCheckOut").setValue(dateC);
                                billf.child("Bill").child(keys).child("total").setValue(txtTongTT.getText().toString());
                                tables.child(String.valueOf(GetIDTables())).child("Status").setValue("Trống");

                                // Xóa key
                                new Database(getBaseContext()).DeleteKey(GetIDTables());
                                new Database(getBaseContext()).DeleteCo(GetIDTables());
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

//
//                AlertDialog alertDialog = new AlertDialog.Builder(ThanhtoanActivity.this)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setTitle("Thông báo")
//                        .setMessage("Bạn có muốn thanh toán hóa đơn này không ?")
//                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                String keys = XulyKey(lstb.get(vitri).getDateCheckIn());
//                                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//                                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
//                                String dateC = currentDate + "_" + currentTime;
//
//                                FoodActivity.sl = 0;
//                                billf.child("Bill").child(keys).child("status").setValue("1");
//                                billf.child("Bill").child(keys).child("dateCheckOut").setValue(dateC);
//                                billf.child("Bill").child(keys).child("total").setValue(txtTongTT.getText().toString());
//                                tables.child(String.valueOf(GetIDTables())).child("Status").setValue("Trống");
//
//                                // Xóa key
//                                new Database(getBaseContext()).DeleteKey(GetIDTables());
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

        btnClearTT = (Button)findViewById(R.id.btnXoaTT);
        btnClearTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(ThanhtoanActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Xác nhận")
                        .setContentText("Bạn có muốn xóa hóa đơn này không ?")
                        .setConfirmText("Có")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                String keys = XulyKey(lstb.get(vitri).getDateCheckIn());
                                new Database(getBaseContext()).DeleteKey(GetIDTables());
                                billf.child("Bill").child(keys).removeValue();
                                tables.child(String.valueOf(GetIDTables())).child("Status").setValue("Trống");

                                Themon.sl = 0;
                                FoodActivity.sl = 0;
                                new Database(getBaseContext()).DeleteCo(GetIDTables());
                                new Database(getBaseContext()).DeleteBillInsert(GetIDTables());
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

//                AlertDialog alertDialog = new AlertDialog.Builder(ThanhtoanActivity.this)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setTitle("Thông báo")
//                        .setMessage("Bạn có muốn xóa hóa đơn này không ?")
//                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                String keys = XulyKey(lstb.get(vitri).getDateCheckIn());
//                                new Database(getBaseContext()).DeleteKey(GetIDTables());
//                                billf.child("Bill").child(keys).removeValue();
//                                tables.child(String.valueOf(GetIDTables())).child("Status").setValue("Trống");
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
    private  void AnhXa()
    {
        txtTongTT = (TextView)findViewById(R.id.txttongtien1);
        txtGiaTT = (TextView)findViewById(R.id.txtgiatien1);
        String key = new Database(getBaseContext()).GetKeyInTable(GetIDTables());
        tt.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bbf = dataSnapshot.getValue(BillFirebase.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void init()
    {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.lstthanhtoan);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        PaymentAdapter billAdapter= new PaymentAdapter(b,getApplicationContext());
        recyclerView.setAdapter(billAdapter);
    }

    public void GetALLListBill(final ThanhtoanActivity.FirebaseCallBack firebaseCallBack)
    {


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    BillFirebase tam = ds.getValue(BillFirebase.class);
                    lstb.add(tam);
                }
                firebaseCallBack.onCallBack(lstb);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        billf.child("Bill").addListenerForSingleValueEvent(valueEventListener);
    }
    private int GetIDTables()
    {
        int idTable;
        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        idTable = bundle.getInt("id");
        return  idTable;
    }
    private interface  FirebaseCallBack{

        void onCallBack(ArrayList<BillFirebase> list);
    }
    private  String XulyKey(String temp)
    {
        String[] a = temp.split("_");
        String keyss ;
        keyss = a[2].trim();
        return keyss;
    }
}