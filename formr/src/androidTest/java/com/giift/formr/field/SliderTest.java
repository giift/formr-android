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
 * @author vieony on 10/3/2016.
 */
@RunWith(AndroidJUnit4.class)
public class SliderTest {
  @Rule
  public ActivityTestRule<FieldsTestActivity> activityTestRule_ = new ActivityTestRule<>(
      FieldsTestActivity.class);

  @Before
  public void ScrollToSlider() {
    onView(withId(R.id.slider)).perform(closeSoftKeyboard());
    onView(withId(R.id.slider)).perform(scrollTo());
    onView(withId(R.id.slider)).check(matches(isDisplayed()));
  }

  @Test
  public void SetLabel01() {
    onView(withId(R.id.slider)).perform(SetLabel(null));
  }

  @Test
  public void SetLabel02() {
    onView(withId(R.id.slider)).perform(SetLabel("null"));
  }

  @Test
  public void SetLabel03() {
    onView(withId(R.id.slider)).perform(SetLabel("Available Options"));
  }

  @Test
  public void SetHint01() {
    onView(withId(R.id.slider)).perform(SetHint(null));
  }

  @Test
  public void SetHint02() {
    onView(withId(R.id.slider)).perform(SetHint("null"));
  }

  @Test
  public void SetHint03() {
    onView(withId(R.id.slider)).perform(SetHint("Select Value"));
  }

  @Test
  public void SetError01() {
    onView(withId(R.id.slider)).perform(SetError(null));
  }

  @Test
  public void SetError02() {
    onView(withId(R.id.slider)).perform(SetError("null"));
  }

  @Test
  public void SetError03() {
    onView(withId(R.id.slider)).perform(SetError("Slider error"));
  }

  @Test
  public void SetValue01() {
    onView(withId(R.id.slider)).perform(SetMin(10.00));
    onView(withId(R.id.slider)).perform(SetMax(200.00));
    onView(withId(R.id.slider)).perform(SetValue(100.00));
    onView(withId(R.id.slider)).check(matches(GetValue(100.00)));
  }

  private ViewAction SetLabel(final String label) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Slider slider = (Slider) view;
        slider.SetLabel(label);

      }

      @Override
      public String getDescription() {
        return "Set label for Slider";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Slider.class);
      }
    };
  }

  private ViewAction SetHint(final String hint) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Slider slider = (Slider) view;
        slider.SetHint(hint);

      }

      @Override
      public String getDescription() {
        return "Set hint for Slider";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Slider.class);
      }
    };
  }

  private ViewAction SetError(final String error) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Slider slider = (Slider) view;
        slider.SetError(error);

      }

      @Override
      public String getDescription() {
        return "Set error for Slider";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Slider.class);
      }
    };
  }

  private ViewAction SetMin(final double min) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Slider slider = (Slider) view;
        slider.SetMin(min);

      }

      @Override
      public String getDescription() {
        return "Set Minimum value for Slider";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Slider.class);
      }
    };
  }

  private ViewAction SetMax(final double max) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Slider slider = (Slider) view;
        slider.SetMax(max);

      }

      @Override
      public String getDescription() {
        return "Set Maximum value for Slider";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Slider.class);
      }
    };
  }

  private ViewAction SetValue(final double value) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        Slider slider = (Slider) view;
        slider.SetValue(value);

      }

      @Override
      public String getDescription() {
        return "Set value for Slider";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(Slider.class);
      }
    };
  }

  public static Matcher<View> GetValue(final double expectedValue) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof Slider)) {
          return false;
        }

        double value = ((Slider) view).GetValueAsDouble();

        return (expectedValue == value);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Get value Slider");
      }
    };
  }
}