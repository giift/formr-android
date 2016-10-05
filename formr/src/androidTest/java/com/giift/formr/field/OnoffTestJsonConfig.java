package com.giift.formr.field;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.giift.formr.R;
import com.giift.formr.activity.FieldsTestActivity;

import org.hamcrest.Matcher;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * @author vieony on 10/4/2016.
 */
@RunWith(AndroidJUnit4.class)
public class OnoffTestJsonConfig {

  private static final String LOG_TAG = OnoffTestJsonConfig.class.getName();

  @Rule
  public ActivityTestRule<FieldsTestActivity> activityTestRule_ = new ActivityTestRule<>(
      FieldsTestActivity.class);

  @Before
  public void ScrollToOnoff() {
    onView(withId(R.id.onoff)).perform(closeSoftKeyboard());
    onView(withId(R.id.onoff)).perform(scrollTo());
    onView(withId(R.id.onoff)).check(matches(isDisplayed()));
  }

  @Test
  public void OnoffInitJson01() {
    String id = Utils.GetUniqueStringId();
    onView(withId(R.id.onoff)).perform(
        initView(GetOnoffJson(id, false, false, null, null, null)));
    onView(withId(R.id.onoff)).check(matches(OnoffTest.GetFieldId(id)));
  }

  @Test
  public void OnoffInitJson02() {
    String id = Utils.GetUniqueStringId();
    Random random = new Random();
    boolean value = random.nextBoolean();
    final String hint = Utils.GetUniqueStringId();
    final String error = Utils.GetUniqueStringId();
    JSONObject callback = new JSONObject();
    try {
      callback.put("on_focus_lost", Utils.GetUniqueStringId());
      callback.put("on_value_changed", Utils.GetUniqueStringId());
    }catch (JSONException e){
      Log.e(LOG_TAG, "Json Exception", e);
    }
    onView(withId(R.id.onoff)).perform(
        initView(GetOnoffJson(id, true, value, hint, error, callback)));
    onView(withId(R.id.onoff)).check(matches(OnoffTest.GetValue(value)));
  }

  private ViewAction initView(final JSONObject jsonObject) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Onoff onoff = (Onoff) view;
        onoff.Init(jsonObject);
      }

      @Override
      public String getDescription() {
        return "Initialise Onoff Json";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Onoff.class);
      }
    };
  }

  private JSONObject GetOnoffJson(
      String id,
      boolean mandatory,
      boolean value,
      String hint,
      String error,
      JSONObject callback) {
    JSONObject object = null;
    try {
      object = new JSONObject();
      object.put("id", id);
      object.put("mandatory", mandatory);
      object.put("value", value);
      JSONObject settings = new JSONObject();
      if (!TextUtils.isEmpty(hint)) {
        JSONObject hintJson = new JSONObject();
        hintJson.put("label", hint);
        settings.put("hint", hintJson);
      }
      if (!TextUtils.isEmpty(error)) {
        JSONObject errorJson = new JSONObject();
        errorJson.put("label", error);
        settings.put("error", errorJson);
      }
      if (callback != null) {
        settings.put("callback", callback);
      }
      object.put("settings", settings);
    }catch (JSONException e){
      Log.e(LOG_TAG, "Json Exception",e);
    }
    return object;
  }
}