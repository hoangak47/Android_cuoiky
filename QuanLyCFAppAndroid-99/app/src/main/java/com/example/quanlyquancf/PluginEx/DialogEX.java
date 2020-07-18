package com.example.quanlyquancf.PluginEx;

import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DialogEX extends AppCompatActivity {
    private DialogEX(){}
    public void SetDialog(String title , String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(DialogEX.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }) .show();
    }
    public  void temp()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure to Exit")

                .setMessage("Exiting will call finish() method")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                        Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

}
