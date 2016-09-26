package com.giift.formr.utils;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vieony on 9/22/2016.
 */
public class Culture {

  /**
   * Format a date following the local of the phone
   *
   * @param d date to be formatted
   * @return formatted date
   */
  @NonNull
  public static String GetPrettyDate(@NonNull Date d) {
    DateFormat df = DateFormat.getDateInstance();
    return df.format(d);
  }


  /**
   * Format a number following the local of the phone
   *
   * @param v number to be formatted
   * @return formatted number
   */
  @NonNull
  public static CharSequence FormatNumber(double v) {
    NumberFormat nf = NumberFormat.getInstance();
    return nf.format(v);
  }

  /**
   *    * Format a number following the local of the phone
   *    *
   *    * @param v number to be formatted
   *    * @return formatted number
   */
  @NonNull
  public static CharSequence FormatNumber(double v, String unit) {
    java.util.Currency ccy = null;
    if (unit == null) {
      unit = "";
    }

    try {
      ccy = java.util.Currency.getInstance(unit.toUpperCase(Locale.getDefault()));
    } catch (IllegalArgumentException ignored) {
    }

    CharSequence res;
    if (ccy == null) {
      res = FormatNumber(v);
    } else {
      NumberFormat nf = NumberFormat.getInstance();
      nf.setMaximumFractionDigits(ccy.getDefaultFractionDigits());
      nf.setMinimumFractionDigits(ccy.getDefaultFractionDigits());
      res = nf.format(v);
    }
    return res;
  }

  /**
   *    * If unit is the iso code of an official currency, then we use the standard api to display it
   *    * properly. Otherwise, we just format the number nicely and display the unit afterward
   *    *
   *    * @param v    amount to be formatted
   *    * @param unit can be an official currency or an unofficial one, such as miles, points...
   *    * @return formatted string representing the amount and its currency in the phone locale
   */

  public static CharSequence FormatNumberWithUnit(double v, String unit) {
    java.util.Currency ccy = null;
    if (unit == null) {
      unit = "";
    }

    try {
      ccy = java.util.Currency.getInstance(unit.toUpperCase(Locale.getDefault()));
    } catch (IllegalArgumentException ignored) {
    }

    CharSequence res;
    if (ccy == null) {
      res = String.format("%s %s", FormatNumber(v), unit);
    } else {
      NumberFormat nf = NumberFormat.getCurrencyInstance();
      nf.setCurrency(ccy);
      nf.setMaximumFractionDigits(ccy.getDefaultFractionDigits());
      nf.setMinimumFractionDigits(ccy.getDefaultFractionDigits());

      res = nf.format(v);
    }
    return res;
  }
}
