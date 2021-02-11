package com.example.myapp.Volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;



public class VolleySender {

    private String URL;
    private int REQUEST_TYPE = Request.Method.GET;
    private static RequestQueue queue;
    private boolean callback = true;
    private VolleyCallbacks volleyCallbacks;
    private HashMap<String, String> nameValuePair = new HashMap<>();

    /**
     * Use this constructor when there is ?action= in url of request,.. mostly in chatroom related url
     *
     * @param c         context
     * @param URL       url
     * @param callbacks callback
     */
    public VolleySender(Context c, String URL, VolleyCallbacks callbacks) {
        this.volleyCallbacks = callbacks;
        queue = getQueueInstance(c);
        this.URL = URL;
    }

    /**
     * Use this constructor when there is ?action= in url of request,.. mostly in chatroom related url
     *
     * @param c   context
     * @param URL url
     */
    public VolleySender(Context c, String URL) {
        queue = getQueueInstance(c);
        this.URL = URL;
    }

    public static synchronized RequestQueue getQueueInstance(Context c) {
        if (null == queue) {
            queue = Volley.newRequestQueue(c);
        }
        return queue;
    }

    /**
     * Add a new name value pair to the current request. The paramters are
     * cleared after the request in completed.
     */
    public void addNameValuePair(String key, String value) {
        if (null == value) {
            nameValuePair.put(key, "");
        } else {
            nameValuePair.put(key, value);
        }
    }

    /**
     * Add a new name value pair to the current request. The paramters are
     * cleared after the request in completed.
     */
    public void addNameValuePair(String key, Integer value) {
        if (null == value) {
            nameValuePair.put(key, "");
        } else {
            nameValuePair.put(key, String.valueOf(value));
        }
    }

    /**
     * Add a new name value pair to the current request. The paramters are
     * cleared after the request in completed.
     */
    public void addNameValuePair(String key, Long value) {
        if (null == value) {
            nameValuePair.put(key, "");
        } else {
            nameValuePair.put(key, String.valueOf(value));
        }
    }

    /**
     * Perform the AJAX request.
     */
    public void sendAjax() {
        try {
            if (MyLocalProvider.isConnected()) {
                if (URL.startsWith("https")) {
                    MyLocalProvider.allowHttpsConnection();
                }

                final String finalURL;
                if (URL.contains("?")) {
                    finalURL = URL + "&t=" + System.currentTimeMillis();
                } else {
                    finalURL = URL + "?t=" + System.currentTimeMillis();
                }

                StringRequest request = new StringRequest(REQUEST_TYPE, finalURL, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.e("Sendajx resp success ",response.toString());
                        nameValuePair.clear();
                        if (volleyCallbacks != null) {
                            volleyCallbacks.successCallback(response);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError response) {
                        Log.e("Sendajx resp error ",response.toString());
                        nameValuePair.clear();
                        if (volleyCallbacks != null) {
                            volleyCallbacks.failCallback(response.toString(), false);
                        }
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        return nameValuePair;
                    }


                };
                Log.e("URL= " ,finalURL+" params =" + nameValuePair);
                request.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                request.setShouldCache(false);
                queue.add(request);
            } else {
                nameValuePair.clear();
                if (volleyCallbacks != null) {
                    volleyCallbacks.failCallback("", true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Pass true to set the "callback" parameter. Callback is sent by default.
     */
    public void sendCallBack(Boolean isRequired) {
        callback = isRequired;
    }

    public void setRequestType(int requestType) {
        this.REQUEST_TYPE = requestType;
    }

}
