package com.giift.formr.fee;

import android.support.annotation.NonNull;

import org.json.JSONObject;

/**
 * @author nicolas on 2016/03/7.
 */
public abstract class Calc {
  public abstract double GetValue(double v);

  public abstract boolean Init(@NonNull Object obj);

  public static Calc GetCalc(@NonNull JSONObject obj) {
    String type = obj.optString("type");
    Calc c = null;
    switch (type) {
      case "fixed":
        c = new Fixed();
        break;
      case "stepped":
        c = new Stepped();
        break;
    }
    if (c != null) {
      Object params = obj.opt("params");
      if (params != null) {
        c.Init(params);
      }
    }
    return c;
  }
}
