package com.example.weatherapp.models;

public class WeatherModel {
  private String location;
  private String localTime;
  private String temp;
  private String windspeed;
  private String humidity;
  private String aqi_state;
  private String prec;
  private String feelsLike;

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  private String image;

  public String getPrec() {
    return prec;
  }

  public void setPrec(String prec) {
    this.prec = prec;
  }

  public String getFeelsLike() {
    return feelsLike;
  }

  public void setFeelsLike(String feelsLike) {
    this.feelsLike = feelsLike;
  }

  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  private String condition;

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getLocalTime() {
    return localTime;
  }

  public void setLocalTime(String localTime) {
    this.localTime = localTime;
  }

  public String getTemp() {
    return temp;
  }

  public void setTemp(String temp) {
    this.temp = temp;
  }

  public String getWindspeed() {
    return windspeed;
  }

  public void setWindspeed(String windspeed) {
    this.windspeed = windspeed;
  }

  public String getHumidity() {
    return humidity;
  }

  public void setHumidity(String humidity) {
    this.humidity = humidity;
  }

  public String getAqi_state() {
    return aqi_state;
  }

  public void setAqi_state(String aqi_state) {
    this.aqi_state = aqi_state;
  }
}
