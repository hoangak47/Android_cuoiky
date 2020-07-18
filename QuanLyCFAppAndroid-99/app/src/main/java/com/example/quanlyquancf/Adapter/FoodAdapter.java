package com.example.quanlyquancf.Adapter;

import android.content.Context;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancf.DoiTuong.Food;
import com.example.quanlyquancf.Lop.FoodActivity;
import com.example.quanlyquancf.Lop.LoginAccount;
import com.example.quanlyquancf.MainActivity;
import com.example.quanlyquancf.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    ArrayList<Food> datashops;
    Context context;
    int dem;
    private OnItemReClickListener mClick;


    public FoodAdapter(ArrayList<Food> datashops, Context context, OnItemReClickListener mClick) {
        this.datashops = datashops;
        this.context = context;
        this.mClick = mClick;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_product_list,parent,false);
        return new FoodAdapter.ViewHolder(itemView,mClick);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Picasso.get().load(datashops.get(position).getImage()).into(holder.imageView);
        holder.textView.setText(datashops.get(position).getName());

        Locale locale = new Locale("vi","VN");
        NumberFormat ft = NumberFormat.getCurrencyInstance(locale);
        int price = Integer.parseInt(String.valueOf(datashops.get(position).getPrice()));
        holder.textView2.setText(ft.format(price));
        holder.textView.setMaxLines(2);
        holder.textView.setEllipsize(TextUtils.TruncateAt.END);

    }

    @Override
    public int getItemCount() {
        return datashops.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView,textView2;
        ImageView imageView;
        OnItemReClickListener mmclick;
        public ViewHolder(@NonNull View itemView,OnItemReClickListener mmclick) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.hinh);
            textView= (TextView)itemView.findViewById(R.id.txtname);
            textView2= (TextView)itemView.findViewById(R.id.txtprice);
            this.mmclick = mmclick;
            itemView.setOnClickListener(this);

            final EditText sl=(EditText) itemView.findViewById(R.id.soluong);
            Button tang=(Button)itemView.findViewById(R.id.btntang);
            Button giam=(Button)itemView.findViewById(R.id.btngiam);
            sl.setText("1");
         //   dem = Integer.parseInt(sl.getText() + "");
            tang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dem = Integer.parseInt(sl.getText() + "");
                    dem++;
                    sl.setText(dem+"");
                }
            });
            giam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dem > 1)
                    {
                        dem = Integer.parseInt(sl.getText() + "");
                        dem -=1 ;
                        sl.setText(dem + "");
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
        mmclick.OnNoteClick(getAdapterPosition());
        }
    }

    public  interface OnItemReClickListener{
        void OnNoteClick(int position);
    }


}
