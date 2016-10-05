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
public class EmailTestJsonConfig {
  @Rule
  public ActivityTestRule<FieldsTestActivity> activityTestRule_ = new ActivityTestRule<>(
      FieldsTestActivity.class);

  private static final String LOG_TAG = EmailTestJsonConfig.class.getName();

  @Before
  public void ScrollToEmail() {
    onView(withId(R.id.email)).perform(closeSoftKeyboard());
    onView(withId(R.id.email)).perform(scrollTo());
    onView(withId(R.id.email)).check(matches(isDisplayed()));
  }

  @Test
  public void EmailInitJson01() {
    String id = Utils.GetUniqueStringId();
    onView(withId(R.id.email)).perform(
        initView(GetEmailJson(id, true, null, null, null, null)));
    onView(withId(R.id.email)).check(matches(EmailTest.GetFieldId(id)));
  }

  @Test
  public void EmailInitJson02() {
    String id = Utils.GetUniqueStringId();
    final String value = String.format("vieony.dacosta+%s@gmail.com", Utils.GetUniqueStringId());
    String hint = Utils.GetUniqueStringId();
    String error = Utils.GetUniqueStringId();
    JSONObject callback = new JSONObject();
    try {
      callback.put("on_focus_lost", Utils.GetUniqueStringId());
      callback.put("on_value_changed", Utils.GetUniqueStringId());
    }
    catch (JSONException e){
      Log.e(LOG_TAG, "Json Exception",e);
    }
    onView(withId(R.id.email)).perform(
        initView(GetEmailJson(id, true, value, hint, error, callback)));
    onView(withId(R.id.email)).check(matches(EmailTest.GetValue(value)));
  }

  private ViewAction initView(final JSONObject jsonObject) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Email email = (Email) view;
        email.Init(jsonObject);
      }

      @Override
      public String getDescription() {
        return "Initialise Email Json";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Email.class);
      }
    };
  }

  private JSONObject GetEmailJson(
      String id,
      boolean mandatory,
      String value,
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
          Log.e(LOG_TAG,"Json Exception", e);
        }
    return object;
  }
}
