package com.sengelgrup.anket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User extends AppCompatActivity {

    ArrayList<String> dizi_tarih = new ArrayList<>();
    ArrayList<String> dizi_isim = new ArrayList<>();
    ArrayList<String> dizi_telefon = new ArrayList<>();
    ArrayList<String> dizi_1 = new ArrayList<>();
    ArrayList<String> dizi_2 = new ArrayList<>();
    ArrayList<String> dizi_3 = new ArrayList<>();
    ArrayList<String> dizi_4 = new ArrayList<>();

    ListView admin_liste;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        admin_liste = findViewById(R.id.user_liste);

        ListeDoldur();
    }

    void ListeDoldur()
    {
        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonresponse = new JSONObject(response);

                    JSONArray tarih = jsonresponse.getJSONArray("tarih");
                    JSONArray isim = jsonresponse.getJSONArray("isim");
                    JSONArray telefon = jsonresponse.getJSONArray("telefon");
                    JSONArray bir = jsonresponse.getJSONArray("bir");
                    JSONArray iki = jsonresponse.getJSONArray("iki");
                    JSONArray uc = jsonresponse.getJSONArray("uc");
                    JSONArray dort = jsonresponse.getJSONArray("dort");

                    for (int i = 0; i < tarih.length(); i++)
                    {
                        dizi_tarih.add(i,tarih.get(i).toString());
                        if (isim.get(i).toString().equals(""))
                        {
                            dizi_isim.add(i,"İsim belirtilmemiş.");
                        }
                        else
                        {
                            dizi_isim.add(i,isim.get(i).toString());
                        }
                        if (telefon.get(i).toString().equals(""))
                        {
                            dizi_telefon.add(i,"Numara belirtilmemiş.");
                        }
                        else
                        {
                            dizi_telefon.add(i,telefon.get(i).toString());
                        }
                        dizi_1.add(i,bir.get(i).toString());
                        dizi_2.add(i,iki.get(i).toString());
                        dizi_3.add(i,uc.get(i).toString());
                        dizi_4.add(i,dort.get(i).toString());
                    }

                    admin_liste.setAdapter(new AdminAdapter(User.this,
                            dizi_tarih,
                            dizi_isim,
                            dizi_telefon,
                            dizi_1,dizi_2,dizi_3,dizi_4));

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };

        AdminJSON loginrequest = new AdminJSON(responselistener);
        RequestQueue queue = Volley.newRequestQueue(User.this);
        queue.add(loginrequest);
    }
}
