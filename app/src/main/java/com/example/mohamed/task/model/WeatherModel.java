package com.example.mohamed.task.model;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WeatherModel implements Serializable{


    private String cityName;
    private String cityTemp;
    private String cityImg;
    private String cityDesc;

    private Drawable cityDrawable;


    public WeatherModel(String cityName, String cityTemp, String cityImg, String cityDesc) {
        this.cityName = cityName;
        this.cityTemp = cityTemp;
        this.cityImg = cityImg;
        this.cityDesc = cityDesc;
    }

    public Drawable getCityDrawable() {
        return cityDrawable;
    }

    public void setCityDrawable(Drawable cityDrawable) {
        this.cityDrawable = cityDrawable;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityTemp() {
        return cityTemp;
    }

    public String getCityImg() {
        return cityImg;
    }

    public String getCityDesc() {
        return cityDesc;
    }
}