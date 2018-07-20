package com.sengelgrup.anket;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class PieJSON extends StringRequest {
    private static final String GIRIS_URL = "http://213.142.145.51/plesk-site-preview/sengelgrup.com/android/anket_pie.php";

    private Map<String, String> params;

    public PieJSON(String deger,Response.Listener<String> listener) {
        super(Method.POST, GIRIS_URL, listener, null);
        // İşlemler için kullanılacak veriler hazırlanır.
        params = new HashMap<>();
        params.put("deger",deger);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
