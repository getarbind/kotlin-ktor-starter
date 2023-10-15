package io.collective.airquality;

public class AirQualityInfo {
    private int id;
    private String city;
    private String date;
    private String pm10_avg;
    private String pm10_max;
    private String pm10_min;

    public AirQualityInfo(int id, String city, String date, String pm10_avg, String pm10_max, String pm10_min) {
        this.id = id;
        this.city = city;
        this.date = date;
        this.pm10_avg = pm10_avg;
        this.pm10_max = pm10_max;
        this.pm10_min = pm10_min;
    }

    public AirQualityInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPm10_avg() {
        return pm10_avg;
    }

    public void setPm10_avg(String pm10_avg) {
        this.pm10_avg = pm10_avg;
    }

    public String getPm10_max() {
        return pm10_max;
    }

    public void setPm10_max(String pm10_max) {
        this.pm10_max = pm10_max;
    }

    public String getPm10_min() {
        return pm10_min;
    }

    public void setPm10_min(String pm10_min) {
        this.pm10_min = pm10_min;
    }
}