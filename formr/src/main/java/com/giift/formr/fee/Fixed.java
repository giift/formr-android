package com.giift.formr.fee;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author nicolas on 2016/03/7.
 */
public class Fixed extends Calc {
  double value_ = 0.0;

  @Override
  public double GetValue(double v) {
    return value_;
  }

  @Override
  public boolean Init(@NonNull Object obj) {
    if (obj instanceof JSONObject) {
      JSONObject jObj = (JSONObject) obj;
      try {
        this.value_ = jObj.getDouble("value");
      } catch (JSONException ex) {
        return false;
      }
      return true;
    }
    return false;
  }
}
