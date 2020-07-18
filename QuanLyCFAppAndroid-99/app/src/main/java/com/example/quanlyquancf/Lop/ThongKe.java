package com.example.quanlyquancf.Lop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.quanlyquancf.DoiTuong.BillFirebase;
import com.example.quanlyquancf.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ThongKe extends AppCompatActivity {

    ArrayList<BillFirebase> lstb = new ArrayList<>();
    DatabaseReference billf;
    BarChart barChart,barChart2;
    LineChart lnc;
    TextView sang,chieu,toi,tongcong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screenr
        setContentView(R.layout.activity_thong_ke);

        billf = FirebaseDatabase.getInstance().getReference();
        AnhXA();
        GetALLListBill(new ThongKe.FirebaseCallBack() {

            @Override
            public void onCallBack(ArrayList<BillFirebase> list) {
                lstb = list;
                setBarChartNgay();
            }
        });

        setBarTuan();
        setLineChar();

        // Date picker

//        Button btnBD = (Button)findViewById(R.id.btnBD);
//        Button btnKT = (Button)findViewById(R.id.btnKT);
//        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
//        builder.setTitleText("Chọn ngày BD");
//        final  MaterialDatePicker materialDatePicker = builder.build();
//        btnBD.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
//            }
//        });
//        btnKT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        //--------------------------------------
    }
    private void AnhXA()
    {
        sang = (TextView)findViewById(R.id.txtSang);
        chieu = (TextView)findViewById(R.id.txtChieu);
        toi = (TextView)findViewById(R.id.txtToi);
        tongcong = (TextView)findViewById(R.id.txtTong);
    }
    private void setBarChartNgay()
    {
        barChart = (BarChart)findViewById(R.id.barNgay);
        // remove line and lable
        barChart.setDrawGridBackground(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);

        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawAxisLine(false);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisRight().setDrawLabels(false);



        BarDataSet barDataSet = new BarDataSet(dataBarNgay(),"");

        barDataSet.setDrawValues(false); // remove label
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData bar = new BarData(barDataSet);
        bar.setDrawValues(false);

        barChart.setFitBars(true);
        barChart.setData(bar);
        barChart.getLegend().setEnabled(false); // remove cube in above bar char
        barChart.getDescription().setEnabled(false);
        barChart.setDrawValueAboveBar(false); // remove value
        barChart.animateY(2000);
        barChart.animateX(2000);
        // -----------------------------------------------------
    }
    private  void setBarTuan()
    {
        barChart2 = (BarChart)findViewById(R.id.barTuan);


        BarDataSet barDataSet2 = new BarDataSet(dataBarTuan(),"");

        //barDataSet2.setDrawValues(false); // remove label
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet2.setValueTextColor(Color.BLACK);
        barDataSet2.setValueTextSize(10f);

        BarData bar2 = new BarData(barDataSet2);
        //bar.setDrawValues(false);
        barChart2.setFitBars(true);
        //  bar2.setDrawValues(false);
        barChart2.getDescription().setEnabled(false);
        barChart2.setData(bar2);
    }

    private void setLineChar(){
        //------------------------------------------------------------

        // Line chart
        lnc = (LineChart)findViewById(R.id.linechart);
        lnc.getXAxis().setDrawAxisLine(false);

        lnc.getXAxis().setDrawGridLines(false);
        //  lnc.getXAxis().setEnabled(false);
        lnc.getAxisRight().setEnabled(false);

        //  lnc.getAxisLeft().setDrawGridLines(false);
        //   lnc.getAxisLeft().setDrawAxisLine(false);
        lnc.getAxisRight().setDrawGridLines(false);
        //   lnc.getAxisRight().setDrawAxisLine(false);
        lnc.getDescription().setEnabled(false);

        LineDataSet lineDataSet = new LineDataSet(dataLineChart(),"Thu nhập từng tháng (VNĐ)");
        //  lineDataSet.setDrawValues(false);
        lineDataSet.setValueTextSize(10f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);
        lnc.setData(data);
        lnc.invalidate();
        //-----------------------------
    }

    private  ArrayList<BarEntry> dataBarNgay()
    {
        ArrayList<BarEntry> tet = new ArrayList<>();

        ArrayList<Long> teo  = thongKeNgay(lstb);
        tet.add(new BarEntry(1,teo.get(0)));
        tet.add(new BarEntry(2,teo.get(1)));
        tet.add(new BarEntry(3,teo.get(2)));
        return  tet;
    }
    private  ArrayList<BarEntry> dataBarTuan()
    {
        ArrayList<BarEntry> tet2 = new ArrayList<>();
        tet2.add(new BarEntry(2,42000));
        tet2.add(new BarEntry(3,72000));
        tet2.add(new BarEntry(4,62000));
        tet2.add(new BarEntry(5,82000));
        tet2.add(new BarEntry(6,9200));
        tet2.add(new BarEntry(7,12000));
        tet2.add(new BarEntry(9,32000));
        return  tet2;
    }
    private ArrayList<Entry> dataLineChart()
    {
        ArrayList<Entry> temp = new ArrayList<>();
        temp.add(new Entry(1,60000));
        temp.add(new Entry(2,270000));
        temp.add(new Entry(3,56000));
        temp.add(new Entry(4,8000));
        temp.add(new Entry(5,96000));
        temp.add(new Entry(6,25000));
        temp.add(new Entry(7,43000));
        temp.add(new Entry(8,79000));
        temp.add(new Entry(9,5000));
        temp.add(new Entry(10,86000));
        temp.add(new Entry(11,63000));
        temp.add(new Entry(12,12000));
        return  temp;
    }
    public void GetALLListBill(final ThongKe.FirebaseCallBack firebaseCallBack)
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
    private interface  FirebaseCallBack{

        void onCallBack(ArrayList<BillFirebase> list);
    }
    public ArrayList<Long> thongKeNgay(ArrayList<BillFirebase> temp)
    {
        ArrayList<Long> tk = new ArrayList<>();
        Long tongsang = 0L;
        Long tongchieu = 0L;
        Long tongtoi = 0L;



        ArrayList<BillFirebase> bf = new ArrayList<>();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        String[] spitTime = currentTime.split(":");

        for(int i = 0;i<temp.size();i++)
        {
            if(currentDate.equals(XuLyNgay(temp.get(i).getDateCheckOut())))
            {
                bf.add(temp.get(i));
            }
        }

        for(int i = 0 ;i<bf.size();i++)
        {
            String[] teo = bf.get(i).getDateCheckOut().split("_");
            String[] gio = teo[1].split(":");
            if(Integer.parseInt(gio[0]) > 0 &&  Integer.parseInt(gio[0]) < 12)
            {

                tongsang += Long.parseLong(XuLyTien(bf.get(i).getTotal()));
            }
            else if (Integer.parseInt(gio[0]) >= 12 &&  Integer.parseInt(gio[0]) <= 17)
            {
                tongchieu += Long.parseLong(XuLyTien(bf.get(i).getTotal()));
            }
            else
            {
                tongtoi += Long.parseLong(XuLyTien(bf.get(i).getTotal()));
            }
        }
        Locale locale = new Locale("vi","VN");
        final NumberFormat ft = NumberFormat.getCurrencyInstance(locale);

        sang.setText(String.valueOf(ft.format(tongsang)));
        chieu.setText(String.valueOf(ft.format(tongchieu)));
        toi.setText(String.valueOf(ft.format(tongtoi)));
        tongcong.setText(String.valueOf(ft.format(tongsang+tongchieu+tongtoi)) );
        tk.add(tongsang);
        tk.add(tongchieu);
        tk.add(tongtoi);
        return tk;
    }



    public String XuLyNgay(String teo)
    {
        String[] ngay = teo.split("_");
        return  ngay[0];
    }
    private String XuLyTien(String teo)
    {
        Locale locale = new Locale("vi","VN");
        Number n= 0;
        final NumberFormat ft = NumberFormat.getCurrencyInstance(locale);
        try {
            n = ft.parse(teo);
        }
        catch (Exception e){}

        return  String.valueOf(n);
    }
}
