package edu.avans.dionb.homeapp.Entity;

import android.support.annotation.NonNull;

/**
 * Created by dionb on 10-4-2018.
 */

public class Device implements Comparable<Device> {
    private String ip;
    private String name;
    private DeviceType type;

    public Device(String ip) {
        this.ip = ip;
        this.name = "UNKNOWN";
        this.type = DeviceType.UNKNOWN;
    }

    public Device(String ip, String name) {
        this.ip = ip;
        this.name = name;
        this.type = DeviceType.UNKNOWN;
    }

    public Device(String ip, String name, DeviceType type) {
        this.ip = ip;
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Device " + ip;
    }

    @Override
    public int compareTo(@NonNull Device o) {
        return this.ip.compareTo(o.ip);
    }
}
