package com.keralafarmers.agrinextai.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Weather data model for storing weather information
 */
@Entity(tableName = "weather_data")
public class Weather {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String date; // YYYY-MM-DD format
    private double temperature; // in Celsius
    private double minTemperature;
    private double maxTemperature;
    private int humidity; // percentage
    private double rainfall; // in mm
    private double windSpeed; // in km/h
    private String weatherCondition; // sunny, cloudy, rainy, etc.
    private String weatherDescription;
    private String weatherIcon;
    private long timestamp;
    private String location;
    private double latitude;
    private double longitude;
    
    // Constructor
    public Weather() {
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public double getTemperature() {
        return temperature;
    }
    
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
    
    public double getMinTemperature() {
        return minTemperature;
    }
    
    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }
    
    public double getMaxTemperature() {
        return maxTemperature;
    }
    
    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }
    
    public int getHumidity() {
        return humidity;
    }
    
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
    
    public double getRainfall() {
        return rainfall;
    }
    
    public void setRainfall(double rainfall) {
        this.rainfall = rainfall;
    }
    
    public double getWindSpeed() {
        return windSpeed;
    }
    
    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }
    
    public String getWeatherCondition() {
        return weatherCondition;
    }
    
    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }
    
    public String getWeatherDescription() {
        return weatherDescription;
    }
    
    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }
    
    public String getWeatherIcon() {
        return weatherIcon;
    }
    
    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    @Override
    public String toString() {
        return "Weather{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", rainfall=" + rainfall +
                ", weatherCondition='" + weatherCondition + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}