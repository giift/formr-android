package com.giift.formr.field;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import java.util.*;

/**
 * Created by vieony on 9/30/2016.
 */
public class Utils {

  private final static String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private final static Random RANDOM = new Random();
  private final static int N = ALPHABET.length();

  public static char GetRandomChar() {
    return ALPHABET.charAt(RANDOM.nextInt(N));
  }

  /**
   * Gets a unique string, that can be used to define a transaction id or something similar when
   * communicating with some other services
   *
   * @return unique String
   */
  public static String GetUniqueStringId() {
    java.util.Date d = new java.util.Date();
    long res = d.getTime();
    String str = Long.toString(res);
    for (int i = 0; i < 6; i++) {
      str += GetRandomChar();
    }
    return str;
  }

  /**
   * @param length length of the string
   * @return string
   */
  public static String GetRandomNumericString(int length) {
    String res = "";
    Random rand = new Random();
    for (int i = 0; i < length; i++) {
      res += Integer.toString(rand.nextInt(10));
    }
    return res;
  }

  public static void rotateScreen(ActivityTestRule activityTestRule) {
    Context context = InstrumentationRegistry.getTargetContext();
    int orientation
        = context.getResources().getConfiguration().orientation;
    Activity activity = activityTestRule.getActivity();
    activity.setRequestedOrientation(
        (orientation == Configuration.ORIENTATION_PORTRAIT) ?
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
