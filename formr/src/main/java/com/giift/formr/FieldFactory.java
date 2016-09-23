package com.giift.formr;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.giift.formr.field.ButtonChoice;
import com.giift.formr.field.CardNumber;
import com.giift.formr.field.Checkbox;
import com.giift.formr.field.Cvv;
import com.giift.formr.field.Date;
import com.giift.formr.field.Day;
import com.giift.formr.field.Dropdown;
import com.giift.formr.field.Email;
import com.giift.formr.field.Hidden;
import com.giift.formr.field.Month;
import com.giift.formr.field.Onoff;
import com.giift.formr.field.Password;
import com.giift.formr.field.PersonName;
import com.giift.formr.field.Phone;
import com.giift.formr.field.PostalAddress;
import com.giift.formr.field.Separator;
import com.giift.formr.field.Slider;
import com.giift.formr.field.Text;
import com.giift.formr.field.TextArea;
import com.giift.formr.field.Url;
import com.giift.formr.field.Year;
import com.giift.formr.field.Zip;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

/**
 */
public class FieldFactory {
  private final static String LOG_TAG = FieldFactory.class.getName();

  @SuppressWarnings("TryWithIdenticalCatches")
  @Nullable
  public static View GetField(JSONObject config, Context context) {
    Class c = GetClass(config);
    View res = null;
    try {
      res = (View) c.getConstructor(Context.class).newInstance(context);
    } catch (InstantiationException e) {
      Log.e(LOG_TAG, "Problem while instanciating the view", e);
    } catch (IllegalAccessException e) {
      Log.e(LOG_TAG, "Problem while instanciating the view", e);
    } catch (NoSuchMethodException e) {
      Log.e(LOG_TAG, "Problem while instanciating the view", e);
    } catch (InvocationTargetException e) {
      Log.e(LOG_TAG, "Problem while instanciating the view", e);
    }
    if (res != null && res instanceof IField) {
      ((IField) res).Init(config);
    }
    return res;
  }

  @SuppressWarnings({"TryWithIdenticalCatches", "unused"})
  public static View GetField(JSONObject config, Context context, AttributeSet attrs) {
    Class c = GetClass(config);
    View res = null;
    try {
      res = (View) c.getConstructor(Context.class, AttributeSet.class).newInstance(context, attrs);
    } catch (InstantiationException e) {
      Log.e(LOG_TAG, "Problem while instanciating the view", e);
    } catch (IllegalAccessException e) {
      Log.e(LOG_TAG, "Problem while instanciating the view", e);
    } catch (NoSuchMethodException e) {
      Log.e(LOG_TAG, "Problem while instanciating the view", e);
    } catch (InvocationTargetException e) {
      Log.e(LOG_TAG, "Problem while instanciating the view", e);
    }
    if (res != null && res instanceof IField) {
      ((IField) res).Init(config);
    }
    return res;
  }

  @SuppressWarnings({"TryWithIdenticalCatches", "unused"})
  public static View GetField(JSONObject config, Context context, AttributeSet attrs, int defStyleAttr) {
    Class c = GetClass(config);
    View res = null;
    try {
      res = (View) c.getConstructor(Context.class, AttributeSet.class, Integer.TYPE).newInstance(context, attrs, defStyleAttr);
    } catch (InstantiationException e) {
      Log.e(LOG_TAG, "Problem while instanciating the view", e);
    } catch (IllegalAccessException e) {
      Log.e(LOG_TAG, "Problem while instanciating the view", e);
    } catch (NoSuchMethodException e) {
      Log.e(LOG_TAG, "Problem while instanciating the view", e);
    } catch (InvocationTargetException e) {
      Log.e(LOG_TAG, "Problem while instanciating the view", e);
    }
    if (res != null && res instanceof IField) {
      ((IField) res).Init(config);
    }
    return res;
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public static View GetField(JSONObject config, Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    Class c = GetClass(config);
    View res = null;
    try {
      res = (View) c.getConstructor(Context.class, AttributeSet.class, Integer.TYPE, Integer.TYPE).newInstance(context, attrs, defStyleAttr, defStyleRes);
    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      e.printStackTrace();
    }
    if (res != null && res instanceof IField) {
      ((IField) res).Init(config);
    }
    return res;
  }


  private static Class GetClass(JSONObject config) {
    String type = config.optString("type", "text");
    Class c;
    switch (type) {
      case "buttonchoice":
        c = ButtonChoice.class;
        break;
      case "cardnumber":
        c = CardNumber.class;
        break;
      case "ccv":
        c = Cvv.class;
        break;
      case "checkbox":
        c = Checkbox.class;
        break;
      case "date":
        c = Date.class;
        break;
      case "day":
        c = Day.class;
        break;
      case "dropdown":
        c = Dropdown.class;
        break;
      case "email":
        c = Email.class;
        break;
      case "month":
        c = Month.class;
        break;
      case "number":
        c = Number.class;
        break;
      case "onoff":
        c = Onoff.class;
        break;
      case "password":
        c = Password.class;
        break;
      case "personname":
        c = PersonName.class;
        break;
      case "phone":
        c = Phone.class;
        break;
      case "postaladdress":
        c = PostalAddress.class;
        break;
      case "radio":
        c = Dropdown.class;
        break;
      case "separator":
        c = Separator.class;
        break;
      case "text":
        c = Text.class;
        break;
      //TODO
//      case "upload":
//        c = com.giift.sdk.ui.form.field.Upload.class;
//        break;
      case "url":
        c = Url.class;
        break;
      case "year":
        c = Year.class;
        break;
      case "zip":
        c = Zip.class;
        break;
      case "hidden":
        c = Hidden.class;
        break;
      case "slider":
        c = Slider.class;
        break;
      case "textarea":
        c = TextArea.class;
        break;
      default:
        c = Text.class;
    }
    return c;
  }
}
