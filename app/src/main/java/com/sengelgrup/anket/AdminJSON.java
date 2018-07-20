package com.sengelgrup.anket;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class AdminJSON extends StringRequest {
    private static final String GIRIS_URL = "http://213.142.145.51/plesk-site-preview/sengelgrup.com/android/anket_admin.php";
    public AdminJSON(Response.Listener<String> listener) {
        super(Method.GET, GIRIS_URL, listener, null);
    }
}
