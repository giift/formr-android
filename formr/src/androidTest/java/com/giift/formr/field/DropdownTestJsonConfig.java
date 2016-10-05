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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

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
public class DropdownTestJsonConfig {

  private static final String LOG_TAG = DropdownTestJsonConfig.class.getName();

  @Rule
  public ActivityTestRule<FieldsTestActivity> activityTestRule_ = new ActivityTestRule<>(
      FieldsTestActivity.class);

  @Before
  public void ScrollToDropdown() {
    onView(withId(R.id.dropdown)).perform(closeSoftKeyboard());
    onView(withId(R.id.dropdown)).perform(scrollTo());
    onView(withId(R.id.dropdown)).check(matches(isDisplayed()));
  }

  @Test
  public void DropdownInitJson01() {
    String id = Utils.GetUniqueStringId();
    onView(withId(R.id.dropdown)).perform(
        InitView(GetDropdownJson(id, false, null, GetOptions(), null, null, null)));
    onView(withId(R.id.dropdown)).check(matches(DropdownTest.GetFieldId(id)));
  }

  @Test
  public void DropdownInitJson02() {
    String id = Utils.GetUniqueStringId();
    LinkedHashMap<String, String> options = GetOptions();
    Object[] randomValue = options.keySet().toArray();
    Object key = randomValue[new Random().nextInt(randomValue.length)];
    String value = key.toString();
    String hint = Utils.GetUniqueStringId();
    String error = Utils.GetUniqueStringId();
    JSONObject callback = new JSONObject();
    try {
      callback.put("on_focus_lost", Utils.GetUniqueStringId());
      callback.put("on_value_changed", Utils.GetUniqueStringId());
    }catch (JSONException e){
      Log.e(LOG_TAG, "Json Exception", e);
    }
    onView(withId(R.id.dropdown)).perform(
        InitView(GetDropdownJson(id, true, value, options, hint, error, callback)));
    onView(withId(R.id.dropdown)).check(matches(DropdownTest.GetValue(value)));
  }

  private ViewAction InitView(final JSONObject jsonObject) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Dropdown dropdown = (Dropdown) view;
        dropdown.Init(jsonObject);
      }

      @Override
      public String getDescription() {
        return "Initialise Dropdown Json";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Dropdown.class);
      }
    };
  }

  private LinkedHashMap<String, String> GetOptions() {
    LinkedHashMap<String, String> options = new LinkedHashMap<>();
    options.put("FR", "France");
    options.put("KR", "Korea");
    options.put("US", "United States");
    return options;
  }

  private JSONObject GetDropdownJson(
      String id,
      boolean mandatory,
      String value,
      LinkedHashMap<String, String> options,
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
      if (options != null) {
        JSONObject optionsJson = new JSONObject();
        for (Map.Entry<String, String> e : options.entrySet()) {
          optionsJson.put(e.getKey(), e.getValue());
        }
        settings.put("options", optionsJson);
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
