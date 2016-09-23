package com.giift.formr.io;

import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by vieony on 9/22/2016.
 */
public class Utils {

  private static final String LOG_TAG = Utils.class.getName();
  /**
   * Method tries to convert OkHttp response to JsonObject
   *
   * @param response OkHttpResponse
   * @return JsonObject representation of the response
   */
  @Nullable
  public static JSONObject GetJsonObject(Response response) {
    JSONObject jsonObject = null;
    try {
      if (response != null) {
        String jsonData = response.body().string();
        jsonObject = new JSONObject(jsonData);
      }
    } catch (JSONException | IOException | OutOfMemoryError e) {
      Log.e(LOG_TAG, "Error converting response to JSONObject ", e);
    }
    return jsonObject;
  }

}
