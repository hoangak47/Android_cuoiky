package com.example.quanlyquancf.Lop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlyquancf.DoiTuong.User;
import com.example.quanlyquancf.R;

public class ProfileActivity extends AppCompatActivity {

    TextView ten,email, twiter;
    User temp = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide(); // hide the title bar
        AnhXa();
        GetIntent();
    }
    private  void AnhXa()
    {
        ten = (TextView)findViewById(R.id.tv_name);
        email =(TextView)findViewById(R.id.tv_mail);
        twiter =(TextView)findViewById(R.id.tv_twiter);
    }
    public  void GetIntent()
    {
        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        temp = (User) bundle.getSerializable("prof");
        ten.setText(temp.getDisplayname());
        email.setText(temp.getEmail());
    }
}
