package com.example.quanlyquancf.Adapter;

import android.content.Context;
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
import com.example.quanlyquancf.R;

import java.util.ArrayList;
import java.util.Locale;

public class PaymentAdapter extends  RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    ArrayList<Bill> datashops;
    Context context;

    public PaymentAdapter(ArrayList<Bill> datashops, Context context) {
        this.datashops = datashops;
        this.context = context;
    }

    @NonNull
    @Override
    public PaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView =layoutInflater.inflate(R.layout.item_payment,parent,false);
        return new PaymentAdapter.ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mon.setText(datashops.get(position).getProductName());
        holder.soluong.setText(datashops.get(position).getQuantity());
        Locale locale = new Locale("vi","VN");
        NumberFormat ft = NumberFormat.getCurrencyInstance(locale);
        int price = Integer.parseInt(String.valueOf(datashops.get(position).getPrice()));
        holder.gia.setText(ft.format(price));

        final int currentPosition = position;
        final Bill infoBill = datashops.get(position);
//        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                removeBill(infoBill);
//            }
//        });
    }
//    public  void removeBill(Bill bill)
//    {
//        int vitri = datashops.indexOf(bill);
//        datashops.remove(vitri);
//        Toast.makeText(context,vitri +"",Toast.LENGTH_LONG).show();
//        new Database(context).DeleteRow(vitri+1);
//        String temp =  new Database(context).getID(vitri);
//        Toast.makeText(context,"DB:" + temp,Toast.LENGTH_SHORT).show();
//        notifyDataSetChanged();
//    }

    @Override
    public int getItemCount() {
        return datashops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mon,soluong,gia,tongtien;
        ImageButton imgDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           // imgDelete = (ImageButton)itemView.findViewById(R.id.imageButton);
            mon=(TextView)itemView.findViewById(R.id.txtbillname1);
            soluong=(TextView)itemView.findViewById(R.id.txtbillsoluong1);
            gia=(TextView)itemView.findViewById(R.id.txtbillgia1);

        }

    }
}
