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

    public String toJson() {
        return "{\"ip\":\"" + ip + "\", \"name\":\"" + name + "\", \"type\":\"" + type.toString() + "\"}";
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Device " + name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        Device comp = (Device) obj;
        return ip.equals(comp.getIp()) && name.equals(comp.getName()) && type.equals(comp.getType());
    }

    @Override
    public int compareTo(@NonNull Device o) {
        return this.ip.compareTo(o.ip);
    }
}
