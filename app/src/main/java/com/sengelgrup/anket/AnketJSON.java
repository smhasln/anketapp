package com.sengelgrup.anket;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class AnketJSON extends StringRequest {
    private static final String GIRIS_URL = "http://213.142.145.51/plesk-site-preview/sengelgrup.com/android/anket.php";

    private Map<String, String> params;

    public AnketJSON(Float bir, Float iki, Float uc, Float dort,String adsoyad,String telefon, Response.Listener<String> listener) {
        super(Request.Method.POST, GIRIS_URL, listener, null);
        // İşlemler için kullanılacak veriler hazırlanır.
        params = new HashMap<>();
        params.put("bir", String.valueOf(bir));
        params.put("iki", String.valueOf(iki));
        params.put("uc", String.valueOf(uc));
        params.put("dort", String.valueOf(dort));
        params.put("adsoyad", adsoyad);
        params.put("telefon", telefon);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
