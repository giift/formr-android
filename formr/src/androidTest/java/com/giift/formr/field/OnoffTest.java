package com.giift.formr.field;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.giift.formr.R;
import com.giift.formr.activity.FieldsTestActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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
import static org.junit.Assert.*;

/**
 * @author vieony on 10/4/2016.
 */
@RunWith(AndroidJUnit4.class)
public class OnoffTest {

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
  public void SetLabel01() {
    onView(withId(R.id.onoff)).perform(SetLabel(null));
  }

  @Test
  public void SetLabel02() {
    onView(withId(R.id.onoff)).perform(SetLabel("null"));
  }

  @Test
  public void SetLabel03() {
    onView(withId(R.id.onoff)).perform(SetLabel("Please accept"));
  }

  @Test
  public void SetHint01() {
    onView(withId(R.id.onoff)).perform(SetHint(null));
  }

  @Test
  public void SetHint02() {
    onView(withId(R.id.onoff)).perform(SetHint("null"));
  }

  @Test
  public void SetHint03() {
    onView(withId(R.id.onoff)).perform(SetHint("Onoff hint"));
  }

  @Test
  public void SetError01() {
    onView(withId(R.id.onoff)).perform(SetError(null));
  }

  @Test
  public void SetError02() {
    onView(withId(R.id.onoff)).perform(SetError("null"));
  }

  @Test
  public void SetError03() {
    onView(withId(R.id.onoff)).perform(SetError("Onoff error"));
  }

  private ViewAction SetLabel(final String label) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Onoff onoff = (Onoff) view;
        onoff.SetLabel(label);

      }

      @Override
      public String getDescription() {
        return "Set label for Onoff";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Onoff.class);
      }
    };
  }

  private ViewAction SetHint(final String hint) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Onoff onoff = (Onoff) view;
        onoff.SetHint(hint);

      }

      @Override
      public String getDescription() {
        return "Set hint for Onoff";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Onoff.class);
      }
    };
  }

  private ViewAction SetError(final String error) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Onoff onoff = (Onoff) view;
        onoff.SetError(error);
      }

      @Override
      public String getDescription() {
        return "Set error for Onoff";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Onoff.class);
      }
    };
  }
  public static Matcher<View> GetFieldId(final String expectedId) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof Onoff)) {
          return false;
        }

        String id = ((Onoff) view).GetFieldId();

        return expectedId.equals(id);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Get Onoff Id");
      }
    };
  }

  public static Matcher<View> GetValue(final boolean expectedValue) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof Onoff)) {
          return false;
        }

        String value = ((Onoff) view).GetValues()[0];

        return (expectedValue == Boolean.valueOf(value));
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Get Onoff value");
      }
    };
  }

}