package com.giift.formr.field;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.giift.formr.R;
import com.giift.formr.activity.FieldsTestActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.core.AllOf.allOf;

/**
 * @author vieony on 10/3/2016.
 */
@RunWith(AndroidJUnit4.class)
public class DateTest {

  Matcher<View> dateMatcher_ = null;

  @Rule
  public ActivityTestRule<FieldsTestActivity> activityTestRule_ = new ActivityTestRule<>(
      FieldsTestActivity.class);

  @Before
  public void ScrollToDate() {
    onView(withId(R.id.date)).perform(closeSoftKeyboard());
    onView( withId( R.id.date)).perform( scrollTo());
    onView(withId(R.id.date)).check(matches(isDisplayed()));
    Matcher<View> linearLayout = allOf(isAssignableFrom(LinearLayout.class), withParent(withId(R.id.date)));
    dateMatcher_ = allOf(isAssignableFrom(AppCompatImageButton.class), withParent(linearLayout));
  }

  @Test
  public void SetLabel01() {
    onView(withId(R.id.date)).perform(SetLabel(null));
  }

  @Test
  public void SetLabel02() {
    onView(withId(R.id.date)).perform(SetLabel("null"));
  }

  @Test
  public void SetLabel03() {
    onView(withId(R.id.date)).perform(SetLabel("Select Date"));
  }

  @Test
  public void SetHint01() {
    onView(withId(R.id.date)).perform(SetHint(null));
  }

  @Test
  public void SetHint02() {
    onView(withId(R.id.date)).perform(SetHint("null"));
  }

  @Test
  public void SetHint03() {
    onView(withId(R.id.date)).perform(SetHint("Date hint"));
  }

  @Test
  public void SetError01() {
    onView(withId(R.id.date)).perform(SetError(null));
  }

  @Test
  public void SetError02() {
    onView(withId(R.id.date)).perform(SetError("null"));
  }

  @Test
  public void SetError03() {
    onView(withId(R.id.date)).perform(SetError("Date error"));
  }

  @Test
  public void SetDate() {
    java.util.Date currentDate = new java.util.Date(System.currentTimeMillis());
    onView(withId(R.id.date)).perform(SetDate(currentDate));
    onView(withId(R.id.date)).check(matches(GetValue(currentDate)));
  }

  @Test
  public void SelectDate() {
    onView(dateMatcher_).perform(click());
    onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(SetDate(1984, 10 + 1, 23));
    onView(withId(android.R.id.button1)).perform(click());
  }


  private ViewAction SetLabel(final String label) {
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

  private ViewAction SetHint(final String hint) {
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

  private ViewAction SetError(final String error) {
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

  private ViewAction SetDate(final java.util.Date dateValue) {
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


  public static ViewAction SetDate(final int day, final int month, final int year) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        ((DatePicker) view).updateDate(year, month, day);
      }

      @Override
      public String getDescription() {
        return "Set the date into the datepicker(day, month, year)";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(DatePicker.class);
      }
    };
  }

  public static Matcher<View> GetFieldId(final String expectedId) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof Date)) {
          return false;
        }

        String id = ((Date) view).GetFieldId();

        return expectedId.equals(id);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Get Date Id");
      }
    };
  }

  public static Matcher<View> GetValue(final java.util.Date expectedValue) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof Date)) {
          return false;
        }

        java.util.Date value = ((Date) view).GetDate();

        return expectedValue.equals(value);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Get currently selected Date");
      }
    };
  }

  public static Matcher<View> GetValue(final String expectedValue) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof Date)) {
          return false;
        }

        String value = ((Date) view).GetValues()[0];

        return expectedValue.equals(value);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Get currently selected Date");
      }
    };
  }

  public static Matcher<View> Validate(final boolean expectedValidation) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof Date)) {
          return false;
        }

        boolean validate = ((Date) view).Validate();

        return (validate==expectedValidation);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Get Validation Result");
      }
    };
  }

}