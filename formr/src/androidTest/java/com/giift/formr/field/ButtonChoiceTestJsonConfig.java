package com.giift.formr.field;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;
import android.view.View;

import com.giift.formr.R;
import com.giift.formr.activity.MainActivity;

import org.hamcrest.Matcher;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author vieony on 9/30/2016.
 */
@RunWith(AndroidJUnit4.class)
public class ButtonChoiceTestJsonConfig {
  @Rule
  public ActivityTestRule<MainActivity> activityTestRule_ = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void ButtonChoiceInitJson() {
    String id = Utils.GetUniqueStringId();
    LinkedHashMap<String, String> options = GetOptions();
    onView(withId(R.id.buttonChoice)).perform(
        initView(GetButtonChoiceJson(id, false, null, options, null, null)));
    onView(withId(R.id.buttonChoice)).check(matches(ButtonChoiceTest.Validate(false)));
  }

  @Test
  public void ButtonChoiceInitJsonCheckValue() {
    String id = Utils.GetUniqueStringId();
    LinkedHashMap<String, String> options = GetOptions();
    Object[] optionsArray = options.keySet().toArray();
    Object key = optionsArray[new Random().nextInt(optionsArray.length)];
    String value = key.toString();
    String hint = Utils.GetUniqueStringId();
    String error = Utils.GetUniqueStringId();
    onView(withId(R.id.buttonChoice)).perform(
        initView(GetButtonChoiceJson(id, true, value, options, hint, error)));
    onView(withId(R.id.buttonChoice)).check(matches(ButtonChoiceTest.Value(value)));
    onView(withId(R.id.buttonChoice)).check(matches(ButtonChoiceTest.Validate(true)));
  }

  private ViewAction initView(final JSONObject jsonObject) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        ButtonChoice buttonChoice = (ButtonChoice) view;
        buttonChoice.Init(jsonObject);
      }

      @Override
      public String getDescription() {
        return "Set position for ButtonChoice";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(ButtonChoice.class);
      }
    };
  }

  private JSONObject GetButtonChoiceJson(
      String id,
      boolean mandatory,
      String value,
      LinkedHashMap<String, String> options,
      String hint,
      String error) {
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
      object.put("settings", settings);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return object;
  }

  private LinkedHashMap<String, String> GetOptions() {
    LinkedHashMap<String, String> options = new LinkedHashMap<>();
    options.put("ten", "$ 10");
    options.put("twenty", "$ 20");
    options.put("thirty", "$ 30");
    return options;
  }
}