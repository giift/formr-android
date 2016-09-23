package com.giift.formr.fee;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nicolas on 2016/03/7.
 */
public class Stepped extends Calc {

  private class Bucket {
    Double min;
    Double max;
    double value;
  }

  private List<Bucket> buckets_ = new ArrayList<>();

  @Override
  public double GetValue(double v) {
    for (Bucket b : this.buckets_) {
      boolean in = true;
      if (b.min != null && b.min > v) {
        in = false;
      }
      if (in && b.max != null && b.max < v) {
        in = false;
      }
      if (in) {
        return b.value;
      }
    }
    return 0.0;
  }

  @Override
  public boolean Init(@NonNull Object obj) {
    if (obj instanceof JSONArray) {
      JSONArray aObj = (JSONArray) obj;
      for (int i = 0; i < aObj.length(); i++) {
        JSONObject bConf = aObj.optJSONObject(i);
        if (bConf != null) {
          Bucket b = new Bucket();
          try {
            b.value = bConf.getDouble("value");
          } catch (JSONException e) {
            return false;
          }
          if (bConf.has("min")) {
            b.min = bConf.optDouble("min");
          }
          if (bConf.has("max")) {
            b.max = bConf.optDouble("max");
          }
          buckets_.add(b);
        }
      }
      return true;
    }
    return false;
  }
}
