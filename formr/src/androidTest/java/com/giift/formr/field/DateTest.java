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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * @author vieony on 10/3/2016.
 */
@RunWith(AndroidJUnit4.class)
public class DateTest {

  @Rule
  public ActivityTestRule<FieldsTestActivity> activityTestRule_ = new ActivityTestRule<>(
      FieldsTestActivity.class);

  @Before
  public void ScrollToDate() {
    onView( withId( R.id.date)).perform( scrollTo());
    onView(withId(R.id.date)).check(matches(isDisplayed()));
  }

  @Test
  public void SetLabel01() {
    onView(withId(R.id.date)).perform(setLabel(null));
  }

  @Test
  public void SetLabel02() {
    onView(withId(R.id.date)).perform(setLabel("null"));
  }

  @Test
  public void SetLabel03() {
    onView(withId(R.id.date)).perform(setLabel("Select Date"));
  }

  @Test
  public void SetHint01() {
    onView(withId(R.id.date)).perform(setHint(null));
  }

  @Test
  public void SetHint02() {
    onView(withId(R.id.date)).perform(setHint("null"));
  }

  @Test
  public void SetHint03() {
    onView(withId(R.id.date)).perform(setHint("Date hint"));
  }

  @Test
  public void SetError01() {
    onView(withId(R.id.date)).perform(setError(null));
  }

  @Test
  public void SetError02() {
    onView(withId(R.id.date)).perform(setError("null"));
  }

  @Test
  public void SetError03() {
    onView(withId(R.id.date)).perform(setError("Date error"));
  }

  private ViewAction setLabel(final String label) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Date date = (Date) view;
        date.SetLabel(label);

      }

      @Override
      public String getDescription() {
        return "Set label for Date";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Date.class);
      }
    };
  }

  private ViewAction setHint(final String hint) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Date date = (Date) view;
        date.SetHint(hint);

      }

      @Override
      public String getDescription() {
        return "Set hint for Date";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Date.class);
      }
    };
  }

  private ViewAction setError(final String error) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Date date = (Date) view;
        date.SetError(error);

      }

      @Override
      public String getDescription() {
        return "Set error for Date";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Date.class);
      }
    };
  }

  private ViewAction setDate(final java.util.Date dateValue) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Date date = (Date) view;
        date.SetDate(dateValue);

      }

      @Override
      public String getDescription() {
        return "Set Date";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Date.class);
      }
    };
  }
}