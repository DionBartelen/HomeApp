package edu.avans.dionb.homeapp.Entity;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;

/**
 * Created by dionb on 10-4-2018.
 */

public class ConnectionRamon {
    Context context;
    RequestQueue queue;
    String ip;
    int port = 80;

    public ConnectionRamon(Context context, String ip) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
        this.ip = ip;
    }

    public RequestQueue getRequestQue() {
        return queue;
    }


    public void SetMode(int mode) {
        String url = "http://" + ip + ":" + port + "/command?command=" + mode;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.PUT, url, onSuccesListener, onErrorListener);
        getRequestQue().add(request);
    }

    public void SetCLRMode(int mode) {
        String url = "http://" + ip + ":" + port + "/colormode?colormode=" + mode;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.PUT, url, onSuccesListener, onErrorListener);
        getRequestQue().add(request);
    }

    public void SetHue(int hue) {
        String url = "http://" + ip + ":" + port + "/clr?hue=" + hue;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.PUT, url, onSuccesListener, onErrorListener);
        getRequestQue().add(request);
    }
    public void SetSat(int sat) {
        String url = "http://" + ip + ":" + port + "/sat?sat=" + sat;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.PUT, url, onSuccesListener, onErrorListener);
        getRequestQue().add(request);
    }

    public void SetBri(int bri) {
        String url = "http://" + ip + ":" + port + "/bri?bri=" + bri;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.PUT, url, onSuccesListener, onErrorListener);
        getRequestQue().add(request);
    }


    public void SetColor(int hue, int sat, int bri) {
        String url = "http://" + ip + ":" + port + "/color?hue=" + hue + "&sat=" + sat + "&bri=" + bri;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.PUT, url, onSuccesListener, onErrorListener);
        getRequestQue().add(request);
    }

    public void SetClockAndSync() {
        Calendar cal = Calendar.getInstance();
        String url = "http://" + ip + ":" + port + "/synctime?h=" + cal.get(Calendar.HOUR) + "&m=" + cal.get(Calendar.MINUTE) + "&s=" + cal.get(Calendar.SECOND) + "&ms=" + cal.get(Calendar.MILLISECOND);
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.PUT, url, onSuccesListener, onErrorListener);
        getRequestQue().add(request);
    }

    public void SetOnOff(boolean b) {
        int n = 0;
        if(b) {
            n = 1;
        }
        String url = "http://" + ip + ":" + port + "/OnOff?on=" + n;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.PUT, url, onSuccesListener, onErrorListener);
        getRequestQue().add(request);
    }


    Response.Listener<String> onSuccesListener = (String response) -> {
        String last = response;
        Log.d("Request gelukt ", response);
        //Toast.makeText(context, response, Toast.LENGTH_LONG).show();
    };


    Response.ErrorListener onErrorListener = (VolleyError error) -> {
        Log.d("Request failed", error.toString());
        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
    };
}
