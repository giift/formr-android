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
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author vieony on 10/3/2016.
 */
@RunWith(AndroidJUnit4.class)
public class SliderTestJsonConfig {

  private static final String LOG_TAG = SliderTestJsonConfig.class.getName();

  @Rule
  public ActivityTestRule<FieldsTestActivity> activityTestRule_ = new ActivityTestRule<>(
      FieldsTestActivity.class);

  @Before
  public void ScrollToSlider() {
    onView(withId(R.id.slider)).perform(scrollTo());
    onView(withId(R.id.slider)).check(matches(isDisplayed()));
  }

  @Test
  public void SliderInitJson01() {
    String id = Utils.GetUniqueStringId();
    String label = "Select Amount to transfer";
    String hint = "Slider hint";
    JSONObject fx = null;
    try {
      fx = new JSONObject();
      fx.put("unit", "usd");
      JSONObject a = new JSONObject();
      JSONArray array = new JSONArray();
      JSONObject params = new JSONObject();
      params.put("value", 0.0014);
      params.put("min", 10000);
      params.put("max", 19999);
      array.put(params);
      params = new JSONObject();
      params.put("value", 0.0016);
      params.put("min", 20000);
      params.put("max", 21999);
      array.put(params);
      params = new JSONObject();
      params.put("value", 0.0041);
      params.put("min", 22000);
      params.put("max", 49999);
      array.put(params);
      a.put("type","stepped");
      a.put("params", array);
      fx.put("a",a);
      JSONObject b = new JSONObject();
      b.put("type", "fixed");
      params = new JSONObject();
      params.put("value", 0);
      b.put("params", params);
      fx.put("b", b);
    }catch (Exception e){
      Log.e(LOG_TAG, "Json Exception", e);
    }
    onView(withId(R.id.slider)).perform(
        initView(GetSliderJson(id,label, true, 50.00, hint, null, 10.00, 100.00, 1.00, "miles", fx)));
  }

  private ViewAction initView(final JSONObject jsonObject) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Slider slider = (Slider) view;
        slider.Init(jsonObject);
      }

      @Override
      public String getDescription() {
        return "Initialise Slider Json";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Slider.class);
      }
    };
  }

  private JSONObject GetSliderJson(
      String id,
      String label,
      boolean mandatory,
      double value,
      String hint,
      String error,
      double min,
      double max,
      double step,
      String unit,
      JSONObject fx) {
    JSONObject object = null;
    try {
      object = new JSONObject();
      object.put("id", id);
      object.put("mandatory", mandatory);
      object.put("value", value);
      object.put("label", label);
      JSONObject settings = new JSONObject();
      settings.put("min",min);
      settings.put("max",max);
      settings.put("step",step);
      settings.put("unit",unit);
      settings.put("fx",fx);
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
    }catch (Exception e){
      Log.e(LOG_TAG, "Json Exception", e);
    }
    return object;
  }
}
