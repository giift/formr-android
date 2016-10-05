package com.giift.formr.field;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.giift.formr.R;
import com.giift.formr.activity.FieldsTestActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

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
public class DropdownTest {

  @Rule
  public ActivityTestRule<FieldsTestActivity> activityTestRule_ = new ActivityTestRule<>(
      FieldsTestActivity.class);

  @Before
  public void ScrollToDropdown() {
    onView(withId(R.id.dropdown)).perform(closeSoftKeyboard());
    onView(withId(R.id.dropdown)).perform(scrollTo());
    onView(withId(R.id.dropdown)).check(matches(isDisplayed()));
  }

  @Test
  public void SetLabel01() {
    onView(withId(R.id.dropdown)).perform(SetLabel(null));
  }

  @Test
  public void SetLabel02() {
    onView(withId(R.id.dropdown)).perform(SetLabel("null"));
  }

  @Test
  public void SetLabel03() {
    onView(withId(R.id.dropdown)).perform(SetLabel("Enter your email"));
  }

  @Test
  public void SetHint01() {
    onView(withId(R.id.dropdown)).perform(SetHint(null));
  }

  @Test
  public void SetHint02() {
    onView(withId(R.id.dropdown)).perform(SetHint("null"));
  }

  @Test
  public void SetHint03() {
    onView(withId(R.id.dropdown)).perform(SetHint("Email hint"));
  }

  @Test
  public void SetError01() {
    onView(withId(R.id.dropdown)).perform(SetError(null));
  }

  @Test
  public void SetError02() {
    onView(withId(R.id.dropdown)).perform(SetError("null"));
  }

  @Test
  public void SetError03() {
    onView(withId(R.id.dropdown)).perform(SetError("Email error"));
  }

  @Test
  public void SetAdapter() {
    String options[] = {"Option 1", "Option 2", "Option 3"};
    int position = new Random().nextInt(options.length);
    ArrayAdapter<String> adapter = new ArrayAdapter<>(activityTestRule_.getActivity(), android.R.layout.simple_spinner_dropdown_item, options);
    onView(withId(R.id.dropdown)).perform(SetAdapter(adapter));
    onView(withId(R.id.dropdown)).perform(SetSelection(position));
    onView(withId(R.id.dropdown)).check(matches(GetSelectedPosition(position)));

  }

  private ViewAction SetLabel(final String label) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Dropdown dropdown = (Dropdown) view;
        dropdown.SetLabel(label);

      }

      @Override
      public String getDescription() {
        return "Set label for Dropdown";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Dropdown.class);
      }
    };
  }

  private ViewAction SetHint(final String hint) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Dropdown dropdown = (Dropdown) view;
        dropdown.SetHint(hint);

      }

      @Override
      public String getDescription() {
        return "Set hint for Dropdown";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Dropdown.class);
      }
    };
  }

  private ViewAction SetError(final String error) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Dropdown dropdown = (Dropdown) view;
        dropdown.SetError(error);
      }

      @Override
      public String getDescription() {
        return "Set error for Dropdown";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Dropdown.class);
      }
    };
  }

  public static Matcher<View> GetValue(final String expectedValue) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof Dropdown)) {
          return false;
        }

        String value = ((Dropdown) view).GetValues()[0];

        return expectedValue.equals(value);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Get currently selected Option value");
      }
    };
  }

  private ViewAction SetAdapter(final SpinnerAdapter adapter) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Dropdown dropdown = (Dropdown) view;
        dropdown.SetAdapter(adapter);

      }

      @Override
      public String getDescription() {
        return "Set spinner adapter for Dropdown";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Dropdown.class);
      }
    };
  }

  private ViewAction SetSelection(final int position) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Dropdown dropdown = (Dropdown) view;
        dropdown.SetSelection(position);

      }

      @Override
      public String getDescription() {
        return "Set spinner position for Dropdown";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Dropdown.class);
      }
    };
  }

  public static Matcher<View> GetFieldId(final String expectedId) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof Dropdown)) {
          return false;
        }

        String id = ((Dropdown) view).GetFieldId();

        return expectedId.equals(id);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Get Dropdown Id");
      }
    };
  }

  public static Matcher<View> GetSelectedPosition(final int expectedValue) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof Dropdown)) {
          return false;
        }

        int value = ((Dropdown) view).GetSelectedItemPosition();

        return (expectedValue==value);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Get currently selected Position");
      }
    };
  }
}