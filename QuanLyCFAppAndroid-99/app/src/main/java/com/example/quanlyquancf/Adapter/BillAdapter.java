package com.example.quanlyquancf.Adapter;

import android.content.Context;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancf.Database.Database;
import com.example.quanlyquancf.DoiTuong.Bill;

import java.util.ArrayList;
import java.util.Locale;

import com.example.quanlyquancf.R;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {
    ArrayList<Bill> datashops;
    Context context;
    public static  long co = 0,tong = 0,tongbt = 0;
    public static  int coXoa =0;
    public BillAdapter(ArrayList<Bill> datashops, Context context) {
        this.datashops = datashops;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView =layoutInflater.inflate(R.layout.item_bill,parent,false);
        return new ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mon.setText(datashops.get(position).getProductName());
        holder.soluong.setText(datashops.get(position).getQuantity());

        Locale locale = new Locale("vi","VN");
        final NumberFormat ft = NumberFormat.getCurrencyInstance(locale);
        int price = Integer.parseInt(String.valueOf(datashops.get(position).getPrice()));
        holder.gia.setText(ft.format(price));
       tongbt = new Database(context).getGia();
        final int currentPosition = position;
        final Bill infoBill = datashops.get(position);
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getItemCount() > 1) {
                    removeBill(infoBill);
                }
                else
                {
                    co = 1;
                }
            }
        });

    }
    public ArrayList<Bill> ReloadBill()
    {
        return  new Database(context).getBill();
    }
    public void LoadBill(ArrayList<Bill> lstbl)
    {
        for(int i = 0 ;i<lstbl.size();i++)
        {
            new Database(context).AddToCart(lstbl.get(i));
        }
    }
    public  void removeBill(Bill bill)
    {
        int vitri = datashops.indexOf(bill);
        datashops.remove(vitri);
        final ArrayList<Bill> lstBill = ReloadBill();
        new Database(context).ClearBill();
        LoadBill(lstBill);
        new Database(context).DeleteRow(vitri+1);
        tong = new Database(context).getGia();
        //Toast.makeText(context,tong+"",Toast.LENGTH_SHORT).show();

        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return datashops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mon,soluong,gia;
        ImageButton imgDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgDelete = (ImageButton)itemView.findViewById(R.id.imageButton);
            mon=(TextView)itemView.findViewById(R.id.txtbillname);
            soluong=(TextView)itemView.findViewById(R.id.txtbillsoluong);
            gia=(TextView)itemView.findViewById(R.id.txtbillgia);
        }

    }
}
