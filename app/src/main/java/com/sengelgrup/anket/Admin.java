package com.sengelgrup.anket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

public class Admin extends AppCompatActivity implements OnChartValueSelectedListener{


    private String[] sabit_yil = {"2018", "2019", "2020","2021","2022","2023","2024","2025","2026","2027","2028","2029","2030"};
    private String[] sabit = {"OCAK", "ŞUBAT", "MART","NISAN","MAYIS","HAZIRAN","TEMMUZ","AGUSTOS","EYLUL","EKIM","KASIM","ARALIK"};

    PieChart pieChart;
    BarChart barChart;

    String deger = "1";
    String baslangic = "2018-01-01";
    String bitis = "2018-12-30";
    String yil = "2018";

    Integer secim1;
    Integer secim2;
    TextView txt_anket_sayi;
    TextView txt_ort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ImageButton mail = findViewById(R.id.img_ist_mail);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Admin.this,MailAt.class));

            }
        });

        pieChart = findViewById(R.id.piechart);
        barChart = findViewById(R.id.barchart);

        txt_anket_sayi = findViewById(R.id.txt_anket_sayi);
        txt_ort = findViewById(R.id.txt_ort);

        Button sorgu1 = findViewById(R.id.sorgu1);
        Button sorgu2 = findViewById(R.id.sorgu2);
        Button sorgu3 = findViewById(R.id.sorgu3);
        Button sorgu4 = findViewById(R.id.sorgu4);

        ImageButton user = findViewById(R.id.btn_user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin.this,User.class));
            }
        });
        ImageButton ayar = findViewById(R.id.btn_ayar);
        ayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin.this,Ayarlar.class));

            }
        });
        sorgu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deger = "1";
                PieDoldur();
            }
        });
        sorgu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deger = "2";
                PieDoldur();
            }
        });
        sorgu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deger = "3";
                PieDoldur();
            }
        });
        sorgu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deger = "4";
                PieDoldur();
            }
        });

        Button btn_filtre = findViewById(R.id.btn_filtrele);
        Button btn_tumu = findViewById(R.id.btn_tumu);

        AnaDoldur();
        BarDoldur();
        PieDoldur();

        btn_tumu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baslangic = "2018-01-01";
                bitis = "2018-12-30";
                BarDoldur();
            }
        });

        btn_filtre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(Admin.this, baslangic, Toast.LENGTH_SHORT).show();
                Toast.makeText(Admin.this, bitis, Toast.LENGTH_SHORT).show();
                BarDoldur();

            }
        });

        final Spinner bitir = findViewById(R.id.spinner_bitis);
        final Spinner basla = findViewById(R.id.spinner_baslangic);
        final Spinner yillar = findViewById(R.id.spinner_yil);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Admin.this, android.R.layout.simple_spinner_dropdown_item, sabit);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        basla.setAdapter(adapter);
        bitir.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Admin.this, android.R.layout.simple_spinner_dropdown_item, sabit_yil);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yillar.setAdapter(adapter2);

        yillar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yil = sabit_yil[yillar.getSelectedItemPosition()];

                if (secim1 < 10)
                {
                    baslangic = yil+"-0"+secim1.toString()+"-01";
                }
                else
                {
                    baslangic = yil+"-"+secim1.toString()+"-01";
                }

                if (secim2 < 10)
                {

                    bitis = yil+"-0"+secim1.toString()+"-30";
                }
                else
                {

                    bitis = yil+"-"+secim1.toString()+"-30";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        basla.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secim1 = basla.getSelectedItemPosition() + 1;
                if (secim1 < 10)
                {
                    baslangic = yil+"-0"+secim1.toString()+"-01";
                }
                else
                {
                    baslangic = yil+"-"+secim1.toString()+"-01";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bitir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secim2 = bitir.getSelectedItemPosition() + 1;
                if (secim2 < 10)
                {
                    bitis = yil+"-0"+secim2.toString()+"-30";
                }
                else
                {
                    bitis = yil+"-"+secim2.toString()+"-30";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void AnaDoldur()
    {

        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

                try {
                    Log.i("yaz",response);

                    JSONObject jsonresponse = new JSONObject(response);

                    txt_anket_sayi.setText("TOPLAM " + jsonresponse.getString("anket") + " ANKET");
                    txt_ort.setText("ORTALAMA " + jsonresponse.getString("ortalama") + " YILDIZ");

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };

        AnaJSON loginrequest = new AnaJSON(responselistener);
        RequestQueue queue = Volley.newRequestQueue(Admin.this);
        queue.add(loginrequest);
    }

    private void BarDoldur()
    {
        barChart.setDescription("ANKET SAYILARI");

        final ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        final ArrayList<String> xVals = new ArrayList<String>();

        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonresponse = new JSONObject(response);

                    JSONArray aylar = jsonresponse.getJSONArray("aylar");
                    JSONArray veri = jsonresponse.getJSONArray("veri");

                    for (int i = 0; i < aylar.length(); i++)
                    {
                        xVals.add(aylar.get(i).toString());
                        yVals.add(new BarEntry(Float.valueOf(veri.get(i).toString()), i));
                    }

                    final BarDataSet barDataSet = new BarDataSet(yVals,"Data Set");

                    barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
                    barDataSet.setDrawValues(true);
                    BarData data = new BarData(xVals,barDataSet);
                    barChart.setData(data);
                    barChart.invalidate();
                    barChart.animateXY(2000, 2000);



                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };

        BarJSON loginrequest = new BarJSON(baslangic,bitis,responselistener);
        RequestQueue queue = Volley.newRequestQueue(Admin.this);
        queue.add(loginrequest);

    }

    private void PieDoldur()
    {
        pieChart.setUsePercentValues(true);

        final ArrayList<Entry> yvalues = new ArrayList<Entry>();
        final ArrayList<String> xVals = new ArrayList<String>();

        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonresponse = new JSONObject(response);

                    JSONArray veri = jsonresponse.getJSONArray("veri");

                    Float say = 0.5f;
                    for (int i = 0; i < veri.length(); i++)
                    {
                        if (veri.get(i).toString().equals("0"))
                        {

                        }
                        else
                        {
                            xVals.add(say+"Yıldız");
                            yvalues.add(new Entry(Float.valueOf(veri.get(i).toString()), i));

                        }
                        say = say + 0.5f;
                    }

                    PieDataSet dataSet = new PieDataSet(yvalues, "Puan baz alınarak");

                    PieData data = new PieData(xVals, dataSet);
                    // In Percentage term
                    data.setValueFormatter(new PercentFormatter());
                    // Default value
                    //data.setValueFormatter(new DefaultValueFormatter(0));
                    pieChart.setData(data);
                    pieChart.setDescription("PUANA GÖRE");

                    pieChart.setDrawHoleEnabled(true);
                    pieChart.setTransparentCircleRadius(25f);
                    pieChart.setHoleRadius(25f);

                    dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
                    data.setValueTextSize(15f);
                    data.setValueTextColor(Color.DKGRAY);

                    pieChart.setOnChartValueSelectedListener(Admin.this);
                    pieChart.animateXY(2000, 2000);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };

        PieJSON loginrequest = new PieJSON(deger,responselistener);
        RequestQueue queue = Volley.newRequestQueue(Admin.this);
        queue.add(loginrequest);

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;

        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }
}
