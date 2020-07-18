package com.example.quanlyquancf.Lop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlyquancf.Database.Database;
import com.example.quanlyquancf.DoiTuong.BillFirebase;
import com.example.quanlyquancf.DoiTuong.Billkey;
import com.example.quanlyquancf.DoiTuong.TableBan;
import com.example.quanlyquancf.DoiTuong.User;
import com.example.quanlyquancf.MainActivity;
import com.example.quanlyquancf.PluginEx.IconizedMenu;
import com.example.quanlyquancf.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.paperdb.Paper;

public class Table extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TableLayout tb;
    private DrawerLayout drawer;
    DatabaseReference table,billinfo;
    TextView ten,email;
    ArrayList<TableBan> banList = new ArrayList<>();
    public static User temp = new User();
    public static BillFirebase bfget ,thanhtoan;
    public static String keys;
    IconizedMenu PopupMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_table);

        new Database(getBaseContext()).ClearBill();
        Toolbar toolbar = findViewById(R.id.tlb1);
        table = FirebaseDatabase.getInstance().getReference();
        billinfo = FirebaseDatabase.getInstance().getReference();
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Get nav
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        ten = (TextView) header.findViewById(R.id.labTen);
        email  = (TextView) header.findViewById(R.id.labEmail);
        SetText();
        // ----
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_table, null);
        tb = (TableLayout) findViewById(R.id.tb1);
        GetAllTable(new FirebaseCallBack() {

            @Override
            public void onCallBack(ArrayList<TableBan> list) {
                banList = list;
                SetTable();
            }
        });
     //   SetTable();

    }
    private void GetAllTable(final FirebaseCallBack firebaseCallBack)
    {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {

                    TableBan tam = ds.getValue(TableBan.class);
                    banList.add(tam);
                }
                firebaseCallBack.onCallBack(banList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Lỗi kết nối mạng",Toast.LENGTH_LONG).show();
            }
        };
        table.child("Table").addListenerForSingleValueEvent(valueEventListener);
    }
    private void SetTable()
    {

        for(int i = 0;i<banList.size();i++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    200, 1f);
            params.setMargins(10, 10, 10, 10);

            final int idTable = i+1 ;
            if (i % 2 == 0)
            {
                final Button myButton = new Button(this);
                if(banList.get(i).getStatus().equals("Trống")) {

                    myButton.setText(banList.get(i).getName());
                    myButton.setHeight(80);
                    myButton.setId(Integer.parseInt(banList.get(i).getID()));
                    myButton.setBackgroundResource(R.drawable.ic_bo_tron_nut_2);
                    myButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                         //   Toast.makeText(getApplicationContext(), "Bàn " + myButton.getId(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), FoodActivity.class);
                            Bundle b = new Bundle();
                            b.putInt("id", idTable);
                            intent.putExtras(b);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    });
                }
                else
                {
                    myButton.setText(banList.get(i).getName());
                    myButton.setHeight(80);
                    myButton.setId(Integer.parseInt(banList.get(i).getID()));
                    myButton.setBackgroundResource(R.drawable.ic_bo_tron_nut_3);
                    myButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            keys = getBillKey(idTable);
                            SetmenuButton(myButton,idTable);
//                            Intent intent = new Intent(getApplicationContext(), ThanhtoanActivity.class);
//                            Bundle b = new Bundle();
//                            b.putInt("id", idTable);
//                            intent.putExtras(b);
//                            startActivity(intent);
                        }
                    });
                }
                final Button myButton2 = new Button(this);

                if (i + 1 < banList.size()) {
                    if(banList.get(i+1).getStatus().equals("Trống")) {

                        myButton2.setText(banList.get(i + 1).getName());
                        myButton2.setHeight(80);
                        myButton2.setId(Integer.parseInt(banList.get(i + 1).getID()));
                        myButton2.setBackgroundResource(R.drawable.ic_bo_tron_nut_2);
                        myButton2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               // Toast.makeText(getApplicationContext(), "Bàn " + myButton2.getId(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), FoodActivity.class);
                                Bundle b = new Bundle();
                                b.putInt("id", idTable+1);
                                intent.putExtras(b);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                                startActivity(intent);
                            }
                        });
                    }
                    else
                    {
                        myButton2.setText(banList.get(i+1).getName());
                        myButton2.setHeight(80);
                        myButton2.setId(Integer.parseInt(banList.get(i+1).getID()));
                        myButton2.setBackgroundResource(R.drawable.ic_bo_tron_nut_3);
                        myButton2.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            keys = getBillKey(idTable + 1);
                                                            SetmenuButton(myButton2,idTable+1);

//                                                            Intent intent = new Intent(getApplicationContext(), ThanhtoanActivity.class);
//                                                            Bundle b = new Bundle();
//                                                            b.putInt("id", idTable+1);
//                                                            intent.putExtras(b);
//
//                                                            startActivity(intent);
                                                        }});
                    }
                    myButton2.setLayoutParams(params);
                }


                myButton.setLayoutParams(params);

                TableRow tr1 = new TableRow(this);
                tr1.addView(myButton, params);
                if (i + 1 < banList.size()){ tr1.addView(myButton2, params);}
                tb.addView(tr1, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_profile:
                Intent pr=  new Intent(getApplicationContext(),ProfileActivity.class);
                Bundle bb=  new Bundle();
                bb.putSerializable("prof",temp);
                pr.putExtras(bb);
                startActivity(pr);
                break;

            case  R.id.nav_message: {
                Intent chatbot=  new Intent(getApplicationContext(), MainActivity.class);
                startActivity(chatbot);

                break;
            }
            case  R.id.nav_thongke:
                Intent s1= new Intent(getApplicationContext(),ThongKe.class);
                startActivity(s1);
                break;

            case  R.id.nav_logout: {
                Paper.book().destroy();
             Intent s= new Intent(getApplicationContext(),LoginAccount.class);
             s.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
             startActivity(s);
            }

        }
        //close navigation drawer
       // mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private interface  FirebaseCallBack{

        void onCallBack(ArrayList<TableBan> list);
    }

    private  void SetText()
    {
        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        temp = (User) bundle.getSerializable("user1");
        ten.setText(temp.getDisplayname());
        email.setText(temp.getEmail());
    }
    private void SetmenuButton(View anchorView, final int idT)
    {
        PopupMenu = new IconizedMenu(getWindow().getContext(), anchorView);
        Menu menu1 = PopupMenu.getMenu();

        MenuInflater inflater = PopupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_button, PopupMenu.getMenu());
        PopupMenu.show();
        PopupMenu.setOnMenuItemClickListener(new IconizedMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.action_hoadon:

                        Intent intent = new Intent(getApplicationContext(), ThanhtoanActivity.class);
                        Bundle b = new Bundle();
                        b.putInt("id", idT);
                        intent.putExtras(b);
                        startActivity(intent);
                        break;
                    case R.id.action_themmon:

//                        billinfo.child("Bill").child(keys);
//                        ValueEventListener eventListener =new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                bfget = dataSnapshot.getValue(BillFirebase.class);
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                Log.d("TAG", databaseError.getMessage());
//                            }
//                        };
//                        billinfo.addListenerForSingleValueEvent(eventListener);
                        Intent intent1 = new Intent(getApplicationContext(), Themon.class);
                        Bundle b1 = new Bundle();
                        b1.putInt("id", idT);

                        intent1.putExtras(b1);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity(intent1);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private String getBillKey(int idTable)
    {
        String key = "";
        ArrayList<Billkey> temp1 = new Database(getBaseContext()).getKey();
        for(int i = 0; i<temp1.size();i++)
        {
            if(temp1.get(i).getIDTable().compareTo(String.valueOf(idTable)) == 0)
            {
                key = temp1.get(i).getKey();
                break;
            }
        }
        return key;
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Database(getBaseContext()).ClearBill();
       // Toast.makeText(getApplicationContext(),"tiep tuc",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Database(getBaseContext()).ClearBill();
    }


}
