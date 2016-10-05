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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author vieony on 10/4/2016.
 */
@RunWith(AndroidJUnit4.class)
public class DateTestJsonConfig {

  private static final String LOG_TAG = DateTestJsonConfig.class.getName();
  @Rule
  public ActivityTestRule<FieldsTestActivity> activityTestRule_ = new ActivityTestRule<>(
      FieldsTestActivity.class);

  @Before
  public void ScrollToDate() {
    onView(withId(R.id.date)).perform(closeSoftKeyboard());
    onView(withId(R.id.date)).perform(scrollTo());
    onView(withId(R.id.date)).check(matches(isDisplayed()));
  }

  @Test
  public void DateInitJson01() {
    String id = Utils.GetUniqueStringId();
    onView(withId(R.id.date)).perform(
        initView(GetDateJson(id, true, null, null, null, null, null, null, null)));
    onView(withId(R.id.date)).check(matches(DateTest.GetFieldId(id)));
  }

  @Test
  public void DateInitJson02() {
    String id = Utils.GetUniqueStringId();
    String hint = Utils.GetUniqueStringId();
    String error = Utils.GetUniqueStringId();
    String label = Utils.GetUniqueStringId();
    String value = "2016-08-24";
    JSONObject callback = new JSONObject();
    try {
      callback.put("on_focus_lost", Utils.GetUniqueStringId());
      callback.put("on_value_changed", Utils.GetUniqueStringId());
    }catch (JSONException e){
      Log.e(LOG_TAG, "Json Exception", e);
    }
    onView(withId(R.id.date)).perform(
        initView(GetDateJson(id, true, value, label, hint, error, null, null, callback)));
    onView(withId(R.id.date)).check(matches(DateTest.GetFieldId(id)));
    onView(withId(R.id.date)).check(matches(DateTest.GetValue(value)));
  }

  @Test
  public void DateInitJson03() {
    String id = Utils.GetUniqueStringId();
    String start = "2015-08-24";
    String end = "2016-08-24";
    onView(withId(R.id.date)).perform(
        initView(GetDateJson(id, true, null, null, null, null, start, end, null)));
  }

  @Test
  public void DateInitJson04() {
    String id = Utils.GetUniqueStringId();
    String value = Utils.GetUniqueStringId();
    onView(withId(R.id.date)).perform(
        initView(GetDateJson(id, true, value, null, null, null, null, null, null)));
    onView(withId(R.id.date)).check(matches(DateTest.Validate(false)));
    onView(withId(R.id.date)).check(matches(DateTest.GetValue("")));
  }

  private ViewAction initView(final JSONObject jsonObject) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Date date = (Date) view;
        date.Init(jsonObject);
      }

      @Override
      public String getDescription() {
        return "Initialise Date Json";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Date.class);
      }
    };
  }

  private JSONObject GetDateJson(
      String id,
      boolean mandatory,
      String value,
      String label,
      String hint,
      String error,
      String start,
      String end,
      JSONObject callback) {
    JSONObject object = null;
    try {
      object = new JSONObject();
      object.put("id", id);
      object.put("mandatory", mandatory);
      object.put("value", value);
      object.put("label", label);
      JSONObject settings = new JSONObject();
      if (!TextUtils.isEmpty(start)) {
        settings.put("start_date", start);
      }
      if (!TextUtils.isEmpty(end)) {
        settings.put("end_date", end);
      }
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
    } catch (JSONException e) {
      Log.e(LOG_TAG, "Json Exception", e);
    }
    return object;
  }

}
