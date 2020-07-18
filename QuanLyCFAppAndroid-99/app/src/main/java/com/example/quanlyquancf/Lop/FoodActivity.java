package com.example.quanlyquancf.Lop;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancf.Adapter.FoodAdapter;
import com.example.quanlyquancf.Database.Database;
import com.example.quanlyquancf.DoiTuong.Bill;
import com.example.quanlyquancf.DoiTuong.BillFirebase;
import com.example.quanlyquancf.DoiTuong.Food;
import com.example.quanlyquancf.DoiTuong.TableBan;
import com.example.quanlyquancf.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FoodActivity extends AppCompatActivity implements FoodAdapter.OnItemReClickListener {

    DatabaseReference food;
    ArrayList<Food> arrFood=new ArrayList<>();
    EditText txtSL;
    int idTable;
    RecyclerView recyclerView;
    Button btnBill,btnCHonLai;
    Database db;
    Bill bills ;
    DatabaseReference billFire;
    TextView txtDangChon;

    public static int sl = 0;
    public static int co = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
//        getSupportActionBar().hide(); // hide the title bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activiti_food);

        db = new Database(getBaseContext());
        btnCHonLai = (Button)findViewById(R.id.btnChonLai);
        btnCHonLai.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              db.ClearBill();
                                              sl = 0;
                                              finish();
                                          }
                                      });
        txtDangChon = (TextView) findViewById(R.id.txtDangChon);
        txtSL = (EditText)findViewById(R.id.soluong);
        food = FirebaseDatabase.getInstance().getReference();
        btnBill = (Button)findViewById(R.id.btnXNBill);
        GetIDTable();


        btnBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new Database(getBaseContext()).isNotEmptyBill()) {
                    Intent s = new Intent(getApplicationContext(), BillActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("idT", idTable);
                    s.putExtras(b);
                    startActivity(s);
                }
                else
                {
                    new SweetAlertDialog(FoodActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Vui lòng chọn món!")
                            .show();
                }
            }
        });
        GetAllFood(new FirebaseCallBack() {
            @Override
            public void onCallBack(ArrayList<Food> list) {
                arrFood = list;
                //     SetTable();
                init();
            }
        });

    }
    private void init()
    {
        billFire = FirebaseDatabase.getInstance().getReference("Bill");
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        FoodAdapter shopAdapter= new FoodAdapter(arrFood,getApplicationContext(),this);
        recyclerView.setAdapter(shopAdapter);

        //animation
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

    private void GetAllFood(final FoodActivity.FirebaseCallBack firebaseCallBack)
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
    EditText t;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void OnNoteClick(int position) {

        RecyclerView.ViewHolder v = recyclerView.findViewHolderForLayoutPosition(position);
        t = (EditText)v.itemView.findViewById(R.id.soluong);

        String IDF = arrFood.get(position).getID();

        if(new  Database(getBaseContext()).isNotEmptyBill()) {

            if (new Database(getBaseContext()).CheckNameFoodEquals(IDF)) {
                int soluong = new Database(getBaseContext()).GetQuantity(IDF);
                soluong += Integer.parseInt(t.getText().toString());
                db.UpdateQuantityFood(IDF, soluong);
                co = 1;
            } else {
                bills = new Bill(arrFood.get(position).getID(),
                        arrFood.get(position).getName(),
                        t.getText().toString(),
                        arrFood.get(position).getPrice().toString()
                );
                db.AddToCart(bills);
                co = 0;
            }
        }
        else
        {
            bills = new Bill(arrFood.get(position).getID(),
                    arrFood.get(position).getName(),
                    t.getText().toString(),
                    arrFood.get(position).getPrice().toString()
            );
            db.AddToCart(bills);
        }
        sl++;
        Animation animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoomin);
        txtDangChon.startAnimation(animZoomIn);
        txtDangChon.setText(String.valueOf(sl));
    }


    private interface  FirebaseCallBack{

        void onCallBack(ArrayList<Food> list);
    }


}