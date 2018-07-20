package com.sengelgrup.anket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class Anket extends AppCompatActivity {

    CircleImageView ana_logo;
    TextView ana_baslik;

    String ana_url = "http://213.142.145.51/plesk-site-preview/sengelgrup.com/android/anket_resimler/";

    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anket);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        id = preferences.getString("id", "N/A");

        if (id.equals("N/A"))
        {
            id = "1";
        }

        ayar_getir();

        final RatingBar bir = findViewById(R.id.bir);
        final RatingBar iki = findViewById(R.id.iki);
        final RatingBar uc = findViewById(R.id.uc);
        final RatingBar dort = findViewById(R.id.dort);

        final EditText adsoyad = findViewById(R.id.adsoyad);
        final EditText telefon = findViewById(R.id.telefon);

        ana_baslik = findViewById(R.id.ana_baslik);
        ana_logo = findViewById(R.id.ana_logo);
        Button tamam = findViewById(R.id.tamamla);


        tamam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (adsoyad.getText().toString().trim().equals("admin") && telefon.getText().toString().trim().equals("123"))
                {
                    startActivity(new Intent(Anket.this,Admin.class));
                }
                else
                {

                    if (bir.getRating() != 0 && iki.getRating() != 0 && uc.getRating() != 0 && dort.getRating() != 0)
                    {
                        Response.Listener<String> responselistener = new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {

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
                                                    bir.setRating(0.0f);
                                                    iki.setRating(0.0f);
                                                    uc.setRating(0.0f);
                                                    dort.setRating(0.0f);

                                                    adsoyad.setText("");
                                                    telefon.setText("");
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
                    else
                    {

                    }
                }

            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    void ayar_getir() {


        Response.Listener<String> responselistener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.i("yaz",response);
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

}
