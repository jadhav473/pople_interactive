package com.example.myapp.Volley;

public interface VolleyCallbacks {
    public boolean successCallback(String response);

    public void failCallback(String response, boolean isNoInternet);
}
