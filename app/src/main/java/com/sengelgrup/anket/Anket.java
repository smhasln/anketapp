package com.sengelgrup.anket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;


public class Anket extends AppCompatActivity {

    class CheckConnection extends TimerTask
    {
        private Context context;

        public CheckConnection(Context context)
        {
            this.context = context;
        }

        public void run()
        {
            if(NetworkUtils.isNetworkAvailable(context))
            {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                Float cek_bir = preferences.getFloat("tut_bir",0.0f);
                Float cek_iki = preferences.getFloat("tut_bir",0.0f);
                Float cek_uc = preferences.getFloat("tut_bir",0.0f);
                Float cek_dort = preferences.getFloat("tut_bir",0.0f);

                if (cek_bir.equals(0.0f)){

                }
                else
                {
                    Response.Listener<String> responselistener = new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject jsonresponse = new JSONObject(response);

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = preferences.edit();

                                editor.putFloat("tut_bir", 0.0f);
                                editor.putFloat("tut_iki", 0.0f);
                                editor.putFloat("tut_uc", 0.0f);
                                editor.putFloat("tut_dort", 0.0f);
                                editor.putString("tut_isim", "");
                                editor.putString("tut_tel", "");

                                editor.commit();

                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }

                    };

                    AnketJSON loginrequest = new AnketJSON(cek_bir,cek_iki,cek_uc,cek_dort,
                            preferences.getString("tut_isim","N/A"),
                            preferences.getString("tut_tel","N/A"), responselistener);

                    RequestQueue queue = Volley.newRequestQueue(Anket.this);
                    queue.add(loginrequest);
                }
                }


            else
            {

            }
        }
    }


    CircleImageView ana_logo;
    TextView ana_baslik;

    String ana_url = "http://213.142.145.51/plesk-site-preview/sengelgrup.com/android/anket_resimler/";

    String id;

    RatingBar bir;
    RatingBar iki;
    RatingBar uc;
    RatingBar dort;

    EditText adsoyad;
    EditText telefon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anket);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Timer timer = new Timer();
        final int MILLISECONDS = 5000; //5 seconds
        timer.schedule(new CheckConnection(this), 0, MILLISECONDS);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        id = preferences.getString("id", "N/A");

        if (id.equals("N/A"))
        {
            id = "1";
        }

        ayar_getir();

        bir = findViewById(R.id.bir);
        iki = findViewById(R.id.iki);
        uc = findViewById(R.id.uc);
        dort = findViewById(R.id.dort);

        adsoyad = findViewById(R.id.adsoyad);
        telefon = findViewById(R.id.telefon);

        ana_baslik = findViewById(R.id.ana_baslik);
        ana_logo = findViewById(R.id.ana_logo);
        Button tamam = findViewById(R.id.tamamla);

        tamam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (adsoyad.getText().toString().trim().equals("admin") && telefon.getText().toString().trim().equals("123")) {
                    startActivity(new Intent(Anket.this,Admin.class));
                }
                else {

                    if(!NetworkUtils.isNetworkAvailable(getApplicationContext()))
                    {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putFloat("tut_bir", bir.getRating());
                        editor.putFloat("tut_iki", iki.getRating());
                        editor.putFloat("tut_uc", uc.getRating());
                        editor.putFloat("tut_dort", dort.getRating());
                        editor.putString("tut_isim", adsoyad.getText().toString());
                        editor.putString("tut_tel", telefon.getText().toString());

                        editor.commit();

                        resetle();

                    }
                    else
                    {
                        gonder();
                    }

                }
            }
        });

    }


    void resetle(){


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        bir.setRating(0.0f);
        iki.setRating(0.0f);
        uc.setRating(0.0f);
        dort.setRating(0.0f);

        adsoyad.setText("");
        telefon.setText("");
    }


    void gonder()
    {
        if (bir.getRating() != 0 && iki.getRating() != 0 && uc.getRating() != 0 && dort.getRating() != 0)
        {
            Response.Listener<String> responselistener = new Response.Listener<String>()
            {
                @Override
                public void onResponse(final String response) {

                    try {

                        JSONObject jsonresponse = new JSONObject(response);

                        new LovelyStandardDialog(Anket.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                                .setTopColorRes(R.color.colorPrimary)
                                .setButtonsColorRes(R.color.colorAccent)
                                .setIcon(R.drawable.checked)
                                .setTitle("Sengel Grup")
                                .setCancelable(false)
                                .setMessage("Görüşlerinizi paylaştığınız için teşekkürler!")
                                .setPositiveButton("Kapat", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                       resetle();
                                    }
                                })
                                .show();

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

            };

            AnketJSON loginrequest = new AnketJSON(bir.getRating(),iki.getRating(),uc.getRating(),dort.getRating(),adsoyad.getText().toString().trim(),telefon.getText().toString().trim(),responselistener);
            RequestQueue queue = Volley.newRequestQueue(Anket.this);
            queue.add(loginrequest);
        }
    }

    void ayar_getir() {


        Response.Listener<String> responselistener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonresponse = new JSONObject(response);

                    String baslik = jsonresponse.getString("baslik");
                    String resim = jsonresponse.getString("resim") + ".jpg";
                    String mail = jsonresponse.getString("mail");

                    ana_baslik.setText(baslik);
                    Picasso.get().load(ana_url+resim).into(ana_logo);


                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString("id", id);
                    editor.putString("baslik", baslik);
                    editor.putString("resim", ana_url+resim);
                    editor.putString("mail", mail);

                    editor.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        AyarGetirJSON loginrequest = new AyarGetirJSON(id, responselistener);
        RequestQueue queue = Volley.newRequestQueue(Anket.this);
        queue.add(loginrequest);
    }

    @Override
    public void onBackPressed() {

    }
}
