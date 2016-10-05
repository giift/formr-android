package com.giift.formr.field;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.giift.formr.R;
import com.giift.formr.activity.FieldsTestActivity;

import org.hamcrest.Matcher;
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
public class MonthTest {

  @Rule
  public ActivityTestRule<FieldsTestActivity> activityTestRule_ = new ActivityTestRule<>(
      FieldsTestActivity.class);

  @Before
  public void ScrollToMonth() {
    onView(withId(R.id.month)).perform(closeSoftKeyboard());
    onView(withId(R.id.month)).perform(scrollTo());
    onView(withId(R.id.month)).check(matches(isDisplayed()));
  }

  @Test
  public void SetLabel01() {
    onView(withId(R.id.month)).perform(SetLabel(null));
  }

  @Test
  public void SetLabel02() {
    onView(withId(R.id.month)).perform(SetLabel("null"));
  }

  @Test
  public void SetLabel03() {
    onView(withId(R.id.month)).perform(SetLabel("Select Month"));
  }

  @Test
  public void SetHint01() {
    onView(withId(R.id.month)).perform(SetHint(null));
  }

  @Test
  public void SetHint02() {
    onView(withId(R.id.month)).perform(SetHint("null"));
  }

  @Test
  public void SetHint03() {
    onView(withId(R.id.month)).perform(SetHint("Month hint"));
  }

  @Test
  public void SetError01() {
    onView(withId(R.id.month)).perform(SetError(null));
  }

  @Test
  public void SetError02() {
    onView(withId(R.id.month)).perform(SetError("null"));
  }

  @Test
  public void SetError03() {
    onView(withId(R.id.month)).perform(SetError("Month error"));
  }
  private ViewAction SetLabel(final String label) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Month month = (Month) view;
        month.SetLabel(label);

      }

      @Override
      public String getDescription() {
        return "Set label for Month";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Month.class);
      }
    };
  }

  private ViewAction SetHint(final String hint) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Month month = (Month) view;
        month.SetHint(hint);

      }

      @Override
      public String getDescription() {
        return "Set hint for Month";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Month.class);
      }
    };
  }

  private ViewAction SetError(final String error) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Month month = (Month) view;
        month.SetError(error);
      }

      @Override
      public String getDescription() {
        return "Set error for Month";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Month.class);
      }
    };
  }
}