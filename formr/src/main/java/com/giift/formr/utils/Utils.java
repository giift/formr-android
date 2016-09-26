package com.giift.formr.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.TypedValue;

import com.giift.formr.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author vieony on 4/21/2016.
 */
public class Utils {

  /**
   * This function returns the accent color set for the Theme in use
   * if the accent color is not set; it returns the default accent for the device
   * @param context Context
   * @return accentColor
   */
  public static @ColorInt
  int GetAccentColor(@NonNull Context context) {
    TypedValue typedValue = new TypedValue();
    TypedArray array = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorAccent});
    int color = array.getColor(0, 0);
    array.recycle();
    return color;
  }

  /**
   * This function returns the colorControlNormal set for the Theme in use
   * if the colorControlNormal is not set; it returns the default colorControlNormal for the device
   * @param context Context
   * @return colorControlNormal
   */
  public static @ColorInt
  int GetColorControlNormal(@NonNull Context context) {
    TypedValue typedValue = new TypedValue();
    TypedArray array = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorControlNormal});
    int color = array.getColor(0, 0);
    array.recycle();
    return color;
  }

  /**
   * Check if a string has a valid e-mail format
   *
   * @param email string to check
   * @return true if it is a valid email
   * TODO make it more robust
   */
  public static boolean IsEmailValid(String email) {
    boolean isValid = false;
    if (!TextUtils.isEmpty(email)) {
      String expression = "^[\\w\\+\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

      Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(email);
      if (matcher.matches()) {
        isValid = true;
      }
      if (isValid && email.charAt(0) == '.') {
        isValid = false;
      }
      if (isValid && email.contains("..")) {
        isValid = false;
      }
      if (isValid && email.contains(".@")) {
        isValid = false;
      }
      if (isValid && email.contains("@-")) {
        isValid = false;
      }
      if (isValid && email.endsWith(".web")) {
        isValid = false;
      }
    }
    return isValid;
  }

}
