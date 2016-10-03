package com.giift.formr.field;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.giift.formr.R;
import com.giift.formr.activity.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * @author vieony on 9/30/2016.
 */
@RunWith(AndroidJUnit4.class)
public class TextTest {
  @Rule
  public ActivityTestRule<MainActivity> activityTestRule_ = new ActivityTestRule<>(
      MainActivity.class);

  @Before
  public void ScrollToText() {
    onView(withId( R.id.text)).perform( scrollTo());
  }

  @Test
  public void ViewVisible() {
    onView(withId(R.id.text)).check(matches(isDisplayed()));
  }

  @Test
  public void TypeText() {
    String value = Utils.GetUniqueStringId();
    onView(withId(R.id.text)).perform(click());
    onView(isAssignableFrom(TextInputEditText.class)).perform(click(), typeText(value));
    onView(isAssignableFrom(TextInputEditText.class)).check(matches(withText(value)));
  }

  @Test
  public void SetLabel01() {
    onView(withId(R.id.text)).perform(setLabel(null));
  }

  @Test
  public void SetLabel02() {
    onView(withId(R.id.text)).perform(setLabel("null"));
  }

  @Test
  public void SetLabel03() {
    String label = "Enter Value";
    onView(withId(R.id.text)).perform(setLabel(label));
    onView(withId(R.id.text)).perform(click());
    onView(isAssignableFrom(TextInputLayout.class)).check(matches(hasTextInputLayoutHintText(label)));
  }

  @Test
  public void SetHint01() {
    onView(withId(R.id.text)).perform(setHint(null));
  }

  @Test
  public void SetHint02() {
    onView(withId(R.id.text)).perform(setHint("null"));
  }

  @Test
  public void SetHint03() {
    onView(withId(R.id.text)).perform(setHint("Text hint"));
  }

  @Test
  public void SetError01() {
    onView(withId(R.id.text)).perform(setError(null));
  }

  @Test
  public void SetError02() {
    onView(withId(R.id.text)).perform(setError("null"));
  }

  @Test
  public void SetError03() {
    onView(withId(R.id.text)).perform(setError("Text error"));
  }

  @Test
  public void OrientationChange() {
    String value = Utils.GetUniqueStringId();
    onView(withId(R.id.text)).perform(click());
    onView(isAssignableFrom(TextInputEditText.class)).perform(click(), typeText(value));
    onView(isAssignableFrom(TextInputEditText.class)).check(matches(withText(value)));
    Utils.rotateScreen(this.activityTestRule_);
    onView(isAssignableFrom(TextInputEditText.class)).check(matches(withText(value)));
  }

  private ViewAction setLabel(final String label) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Text text = (Text) view;
        text.SetLabel(label);

      }

      @Override
      public String getDescription() {
        return "Set label for Text";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Text.class);
      }
    };
  }

  private ViewAction setHint(final String hint) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Text text = (Text) view;
        text.SetHint(hint);

      }

      @Override
      public String getDescription() {
        return "Set hint for Text";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Text.class);
      }
    };
  }

  private ViewAction setError(final String error) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Text text = (Text) view;
        text.SetError(error);

      }

      @Override
      public String getDescription() {
        return "Set error for Text";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Text.class);
      }
    };
  }

  public static Matcher<View> hasTextInputLayoutHintText(final String expectedHintText) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof TextInputLayout)) {
          return false;
        }

        CharSequence hint = ((TextInputLayout) view).getHint();

        if (hint == null) {
          return false;
        }

        String hintValue = hint.toString();

        return expectedHintText.equals(hintValue);
      }

      @Override
      public void describeTo(Description description) {
      }
    };
  }

}