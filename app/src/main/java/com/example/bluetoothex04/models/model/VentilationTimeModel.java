package com.example.bluetoothex04.models.model;

public class VentilationTimeModel {
    private String time;
    private String city_name;
    private String station_name;
    private double minute;
    private double pm_10_value;
    private String pm_10_grade;
    private double pm_10_grade_num;
    private double pm_25_value;
    private String pm_25_grade;
    private double pm_25_grade_num;
    private String standard;
    private String message;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public double getMinute() {
        return minute;
    }

    public void setMinute(double minute) {
        this.minute = minute;
    }

    public double getPm_10_value() {
        return pm_10_value;
    }

    public void setPm_10_value(double pm_10_value) {
        this.pm_10_value = pm_10_value;
    }

    public String getPm_10_grade() {
        return pm_10_grade;
    }

    public void setPm_10_grade(String pm_10_grade) {
        this.pm_10_grade = pm_10_grade;
    }

    public double getPm_10_grade_num() {
        return pm_10_grade_num;
    }

    public void setPm_10_grade_num(double pm_10_grade_num) {
        this.pm_10_grade_num = pm_10_grade_num;
    }

    public double getPm_25_value() {
        return pm_25_value;
    }

    public void setPm_25_value(double pm_25_value) {
        this.pm_25_value = pm_25_value;
    }

    public String getPm_25_grade() {
        return pm_25_grade;
    }

    public void setPm_25_grade(String pm_25_grade) {
        this.pm_25_grade = pm_25_grade;
    }

    public double getPm_25_grade_num() {
        return pm_25_grade_num;
    }

    public void setPm_25_grade_num(double pm_25_grade_num) {
        this.pm_25_grade_num = pm_25_grade_num;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "VentilationTime{" +
                "time='" + time + '\'' +
                ", city_name='" + city_name + '\'' +
                ", station_name='" + station_name + '\'' +
                ", minute=" + minute +
                ", pm_10_value=" + pm_10_value +
                ", pm_10_grade=" + pm_10_grade +
                ", pm_10_grade_num=" + pm_10_grade_num +
                ", pm_25_value=" + pm_25_value +
                ", pm_25_grade=" + pm_25_grade +
                ", pm_25_grade_num=" + pm_25_grade_num +
                ", standard='" + standard + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
