package com.sengelgrup.anket;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class AnaJSON extends StringRequest {
    private static final String GIRIS_URL = "http://213.142.145.51/plesk-site-preview/sengelgrup.com/android/anket_ana.php";

    private Map<String, String> params;

    public AnaJSON(Response.Listener<String> listener) {
        super(Method.POST, GIRIS_URL, listener, null);
        // İşlemler için kullanılacak veriler hazırlanır.
        params = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
