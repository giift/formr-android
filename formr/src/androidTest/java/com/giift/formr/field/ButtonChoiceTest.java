package com.giift.formr.field;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.util.Pair;
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

import java.util.ArrayList;
import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author vieony on 9/30/2016.
 */
@RunWith(AndroidJUnit4.class)
public class ButtonChoiceTest {

  @Rule
  public ActivityTestRule<MainActivity> activityTestRule_ = new ActivityTestRule<>(
      MainActivity.class);

  @Before
  public void InitButtonChoice() {
    onView(withId(R.id.buttonChoice)).perform(initView());
  }

  @Test
  public void ViewVisible() {
    onView(withId(R.id.buttonChoice)).check(matches(isDisplayed()));
  }

  @Test
  public void GetValue() {
    onView(withId(R.id.buttonChoice)).perform(setPosition(0));
    onView(withId(R.id.buttonChoice)).check(matches(Value("0")));
  }

  @Test
  public void SelectPosition01() {
    int random = new Random().nextInt(3);
    onView(withId(R.id.buttonChoice)).perform(setPosition(random));
  }

  @Test
  public void SelectPosition02() {
    int random = new Random().nextInt(100);
    onView(withId(R.id.buttonChoice)).perform(setPosition(random));
  }


  @Test
  public void SetLabel01() {
    onView(withId(R.id.buttonChoice)).perform(setLabel(null));
  }

  @Test
  public void SetLabel02() {
    onView(withId(R.id.buttonChoice)).perform(setLabel("null"));
  }

  @Test
  public void SetLabel03() {
    onView(withId(R.id.buttonChoice)).perform(setLabel("Available Options"));
  }

  @Test
  public void SetHint01() {
    onView(withId(R.id.buttonChoice)).perform(setHint(null));
  }

  @Test
  public void SetHint02() {
    onView(withId(R.id.buttonChoice)).perform(setHint("null"));
  }

  @Test
  public void SetHint03() {
    onView(withId(R.id.buttonChoice)).perform(setHint("Button Choice hint"));
  }

  @Test
  public void SetError01() {
    onView(withId(R.id.buttonChoice)).perform(setError(null));
  }

  @Test
  public void SetError02() {
    onView(withId(R.id.buttonChoice)).perform(setError("null"));
  }

  @Test
  public void SetError03() {
    onView(withId(R.id.buttonChoice)).perform(setError("Button Choice error"));
  }

  @Test
  public void OrientationChange() {
    onView(withId(R.id.buttonChoice)).perform(setPosition(0));
    onView(withId(R.id.buttonChoice)).check(matches(Value("0")));
    Utils.rotateScreen(this.activityTestRule_);
    InitButtonChoice();
    onView(withId(R.id.buttonChoice)).check(matches(Value("0")));
  }

  private ViewAction initView() {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        ButtonChoice buttonChoice = (ButtonChoice) view;
        ArrayList<Pair<String, String>> choiceArray = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
          choiceArray.add(new Pair<>(Integer.toString(i), "value " + i));
        }
        buttonChoice.InitOptionsArray(choiceArray);

      }

      @Override
      public String getDescription() {
        return "Set position for ButtonChoice";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(ButtonChoice.class);
      }
    };
  }

  public static Matcher<View> FieldId(final String expectedId) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof ButtonChoice)) {
          return false;
        }

        String id = ((ButtonChoice) view).GetFieldId();

        return expectedId.equals(id);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Get Button Choice Id");
      }
    };
  }

  public static Matcher<View> Value(final String expectedValue) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof ButtonChoice)) {
          return false;
        }

        String value = ((ButtonChoice) view).GetValues()[0];

        return expectedValue.equals(value);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Get key of the currently selected option");
      }
    };
  }

  public static Matcher<View> Validate(boolean expectedValidation) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof ButtonChoice)) {
          return false;
        }

        boolean validate = ((ButtonChoice) view).Validate();

        return validate;
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Get Validation Result");
      }
    };
  }

  private ViewAction setPosition(final int num) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        ButtonChoice buttonChoice = (ButtonChoice) view;
        buttonChoice.SetSelectedPosition(num);

      }

      @Override
      public String getDescription() {
        return "Set position for ButtonChoice";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(ButtonChoice.class);
      }
    };
  }

  private ViewAction setLabel(final String label) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        ButtonChoice buttonChoice = (ButtonChoice) view;
        buttonChoice.SetLabel(label);

      }

      @Override
      public String getDescription() {
        return "Set label for Button Choice";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(ButtonChoice.class);
      }
    };
  }

  private ViewAction setHint(final String hint) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        ButtonChoice buttonChoice = (ButtonChoice) view;
        buttonChoice.SetHint(hint);

      }

      @Override
      public String getDescription() {
        return "Set hint for Button Choice";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(ButtonChoice.class);
      }
    };
  }

  private ViewAction setError(final String error) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        ButtonChoice buttonChoice = (ButtonChoice) view;
        buttonChoice.SetError(error);

      }

      @Override
      public String getDescription() {
        return "Set error for Button Choice";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(ButtonChoice.class);
      }
    };
  }

}