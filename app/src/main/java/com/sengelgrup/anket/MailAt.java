package com.sengelgrup.anket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MailAt extends AppCompatActivity {

    String giden_kisi = "";
    String giden_mesaj = "10";
    private int PICK_IMAGE_REQUEST = 1;

    ImageView mail_resim;
    EditText kisi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_at);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mail_resim = findViewById(R.id.mail_img);

        Button btn_sec = findViewById(R.id.mail_galeri);
        Button btn_resim_gonder = findViewById(R.id.mail_gonder);
        Button btn_kullanici_gonder = findViewById(R.id.mail_gonder2);

        btn_resim_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (kisi.getText().toString().trim().isEmpty())
                {
                    new LovelyStandardDialog(MailAt.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                            .setTopColorRes(R.color.colorPrimary)
                            .setButtonsColorRes(R.color.colorAccent)
                            .setIcon(R.drawable.ic_error_outline_black_24dp)
                            .setTitle("Mail İşlemi")
                            .setCancelable(false)
                            .setMessage("Mail gönderilecek adres yazılmalı!")
                            .setPositiveButton("Kapat", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            })
                            .show();
                }
                else
                {
                    giden_kisi = kisi.getText().toString().trim();

                    gonder();
                }
            }
        });

        btn_kullanici_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kisi.getText().toString().trim().isEmpty())
                {

                    new LovelyStandardDialog(MailAt.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                            .setTopColorRes(R.color.colorPrimary)
                            .setButtonsColorRes(R.color.colorAccent)
                            .setIcon(R.drawable.ic_error_outline_black_24dp)
                            .setTitle("Mail İşlemi")
                            .setCancelable(false)
                            .setMessage("Mail gönderilecek adres yazılmalı!")
                            .setPositiveButton("Kapat", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            })
                            .show();
                }
                else
                {
                    giden_kisi = kisi.getText().toString().trim();

                    giden_mesaj = "10";
                    gonder();
                }
            }
        });

        btn_sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        kisi = findViewById(R.id.mail_adresi);
    }

    void gonder(){

        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(final String response) {

                try {

                    JSONObject jsonresponse = new JSONObject(response);

                    new LovelyStandardDialog(MailAt.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                            .setTopColorRes(R.color.colorPrimary)
                            .setIcon(R.drawable.ic_error_outline_black_24dp)
                            .setIcon(R.drawable.checked)
                            .setTitle("Mail İşlemi")
                            .setCancelable(false)
                            .setMessage(giden_kisi + " kişisine mail gönderildi!")
                            .setPositiveButton("Kapat", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
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

        MailJSON loginrequest = new MailJSON(giden_mesaj,giden_kisi,responselistener);
        RequestQueue queue = Volley.newRequestQueue(MailAt.this);
        queue.add(loginrequest);
    }
    private String encodeImage(Bitmap bm)
    {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);

        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Mail atılacak resmi seçin"), PICK_IMAGE_REQUEST);
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

                Bitmap btmp = Bitmap.createScaledBitmap(bitmap, 1250, 1250, false);
                giden_mesaj = encodeImage(btmp);

                mail_resim.setImageBitmap(bitmap);

                mail_resim.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
