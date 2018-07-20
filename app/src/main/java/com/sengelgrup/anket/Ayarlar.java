package com.sengelgrup.anket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Ayarlar extends AppCompatActivity {

    ArrayList<String> secenek_id = new ArrayList<>();
    ArrayList<String> secenek_baslik = new ArrayList<>();
    Spinner ayar;


    String resim_kodu = "";
    String mail;
    String baslik;

    String secilen;

    CircleImageView img_logo;
    private int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String resim_cek = preferences.getString("resim", "N/A");
        String baslik_cek = preferences.getString("baslik", "N/A");
        String mail_cek = preferences.getString("mail", "N/A");

        final EditText txt_baslik = findViewById(R.id.ayar_baslik);
        final EditText txt_mail = findViewById(R.id.ayar_mail);
        img_logo = findViewById(R.id.ayar_logo);

        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFileChooser();
            }
        });

        Picasso.get().load(resim_cek).into(img_logo);

        txt_baslik.setHint(baslik_cek);
        txt_mail.setHint(mail_cek);

        final Button tamam = findViewById(R.id.ayar_btn);
        tamam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baslik = txt_baslik.getText().toString().trim();
                mail = txt_mail.getText().toString().trim();

                tamamla();
            }
        });

        Button ayar_uygula = findViewById(R.id.ayar_uygula);
        ayar_uygula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("id", secilen);

                editor.commit();
                startActivity(new Intent(Ayarlar.this,Anket.class));
            }
        });

        ayar = findViewById(R.id.spinner_ayar);
        secenekler();

        ayar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secilen = secenek_id.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    private String encodeImage(Bitmap bm)
    {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,80,baos);


        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Logo seçin"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView

                Bitmap btmp = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                resim_kodu = encodeImage(btmp);

                img_logo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void secenekler()
    {
        secenek_id.clear();
        secenek_baslik.clear();

        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

                try {

                    Log.i("yaz",response);
                    JSONObject jsonresponse = new JSONObject(response);

                    JSONArray id = jsonresponse.getJSONArray("idler");
                    JSONArray baslik = jsonresponse.getJSONArray("basliklar");

                    for (int i = 0; i < baslik.length(); i++)
                    {
                        secenek_baslik.add(baslik.get(i).toString());
                        secenek_id.add(id.get(i).toString());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Ayarlar.this, android.R.layout.simple_spinner_dropdown_item, secenek_baslik);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ayar.setAdapter(adapter);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };

        SecenekJSON loginrequest = new SecenekJSON(responselistener);
        RequestQueue queue = Volley.newRequestQueue(Ayarlar.this);
        queue.add(loginrequest);
    }
    void tamamla()
    {
        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonresponse = new JSONObject(response);

                    Integer hata = jsonresponse.getInt("hata");
                    if (hata == 0)
                    {
                        new LovelyStandardDialog(Ayarlar.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                                .setTopColorRes(R.color.colorPrimary)
                                .setButtonsColorRes(R.color.colorAccent)
                                .setIcon(R.drawable.checked)
                                .setTitle("Sengel Grup")
                                .setCancelable(false)
                                .setMessage("Ayarlar uygulandı!")
                                .setPositiveButton("Kapat", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        secenekler();
                                    }
                                })
                                .show();

                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };

        AyarJSON loginrequest = new AyarJSON(resim_kodu,baslik,mail,responselistener);
        RequestQueue queue = Volley.newRequestQueue(Ayarlar.this);
        queue.add(loginrequest);
    }
}
