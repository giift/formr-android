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
public class CheckboxTestJsonConfig {

  private static final String LOG_TAG = CheckboxTestJsonConfig.class.getName();

  @Rule
  public ActivityTestRule<FieldsTestActivity> activityTestRule_ = new ActivityTestRule<>(
      FieldsTestActivity.class);

  @Before
  public void ScrollToCheckbox() {
    onView(withId(R.id.checkbox)).perform(closeSoftKeyboard());
    onView(withId(R.id.checkbox)).perform(scrollTo());
    onView(withId(R.id.checkbox)).check(matches(isDisplayed()));
  }

  @Test
  public void CheckboxInitJson01() {
    String id = Utils.GetUniqueStringId();
    LinkedHashMap<String, String> options = GetOptions();
    onView(withId(R.id.checkbox)).perform(
        InitView(GetCheckboxJson(id, false, null, options, null, null, null)));
    onView(withId(R.id.checkbox)).check(matches(CheckboxTest.GetFieldId(id)));
  }

  @Test
  public void CheckboxInitJson02() {
    String id = Utils.GetUniqueStringId();
    LinkedHashMap<String, String> options = GetOptions();
    String hint = Utils.GetUniqueStringId();
    String error = Utils.GetUniqueStringId();
    JSONObject callback = new JSONObject();
    try {
      callback.put("on_focus_lost", Utils.GetUniqueStringId());
      callback.put("on_value_changed", Utils.GetUniqueStringId());
    }catch (JSONException e){
      Log.e(LOG_TAG, "Json Exception", e);
    }
    onView(withId(R.id.checkbox)).perform(
        InitView(GetCheckboxJson(id, true, null, options, hint, error, callback)));
  }

  private ViewAction InitView(final JSONObject jsonObject) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Checkbox checkbox = (Checkbox) view;
        checkbox.Init(jsonObject);
      }

      @Override
      public String getDescription() {
        return "Initialise Checkbox Json";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Checkbox.class);
      }
    };
  }
  private LinkedHashMap<String, String> GetOptions() {
    LinkedHashMap<String, String> options = new LinkedHashMap<>();
    options.put("1", "Monday");
    options.put("2", "Tuesday");
    options.put("3", "Wednesday");
    return options;
  }

  private JSONObject GetCheckboxJson(
      String id,
      boolean mandatory,
      String value,
      LinkedHashMap<String, String> options,
      String hint,
      String error,
      JSONObject callback){
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
    }catch (JSONException e){
      Log.e(LOG_TAG, "Json Exception", e);
    }
    return object;
  }
}