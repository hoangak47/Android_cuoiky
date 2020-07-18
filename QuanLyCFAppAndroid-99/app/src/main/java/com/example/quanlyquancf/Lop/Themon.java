package com.example.quanlyquancf.Lop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlyquancf.Adapter.FoodAdapter;
import com.example.quanlyquancf.Database.Database;
import com.example.quanlyquancf.DoiTuong.Bill;
import com.example.quanlyquancf.DoiTuong.BillFirebase;
import com.example.quanlyquancf.DoiTuong.Food;
import com.example.quanlyquancf.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Themon extends AppCompatActivity implements FoodAdapter.OnItemReClickListener  {

    DatabaseReference food;
    ArrayList<Food> arrFood=new ArrayList<>();
    EditText txtSL;
    int idTable;
    RecyclerView recyclerView;
    Button btnCapnhat,btnCHonLai;
    Database db;
    Bill bills  ;
    public  static  ArrayList<Bill> arrBill = new ArrayList<>();

    DatabaseReference billFire;
    TextView txtDangChon;
    public static int sl = 0;
    public static int cos = 0;
    public static int coQuantity = 0;
    public static ArrayList<BillFirebase> arrBillF = new ArrayList<>();
    BillFirebase bte;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themon);

        db = new Database(getBaseContext());
        btnCHonLai = (Button)findViewById(R.id.btnChonLai2);
        btnCHonLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          //      db.ClearBill();
                sl = 0;
                finish();
            }
        });
        txtDangChon = (TextView) findViewById(R.id.txtDangChon1);
        txtSL = (EditText)findViewById(R.id.soluong);
        food = FirebaseDatabase.getInstance().getReference();
        btnCapnhat = (Button)findViewById(R.id.btnCapNhat);
        GetIDTable();

        billFire = FirebaseDatabase.getInstance().getReference();
        btnCapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new Database(getBaseContext()).isNotEmptyBill()) {
                    cos = 1;
                    Intent s= new Intent(getApplicationContext(),BillUpdate.class);
                    Bundle b = new Bundle();
                    b.putInt("id", idTable);
                    s.putExtras(b);
                    startActivity(s);
                }
                else
                {
                    new SweetAlertDialog(Themon.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Vui lòng chọn món!")
                            .show();
                }
            }
        });
        GetAllFood(new Themon.FirebaseCallBack() {
            @Override
            public void onCallBack(ArrayList<Food> list) {
                arrFood = list;
                //     SetTable();
                init();
            }
        });
        String coSQL = new Database(getBaseContext()).getco(idTable);
        if(Integer.parseInt(coSQL) == 0) {
            GetALLListBill(new FireBaseCallBill() {
                @Override
                public void onCallBack(ArrayList<BillFirebase> list) {
                    arrBillF = list;
                    ArrayList<Bill> biltemp = new ArrayList<>();
                    for (int i = 0; i < arrBillF.size(); i++) {
                        if (arrBillF.get(i).getStatus().equals("0") && Integer.parseInt(arrBillF.get(i).getIDTable()) == idTable) {
                            biltemp = arrBillF.get(i).getLstBill();
                            break;
                        }
                    }
                    sl = biltemp.size();
                    arrBill = biltemp;
                    // arrBillUp = Table.bfget.getLstBill(); // note
                    txtDangChon.setText(String.valueOf(sl));
                    for (int i = 0; i < biltemp.size(); i++) {
                        new Database(getBaseContext()).AddToCart(biltemp.get(i));
                    }
                }
            });
        }
        else
        {
            arrBill = new Database(getBaseContext()).getBillAfterInsert(idTable);
            sl = arrBill.size();
            txtDangChon.setText(String.valueOf(sl));
            for (int i = 0; i < arrBill.size(); i++) {
                new Database(getBaseContext()).AddToCart(arrBill.get(i));
            }
        }
    }

    private void init()
    {


        recyclerView = (RecyclerView)findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        FoodAdapter shopAdapter= new FoodAdapter(arrFood,getApplicationContext(),this);
        recyclerView.setAdapter(shopAdapter);

        LayoutAnimationController controller = null;
        controller = AnimationUtils.loadLayoutAnimation(getApplicationContext(),R.anim.layout_phai);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void GetIDTable()
    {
        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        idTable = bundle.getInt("id");
        // Toast.makeText(getApplicationContext(),"id: " + idTable,Toast.LENGTH_SHORT).show();
    }

    private void GetAllFood(final Themon.FirebaseCallBack firebaseCallBack)
    {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {

                    Food tam = ds.getValue(Food.class);
                    arrFood.add(tam);
                }
                firebaseCallBack.onCallBack(arrFood);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Lỗi kết nối mạng",Toast.LENGTH_SHORT).show();
            }
        };
        food.child("Food").addListenerForSingleValueEvent(valueEventListener);
    }
    public void GetALLListBill(final Themon.FireBaseCallBill firebaseCallBack)
    {


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    BillFirebase tam = ds.getValue(BillFirebase.class);
                    arrBillF.add(tam);
                }
                firebaseCallBack.onCallBack(arrBillF);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        billFire.child("Bill").addListenerForSingleValueEvent(valueEventListener);
    }
    EditText t;
    @Override
    public void OnNoteClick(int position) {

        RecyclerView.ViewHolder v = recyclerView.findViewHolderForLayoutPosition(position);
        t = (EditText) v.itemView.findViewById(R.id.soluong);
        String IDF = arrFood.get(position).getID();


            if (new Database(getBaseContext()).CheckNameFoodEquals(IDF)) {
                int soluong = new Database(getBaseContext()).GetQuantity(IDF);
                soluong += Integer.parseInt(t.getText().toString());
                db.UpdateQuantityFood(IDF, soluong);
                coQuantity = 1;
            } else {
                bills = new Bill(arrFood.get(position).getID(),
                        arrFood.get(position).getName(),
                        t.getText().toString(),
                        arrFood.get(position).getPrice().toString()
                );
                db.AddToCart(bills);
                coQuantity = 0;
            //    arrBillUp.add(bills);
            }


        sl++;
        txtDangChon.setText(String.valueOf(sl));
        // Toast.makeText(getApplicationContext(),"Thành công",Toast.LENGTH_LONG).show();
    }


    private interface FirebaseCallBack{

        void onCallBack(ArrayList<Food> list);
    }
    private  interface  FireBaseCallBill{
        void onCallBack(ArrayList<BillFirebase> list);
    }


}
