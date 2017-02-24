package com.giift.formr.field;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.giift.formr.R;
import com.giift.formr.activity.FieldsTestActivity;

import org.hamcrest.Matcher;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;

/**
 * @author vieony on 10/3/2016.
 */
public class TextTestJsonConfig {

  private static final String LOG_TAG = TextTestJsonConfig.class.getName();

  @Rule
  public ActivityTestRule<FieldsTestActivity> activityTestRule_ = new ActivityTestRule<>(
      FieldsTestActivity.class);

  @Before
  public void ScrollToText() {
    onView(withId(R.id.text)).perform(closeSoftKeyboard());
    onView(withId(R.id.text)).perform(scrollTo());
    Matcher<View> linearLayout = allOf(isAssignableFrom(LinearLayout.class), withParent(withId(R.id.text)));
    Matcher<View> textInputLayout = allOf(isAssignableFrom(TextInputLayout.class), withParent(linearLayout));
    onView(textInputLayout).perform(closeSoftKeyboard());
    Matcher<View> frameLayoutLayout = allOf(isAssignableFrom(FrameLayout.class), withParent(textInputLayout));
    Matcher<View> textInputEditText = allOf(isAssignableFrom(TextInputEditText.class), withParent(frameLayoutLayout));
    onView(textInputEditText).perform(closeSoftKeyboard());
    onView(textInputEditText).perform(scrollTo());
  }


  @Test
  public void TextInitJson01() {
    String id = Utils.GetUniqueStringId();
    JSONObject jsonObject = GetTextJson(id, true, null, false, null, null, null);
    onView(withId(R.id.text)).perform(initView(jsonObject));
    onView(withId(R.id.text)).check(matches(TextTest.GetFieldId(id)));
  }

  @Test
  public void TextInitJson02() {
    String id = Utils.GetUniqueStringId();
    String value = Utils.GetUniqueStringId();
    String hint = Utils.GetUniqueStringId();
    String error = Utils.GetUniqueStringId();
    final JSONObject jsonObject;
    JSONObject callback = null;
    try {
      callback = new JSONObject();
      callback.put("on_focus_lost", Utils.GetUniqueStringId());
      callback.put("on_value_changed", Utils.GetUniqueStringId());
    } catch (JSONException e) {
      e.printStackTrace();
    }
    jsonObject = GetTextJson(id, true, value, true, hint, error, callback);
    onView(withId(R.id.text)).perform(initView(jsonObject));
    Matcher<View> linearLayout = allOf(isAssignableFrom(LinearLayout.class), withParent(withId(R.id.text)));
    onView(allOf(withTagValue(is((Object) "scannable")), withParent(linearLayout))).check(matches(isDisplayed()));
    onView(withId(R.id.text)).check(matches(TextTest.GetValue(value)));
  }

  private ViewAction initView(final JSONObject jsonObject) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Text text = (Text) view;
        text.Init(jsonObject);
      }

      @Override
      public String getDescription() {
        return "Initialise text JsonConfig";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Text.class);
      }
    };
  }

  private JSONObject GetTextJson(
      String id,
      boolean mandatory,
      String value,
      boolean scannable,
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
      settings.put("scannable", scannable);
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
