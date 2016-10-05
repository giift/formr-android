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
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author vieony on 9/30/2016.
 */
@RunWith(AndroidJUnit4.class)
public class CardNumberTestJsonConfig {

  private static final String LOG_TAG = CardNumberTestJsonConfig.class.getName();

  @Rule
  public ActivityTestRule<FieldsTestActivity> activityTestRule_ = new ActivityTestRule<>(
      FieldsTestActivity.class);

  @Before
  public void ScrollToCardNumber() {
    onView(withId(R.id.cardNumber)).perform(closeSoftKeyboard());
    onView(withId(R.id.cardNumber)).perform(scrollTo());
  }

  @Test
  public void CardNumberInitJson01() {
    String id = Utils.GetUniqueStringId();
    onView(withId(R.id.cardNumber)).perform(
        initView(GetTextJson(id, false, null, null, null)));
    onView(withId(R.id.cardNumber)).check(matches(CardNumberTest.GetFieldId(id)));
  }

  @Test
  public void CardNumberInitJson02() {
    String id = Utils.GetUniqueStringId();
    String value = "1234567812345678";
    String hint = Utils.GetUniqueStringId();
    onView(withId(R.id.cardNumber)).perform(
        initView(GetTextJson(id, true, value, hint, null)));
    onView(withId(R.id.cardNumber)).check(matches(CardNumberTest.GetValue(value)));
  }

  @Test
  public void CardNumberInitJson03() {
    String id = Utils.GetUniqueStringId();
    String value = "4012888888881881";
    onView(withId(R.id.cardNumber)).perform(
        initView(GetTextJson(id, true, value, null, null)));
    onView(withId(R.id.cardNumber)).check(matches(CardNumberTest.GetValue(value)));
    onView(withId(R.id.cardNumber)).check(matches(CardNumberTest.Validate(true)));
  }

  @Test
  public void CardNumberInitJson04() {
    String id = Utils.GetUniqueStringId();
    String value = "0000123488881881";
    onView(withId(R.id.cardNumber)).perform(
        initView(GetTextJson(id, false, value, null, null)));
    onView(withId(R.id.cardNumber)).check(matches(CardNumberTest.Validate(false)));
  }


  private ViewAction initView(final JSONObject jsonObject) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        CardNumber cardNumber = (CardNumber) view;
        cardNumber.Init(jsonObject);
      }

      @Override
      public String getDescription() {
        return "Set position for ButtonChoice";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(CardNumber.class);
      }
    };
  }

  private JSONObject GetTextJson(
      String id,
      boolean mandatory,
      String value,
      String hint,
      String error) {
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
      object.put("settings", settings);
    } catch (JSONException e) {
      Log.e(LOG_TAG, "Json Exception", e);

    }
    return object;
  }
}
