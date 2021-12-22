package com.example.weatherapp.dataservice;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.weatherapp.application.VolleyRequest;
import com.example.weatherapp.models.WeatherModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {

  private final Context context;

  public WeatherDataService(@NonNull final Context context) {
    this.context = context;
  }

  public void getWeatherData(@NonNull final String urlString, VolleyResponseListener<WeatherModel> volleyResponseListener) {
    final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, urlString, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(@NonNull final JSONObject response) {
        Log.i(WeatherDataService.class.getSimpleName(), response.toString());
        List<WeatherModel> weatherModelList = new ArrayList<>();
        try {
          WeatherModel weatherModel = new WeatherModel();
          final JSONObject location = response.getJSONObject("location");
          final String mainLocation = location.getString("name") + ", " + location.getString("country");
          weatherModel.setLocation(mainLocation);
          final String localTime = location.getString("localtime");
          weatherModel.setLocalTime(localTime);
          final JSONObject currentTemp = response.getJSONObject("current");
          final String temperature = currentTemp.getString("temp_c");
          weatherModel.setTemp(temperature.substring(0,2));
          final String windSpeed = currentTemp.getString("wind_kph");
          weatherModel.setWindspeed(windSpeed+" k/h");
          final String humidity = currentTemp.getString("humidity");
          weatherModel.setHumidity(humidity);
          final String feelsLike = currentTemp.getString("feelslike_c");
          weatherModel.setFeelsLike(feelsLike);
          final String precip = currentTemp.getString("precip_mm");
          weatherModel.setPrec(precip+" %");
          final JSONObject conditionObj = currentTemp.getJSONObject("condition");
          final String condition = conditionObj.getString("text");
          weatherModel.setCondition(condition);
          final String icon = conditionObj.getString("icon");
          weatherModel.setImage("https:"+icon);
          final JSONObject airQuality = currentTemp.getJSONObject("air_quality");
          final String pm2_5 = airQuality.getString("pm2_5");
          final String aqi_state = fetchAirQualityState(pm2_5);
          weatherModel.setAqi_state(aqi_state);
          weatherModelList.add(weatherModel);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        volleyResponseListener.onResponse(weatherModelList);
        Log.i(WeatherDataService.class.getSimpleName(), "List of weather :" + response.toString());
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(@NonNull final VolleyError error) {
        volleyResponseListener.onError(error);
      }
    });
    //Singleton class is for storing all the request queue
    VolleyRequest.getInstance(context).addToRequestQueue(jsonRequest);
  }

  protected String fetchAirQualityState(String pm2_5) {
    double pm2 = Double.parseDouble(pm2_5);
    if (pm2 < 31)
      return "Good";
    else if (pm2 > 30 && pm2 < 60)
      return "Better";
    else if (pm2 > 60 && pm2 < 90)
      return "Mod.";
    else if (pm2 > 90 && pm2 < 120)
      return "Poor";
    else if (pm2 > 120 && pm2 < 250)
      return "V.Poor";
    else if (pm2 > 250 && pm2 < 380)
      return "Severe";
    else
      return "NA";
  }
}
