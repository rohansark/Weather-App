package com.example.weatherapp.application;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Application class is to handle all the request that comes from API and stores it into a Queue.
 *
 * @author Souvik Sarkar
 * @since 2021-10-07
 */
public final class VolleyRequest {
  private static VolleyRequest instance;
  private RequestQueue requestQueue;
  private final Context ctx;

  /**
   * parameterized constructor to make singleton class.
   *
   * @param context context on which it is running
   */
  private VolleyRequest(@NonNull final Context context) {
    ctx = context;
    requestQueue = getRequestQueue();
  }

  /**
   * This method is used to create only one object of this particular class.
   *
   * @param context context on which it is running
   * @return object or instance
   */
  public static synchronized VolleyRequest getInstance(@NonNull final Context context) {
    if (instance == null) {
      instance = new VolleyRequest(context);
    }
    return instance;
  }

  /**
   * This method is for fetching the request queue.
   *
   * @return Queue
   */
  public RequestQueue getRequestQueue() {
    if (requestQueue == null) {
      requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
    } else {
      Log.i(VolleyRequest.class.getSimpleName(), "Request queue not null");
    }
    return requestQueue;
  }

  /**
   * This method is for add the request to the queue.
   *
   * @param req Request
   */
  public <T> void addToRequestQueue(@NonNull final Request<T> req) {
    getRequestQueue().add(req);
  }
}
