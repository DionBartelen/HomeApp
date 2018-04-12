package edu.avans.dionb.homeapp.Entity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by dionb on 10-4-2018.
 */

public class ConnectionDion {
    Context context;
    RequestQueue queue;
    String ip;
    int port = 8000;
    CheckWeahterListener listener = null;

    public ConnectionDion(Context context, String ip) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
        this.ip = ip;
    }

    public RequestQueue getRequestQue() {
        return queue;
    }

    public void TurnOfOnn() {
        String url = "http://" + ip + ":" + port + "/onOff";
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url, OnSucces(), ErrorListener());
        getRequestQue().add(request);
    }

    public void TurnOn() {
        String url = "http://" + ip + ":" + port + "/on";
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url, OnSucces(), ErrorListener());
        getRequestQue().add(request);
    }

    public void TurnOff() {
        String url = "http://" + ip + ":" + port + "/off";
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url, OnSucces(), ErrorListener());
        getRequestQue().add(request);
    }

    public void NextMode() {
        String url = "http://" + ip + ":" + port + "/nextModus";
        StringRequest request = new StringRequest(Request.Method.GET, url, OnSucces(), ErrorListener());
        getRequestQue().add(request);
    }

    public void SetMode(final int mode) {
        String url = "http://" + ip + ":" + port + "/mode";
        StringRequest request = new StringRequest(Request.Method.POST, url, OnSucces(), ErrorListener()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> toReturn = new HashMap<>();
                toReturn.put("Content-Type", "application/json");
                return toReturn;
            };

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject object = new JSONObject();
                try {
                    object.put("id", "mode");
                    object.put("mode", mode);
                }catch (Exception e) {
                }
                return object.toString().getBytes();
            }
        };
        getRequestQue().add(request);
    }

    public void SetColor(final int rood, final int groen, final int blauw) {
        String url = "http://" + ip + ":" + port + "/postColor";
        StringRequest request = new StringRequest(Request.Method.POST, url, OnSucces(), ErrorListener()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> toReturn = new HashMap<>();
                toReturn.put("Content-Type", "application/json");
                return toReturn;
            };

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject object = new JSONObject();
                try {
                    object.put("id", "color");
                    object.put("rood", rood);
                    object.put("groen", groen);
                    object.put("blauw", blauw);
                }catch (Exception e) {
                }
                return object.toString().getBytes();
            }
        };
        getRequestQue().add(request);
    }

    public void checkWeather(CheckWeahterListener lst) {
        listener = lst;
        String url = "http://" + ip + ":" + port + "/currentDHT";
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url, OnWeatherSucces(), ErrorListener());
        getRequestQue().add(request);
    }

    public Response.Listener<String> OnWeatherSucces() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String measurement) {
                if(listener != null) {
                    listener.OnWeatherReceived(WeatherMeasurement.fromJson(measurement));
                }
                listener = null;
            }
        };
    }

    public Response.Listener<String> OnSucces() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
            }
        };
    }

    public Response.ErrorListener ErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getLocalizedMessage());
                Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }
}
