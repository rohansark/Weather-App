package com.example.weatherapp.dataservice;

import com.android.volley.VolleyError;
import java.util.List;

/**
 * This interface is used for callback methods from volley.
 *
 * @author Souvik Sarkar
 * @since 2021-10-07
 */
public interface VolleyResponseListener<T> {

  /**
   * On Response from API this method will be called.
   *
   * @param listModels Models
   */
  void onResponse(List<T> listModels);

  /**
   * On error fetching the data from API this method will be called.
   *
   * @param error generated error from volley library
   */
  void onError(VolleyError error);
}
