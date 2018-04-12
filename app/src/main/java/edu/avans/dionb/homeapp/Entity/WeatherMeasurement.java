package edu.avans.dionb.homeapp.Entity;

import org.json.JSONObject;

/**
 * Created by dionb on 12-4-2018.
 */

public class WeatherMeasurement {
    private double temperature;
    private double humidity;

    public WeatherMeasurement(double temperature, double humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public static WeatherMeasurement fromJson(String measurement) {
        try {
            JSONObject jo = new JSONObject(measurement);
            int temp = jo.getInt("temperature");
            int humidity = jo.getInt("humidity");
            return new WeatherMeasurement(temp, humidity);
        } catch (Exception e) {
            return new WeatherMeasurement(0, 0);
        }
    }
}
