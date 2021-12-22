package com.example.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.weatherapp.dataservice.VolleyResponseListener;
import com.example.weatherapp.dataservice.WeatherDataService;
import com.example.weatherapp.models.WeatherModel;
import com.example.weatherapp.utility.UtilityClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
  private LocationManager locationManager;
  private LocationListener locationListener;
  private SwipeRefreshLayout swipeContainer;
  private WeatherDataService weatherDataService;
  private double latitude;
  private double longitude;
  private String key="";
  private String urlString = "";
  private TextView tv_location;
  private TextView tv_humidity;
  private TextView tv_windspeed;
  private TextView tv_aqi;
  private TextView tv_temp;
  private TextView tv_precip;
  private TextView tv_feelsLike;
  private TextView tv_condition;
  private ImageView iv_image;
  private ConstraintLayout constraintLayout;

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == 1) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
          locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1000, locationListener);
        }
      }
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
    weatherDataService = new WeatherDataService(getApplicationContext());
    tv_location = findViewById(R.id.tv_location);
    tv_aqi = findViewById(R.id.tv_aqi);
    tv_humidity = findViewById(R.id.tv_humidity);
    tv_windspeed = findViewById(R.id.tv_windspeed);
    tv_temp = findViewById(R.id.tv_temp);
    tv_feelsLike = findViewById(R.id.tv_feelsLike);
    tv_condition = findViewById(R.id.tv_condition);
    tv_precip = findViewById(R.id.tv_precip);
    iv_image = findViewById(R.id.iv_condition);
    constraintLayout = findViewById(R.id.backgroundImage);
    // Setup refresh listener which triggers new data loading
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        // Your code to refresh the list here.
        // Make sure you call swipeContainer.setRefreshing(false)
        // once the network request has completed successfully.
        fetchWeatherDataFromApi();
      }
    });
    // Configure the refreshing colors
    swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
          android.R.color.holo_green_light,
          android.R.color.holo_orange_light,
          android.R.color.holo_red_light);
  }

  @Override
  protected void onResume() {
    super.onResume();
    locationListener = new LocationListener() {
      @Override
      public void onLocationChanged(Location location) {
        Log.i("Location", location.toString());
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        fetchWeatherDataFromApi();
      }

      @Override
      public void onStatusChanged(String s, int i, Bundle bundle) {

      }

      @Override
      public void onProviderEnabled(String s) {

      }

      @Override
      public void onProviderDisabled(String s) {

      }
    };

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      //asking permission
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    } else {
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1000, locationListener);
    }
  }

  private void fetchWeatherDataFromApi() {
    key = String.format("%.4f", latitude)+","+String.format("%.4f", longitude);
    urlString = "https://api.weatherapi.com/v1/current.json?key=3256b2444e264329ba2100022211512&q="+key+"&aqi=yes";
    Log.i(MainActivity.class.getSimpleName(),urlString);
    weatherDataService.getWeatherData(urlString, new VolleyResponseListener<WeatherModel>() {
      @Override
      public void onResponse(List<WeatherModel> listModels) {
        Log.i(MainActivity.class.getSimpleName(),listModels.get(0).getLocation());
        tv_location.setText(listModels.get(0).getLocation());
        tv_temp.setText(listModels.get(0).getTemp());
        tv_humidity.setText(listModels.get(0).getHumidity());
        tv_windspeed.setText(listModels.get(0).getWindspeed());
        tv_aqi.setText(listModels.get(0).getAqi_state());
        tv_feelsLike.setText(listModels.get(0).getFeelsLike());
        tv_precip.setText(listModels.get(0).getPrec());
        tv_condition.setText(listModels.get(0).getCondition());
        Glide.with(getApplicationContext()).load(listModels.get(0).getImage()).dontAnimate().into(iv_image);
        try{
          Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH).parse(listModels.get(0).getLocalTime());
          String hour = new SimpleDateFormat("H", Locale.ENGLISH).format(date);
          setBackgroundBasedOnTime(hour);
          Log.i(MainActivity.class.getSimpleName(), "Time is - "+hour);
        }
        catch (Exception e)
        {
          System.out.println(e);
        }
        swipeContainer.setRefreshing(false);
      }

      @Override
      public void onError(@NonNull final VolleyError error) {
        Log.i(MainActivity.class.getSimpleName(), UtilityClass.volleyerror(error));
        Toast.makeText(MainActivity.this, UtilityClass.volleyerror(error), Toast.LENGTH_LONG).show();
      }
    });
  }

  private void setBackgroundBasedOnTime(String hour) {
    int hourtime = Integer.parseInt(hour);
    if(hourtime<=19 && hourtime>=15)
      constraintLayout.setBackgroundResource(R.drawable.evening);
    else if(hourtime<=15 && hourtime>=7)
      constraintLayout.setBackgroundResource(R.drawable.afternoon);
    else if(hourtime<=7 && hourtime>=4)
      constraintLayout.setBackgroundResource(R.drawable.sunset);
    else
      constraintLayout.setBackgroundResource(R.drawable.night);
  }
}