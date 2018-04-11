package edu.avans.dionb.homeapp.Entity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.zip.DeflaterInputStream;

/**
 * Created by dionb on 10-4-2018.
 */

public class SharedPrefHandler {

    public static void AddDevice(Device device, Context context) {
        ArrayList<Device> allDevices = GetDevices(context);
        allDevices.add(device);
        String newAllDevicesString = "[";
        for(int x = 0; x < allDevices.size(); x++) {
            newAllDevicesString += allDevices.get(x).toJson();
            if(x < (allDevices.size() -1)) {
                newAllDevicesString += ", ";
            }
        }
        newAllDevicesString += "]";
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("AllDevices", newAllDevicesString).apply();
    }

    public static void DeleteDevice(Device device, Context context) {
        ArrayList<Device> allDevices = GetDevices(context);
        allDevices.remove(device);
        String newAllDevicesString = "[";
        for(int x = 0; x < allDevices.size(); x++) {
            newAllDevicesString += allDevices.get(x).toJson();
            if(x < (allDevices.size() -1)) {
                newAllDevicesString += ", ";
            }
        }
        newAllDevicesString += "]";
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("AllDevices", newAllDevicesString).apply();
    }

    public static ArrayList<Device> GetDevices(Context context) {
        ArrayList<Device> toReturn = new ArrayList<>();
        String allDevices = PreferenceManager.getDefaultSharedPreferences(context).getString("AllDevices", "[]");
        try {
            JSONArray allDevicesJA = new JSONArray(allDevices);
            if(allDevicesJA.length()!= 0) {
                for(int x = 0; x < allDevicesJA.length(); x++) {
                    JSONObject deviceObj = allDevicesJA.getJSONObject(x);
                    toReturn.add(new Device(deviceObj.getString("ip"), deviceObj.getString("name"), DeviceType.valueOf(deviceObj.getString("type"))));
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }
}
