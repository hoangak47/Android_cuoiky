package com.example.quanlyquancf.Lop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanlyquancf.DoiTuong.TableBan;
import com.example.quanlyquancf.DoiTuong.User;
import com.example.quanlyquancf.MainActivity;
import com.example.quanlyquancf.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


import cn.pedant.SweetAlert.SweetAlertDialog;
import io.paperdb.Paper;

public class LoginAccount extends AppCompatActivity {

    Button btnDn;
    EditText txtUser,txtPass;
    CheckBox chkRemember;
    DatabaseReference user;
    public  static String USER_P = "USER_PASS";
    public  static String USER_N = "USER_NAME";
    public  static String USER = "USER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_login_account);
        Paper.init(getApplicationContext());
      //  Paper.book().destroy();
        AnhXa();
        if (isRemember()) {
            txtUser.setText(Paper.book().read(USER_N).toString());
            txtPass.setText(Paper.book().read(USER_P).toString());
            chkRemember.setChecked(true);
        }
        btnDn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


//                    final ProgressDialog progressDialog = new ProgressDialog(LoginAccount.this);
//                    progressDialog.setMessage("Xin chờ ...");
//                    progressDialog.show();
                    final SweetAlertDialog pDialog = new SweetAlertDialog(LoginAccount.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Xin chờ...");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    user.child("User").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user_nv1 = dataSnapshot.child(txtUser.getText().toString()).getValue(User.class);

                            if (dataSnapshot.child(txtUser.getText().toString()).exists()) {
                                if (txtUser.getText().toString().equals(user_nv1.getLoginname()) && txtPass.getText().toString().equals(user_nv1.getPassword())) {
                                    pDialog.dismiss();
                                    // Toast.makeText(getApplicationContext(),"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                                    if (chkRemember.isChecked()) {
                                        Paper.book().write(USER_N, txtUser.getText().toString());
                                        Paper.book().write(USER_P, txtPass.getText().toString());
                                        Paper.book().write(USER, user_nv1);
                                    }
                                    Intent intent = new Intent(getApplicationContext(), Table.class);
                                    Bundle b = new Bundle();
                                    b.putSerializable("user1", user_nv1);
                                    intent.putExtras(b);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else
                                    Toast.makeText(getApplicationContext(), "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();
                            } else {
                                Toast.makeText(getApplicationContext(), "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            new SweetAlertDialog(LoginAccount.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Có lỗi xảy ra! Vui lòng thử lại")
                                    .show();
                        }
                    });

                }
            });

    };

    private void AnhXa()
    {
        // Get database from firebase
        user = FirebaseDatabase.getInstance().getReference();

        chkRemember = (CheckBox)findViewById(R.id.chk1);
        txtUser = (EditText) findViewById(R.id.tass);
        txtPass = (EditText) findViewById(R.id.tass2);
        btnDn = (Button)findViewById(R.id.btnDN);

    }
    private Boolean isRemember()
    {
        if(Paper.exist(USER_N) && Paper.exist(USER_P)) {
            String user = Paper.book().read(USER_N);
            String pass = Paper.book().read(USER_P);
            if (!user.isEmpty() && !pass.isEmpty())
                return true;
            else
                return false;
        }
        else
            return  false;
    }
}
