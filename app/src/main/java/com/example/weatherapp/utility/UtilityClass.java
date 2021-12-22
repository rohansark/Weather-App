package com.example.weatherapp.utility;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class UtilityClass {

  public static String volleyerror(@NonNull final VolleyError error) {
    String message = "";
    if (error instanceof NetworkError) {
      message = "Cannot connect to Internet. Please check your connection!";
    } else if (error instanceof ServerError) {
      message = "The server could not be found. Please try again after some time!!";
    } else if (error instanceof AuthFailureError) {
      message = "Cannot connect to Internet...Please check your connection!";
    } else if (error instanceof ParseError) {
      message = "Parsing error! Please try again after some time!!";
    } else if (error instanceof TimeoutError) {
      message = "Connection TimeOut! Please check your internet connection.";
    }
    return message;
  }
}
