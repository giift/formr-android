package com.giift.formr.field;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.giift.formr.R;
import com.giift.formr.activity.FieldsTestActivity;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

/**
 * @author vieony on 10/3/2016.
 */
@RunWith(AndroidJUnit4.class)
public class DayTest {
  Matcher textInputLayout_ = null;
  Matcher<View> textInputEditText_ = null;

  @Rule
  public ActivityTestRule<FieldsTestActivity> activityTestRule_ = new ActivityTestRule<>(
      FieldsTestActivity.class);
  @Before
  public void ScrollToDay() {
    onView(withId(R.id.day)).perform(closeSoftKeyboard());
    onView(withId(R.id.day)).perform(scrollTo());
    onView(withId(R.id.day)).check(matches(isDisplayed()));
    Matcher<View> linearLayout = allOf(isAssignableFrom(LinearLayout.class), withParent(withId(R.id.day)));
    textInputLayout_ = allOf(isAssignableFrom(TextInputLayout.class), withParent(linearLayout));
    Matcher<View> frameLayoutLayout = allOf(isAssignableFrom(FrameLayout.class), withParent(textInputLayout_));
    textInputEditText_ = allOf(isAssignableFrom(TextInputEditText.class), withParent(frameLayoutLayout));
  }

  @Test
  public void TypeText01() {
    String value = "abc";
    onView(textInputEditText_).perform(click(), typeText(value));
    onView(textInputEditText_).check(matches(withText("")));
    onView(textInputEditText_).perform(closeSoftKeyboard());
  }

  @Test
  public void TypeText02() {
    String value = "123";
    onView(textInputEditText_).perform(click(), typeText(value));
    onView(textInputEditText_).check(matches(withText(value.substring(0,2))));
    onView(textInputEditText_).perform(closeSoftKeyboard());
  }
}