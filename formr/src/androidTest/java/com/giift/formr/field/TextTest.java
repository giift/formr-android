package com.giift.formr.field;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

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

/**
 * @author vieony on 9/30/2016.
 */
@RunWith(AndroidJUnit4.class)
public class TextTest {
  Matcher<View> textInputLayout_ = null;
  Matcher<View> textInputEditText_ = null;

  @Rule
  public ActivityTestRule<FieldsTestActivity> activityTestRule_ = new ActivityTestRule<>(
      FieldsTestActivity.class);

  @Before
  public void ScrollToText() {
    onView(withId(R.id.text)).perform(closeSoftKeyboard());
    onView(withId(R.id.text)).perform(scrollTo());
    onView(withId(R.id.text)).check(matches(isDisplayed()));
    Matcher<View> linearLayout = allOf(isAssignableFrom(LinearLayout.class), withParent(withId(R.id.text)));
    textInputLayout_ = allOf(isAssignableFrom(TextInputLayout.class), withParent(linearLayout));
    onView(textInputLayout_).perform(scrollTo());
    Matcher<View> frameLayoutLayout = allOf(isAssignableFrom(FrameLayout.class), withParent(textInputLayout_));
    onView(frameLayoutLayout).perform(scrollTo());
    textInputEditText_ = allOf(isAssignableFrom(TextInputEditText.class), withParent(frameLayoutLayout));
    onView(textInputEditText_).perform(scrollTo());
  }

  @Test
  public void TypeText() {
    String value = Utils.GetUniqueStringId();
    onView(textInputEditText_).perform(click(), typeText(value));
    onView(textInputEditText_).check(matches(withText(value)));
  }

  @Test
  public void SetLabel01() {
    onView(withId(R.id.text)).perform(SetLabel(null));
  }

  @Test
  public void SetLabel02() {
    onView(withId(R.id.text)).perform(SetLabel("null"));
  }

  @Test
  public void SetLabel03() {
    String label = "Enter Value";
    onView(withId(R.id.text)).perform(SetLabel(label));
    onView(textInputLayout_).check(matches(HasTextInputLayoutHintText(label)));
  }

  @Test
  public void SetHint01() {
    onView(withId(R.id.text)).perform(SetHint(null));
  }

  @Test
  public void SetHint02() {
    onView(withId(R.id.text)).perform(SetHint("null"));
  }

  @Test
  public void SetHint03() {
    onView(withId(R.id.text)).perform(SetHint("Text hint"));
  }

  @Test
  public void SetError01() {
    onView(withId(R.id.text)).perform(SetError(null));
  }

  @Test
  public void SetError02() {
    onView(withId(R.id.text)).perform(SetError("null"));
  }

  @Test
  public void SetError03() {
    onView(withId(R.id.text)).perform(SetError("Text error"));
  }

  @Test
  public void SetText01() {
    onView(withId(R.id.text)).perform(SetText("Hello"));
  }

  @Test
  public void SetMandatory() {
    onView(withId(R.id.text)).perform(SetMandatory(true));
    onView(withId(R.id.text)).check(matches(Validate(false)));
    String expectedError = InstrumentationRegistry.getTargetContext().getString(R.string.error_field_required);
    onView(textInputEditText_).perform(click());
    onView(textInputLayout_).check(matches(HasTextInputLayoutErrorText(expectedError)));
    onView(textInputEditText_).perform(closeSoftKeyboard());
  }

  @Test
  public void OrientationChange() {
    String value = Utils.GetUniqueStringId();
    onView(textInputEditText_).perform(click(), typeText(value));
    onView(textInputEditText_).check(matches(withText(value)));
    onView(textInputEditText_).perform(closeSoftKeyboard());
    Utils.rotateScreen(this.activityTestRule_);
    onView(textInputEditText_).check(matches(withText(value)));
  }

  private ViewAction SetLabel(final String label) {
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

  private ViewAction SetHint(final String hint) {
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

  private ViewAction SetError(final String error) {
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

  private ViewAction SetText(final CharSequence textValue) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Text text = (Text) view;
        text.SetText(textValue);

      }

      @Override
      public String getDescription() {
        return "Set Text for Text";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Text.class);
      }
    };
  }

  private ViewAction SetMandatory(final Boolean value) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Text text = (Text) view;
        text.SetMandatory(value);

      }

      @Override
      public String getDescription() {
        return "Set mandatory for Text";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Text.class);
      }
    };
  }

  public static Matcher<View> GetFieldId(final String expectedId) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof Text)) {
          return false;
        }

        String id = ((Text) view).GetFieldId();

        return expectedId.equals(id);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Get text Id");
      }
    };
  }

  public static Matcher<View> GetValue(final String expectedValue) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof Text)) {
          return false;
        }

        String value = ((Text) view).GetValues()[0];

        return expectedValue.equals(value);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Get Text input value");
      }
    };
  }

  public static Matcher<View> Validate(final boolean expectedValidation) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof Text)) {
          return false;
        }

        boolean validate = ((Text) view).Validate();

        return (validate == expectedValidation);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Get Validation Result");
      }
    };
  }

  public static Matcher<View> HasTextInputLayoutHintText(final String expectedHintText) {
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
        description.appendText("Text Get Hint");
      }
    };
  }

  public static Matcher<View> HasTextInputLayoutErrorText(final String expectedErrorText) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof TextInputLayout)) {
          return false;
        }

        CharSequence error = ((TextInputLayout) view).getError();

        if (error == null) {
          return false;
        }

        String errorValue = error.toString();

        return expectedErrorText.equals(errorValue);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Text Get Error");

      }
    };
  }

}