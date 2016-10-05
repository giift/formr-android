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

/**
 * @author vieony on 10/4/2016.
 */
@RunWith(AndroidJUnit4.class)
public class EmailTest {

  @Rule
  public ActivityTestRule<FieldsTestActivity> activityTestRule_ = new ActivityTestRule<>(
      FieldsTestActivity.class);

  @Before
  public void ScrollToEmail() {
    onView(withId(R.id.email)).perform(closeSoftKeyboard());
    onView(withId(R.id.email)).perform(scrollTo());
    onView(withId(R.id.email)).check(matches(isDisplayed()));
  }

  @Test
  public void SetLabel01() {
    onView(withId(R.id.email)).perform(SetLabel(null));
  }

  @Test
  public void SetLabel02() {
    onView(withId(R.id.email)).perform(SetLabel("null"));
  }

  @Test
  public void SetLabel03() {
    onView(withId(R.id.email)).perform(SetLabel("Enter your email"));
  }

  @Test
  public void SetHint01() {
    onView(withId(R.id.email)).perform(SetHint(null));
  }

  @Test
  public void SetHint02() {
    onView(withId(R.id.email)).perform(SetHint("null"));
  }

  @Test
  public void SetHint03() {
    onView(withId(R.id.email)).perform(SetHint("Email hint"));
  }

  @Test
  public void SetError01() {
    onView(withId(R.id.email)).perform(SetError(null));
  }

  @Test
  public void SetError02() {
    onView(withId(R.id.email)).perform(SetError("null"));
  }

  @Test
  public void SetError03() {
    onView(withId(R.id.email)).perform(SetError("Email error"));
  }

  @Test
  public void ValidateEmail01() {
    String email = "abc@gmail.com";
    onView(withId(R.id.email)).perform(SetEmail(email));
    onView(withId(R.id.email)).check(matches(Validate(true)));
  }

  @Test
  public void ValidateEmail02() {
    String email = "abc@gmail..com";
    onView(withId(R.id.email)).perform(SetEmail(email));
    onView(withId(R.id.email)).check(matches(Validate(false)));
  }

  private ViewAction SetLabel(final String label) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Email email = (Email) view;
        email.SetLabel(label);

      }

      @Override
      public String getDescription() {
        return "Set label for Email";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Email.class);
      }
    };
  }

  private ViewAction SetHint(final String hint) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Email email = (Email) view;
        email.SetHint(hint);

      }

      @Override
      public String getDescription() {
        return "Set hint for Email";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Email.class);
      }
    };
  }

  private ViewAction SetError(final String error) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Email email = (Email) view;
        email.SetError(error);

      }

      @Override
      public String getDescription() {
        return "Set error for Email";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Email.class);
      }
    };
  }

  private ViewAction SetEmail(final String emailValue) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Email email = (Email) view;
        email.SetText(emailValue);

      }

      @Override
      public String getDescription() {
        return "Set error for Email";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Email.class);
      }
    };
  }

  public static Matcher<View> GetFieldId(final String expectedId) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof Email)) {
          return false;
        }

        String id = ((Email) view).GetFieldId();

        return expectedId.equals(id);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Get Email Id");
      }
    };
  }

  public static Matcher<View> GetValue(final String expectedValue) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof Email)) {
          return false;
        }

        String value = ((Email) view).GetValues()[0];

        return expectedValue.equals(value);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Get Email");
      }
    };
  }

  public static Matcher<View> Validate(final boolean expectedValidation) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof Email)) {
          return false;
        }

        boolean validate = ((Email) view).Validate();

        return (validate==expectedValidation);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Get Validation Result");
      }
    };
  }
}