package com.giift.formr.field;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.giift.formr.R;
import com.giift.formr.activity.MainActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
/**
 * @author vieony on 9/30/2016.
 */
@RunWith(AndroidJUnit4.class)
public class CardNumberTest {

  @Rule
  public ActivityTestRule<MainActivity> activityTestRule_ = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void ViewVisible() {
    onView(withId(R.id.cardNumber)).check(matches(isDisplayed()));
  }

  @Test
  public void SetLabel01() {
    onView(withId(R.id.cardNumber)).perform(setLabel(null));
  }

  @Test
  public void SetLabel02() {
    onView(withId(R.id.cardNumber)).perform(setLabel("null"));
  }

  @Test
  public void SetLabel03() {
    onView(withId(R.id.cardNumber)).perform(setLabel("Input Card Number"));
  }

  @Test
  public void SetHint01() {
    onView(withId(R.id.cardNumber)).perform(setHint(null));
  }

  @Test
  public void SetHint02() {
    onView(withId(R.id.cardNumber)).perform(setHint("null"));
  }

  @Test
  public void SetHint03() {
    onView(withId(R.id.cardNumber)).perform(setHint("Card Number hint"));
  }

  @Test
  public void SetError01() {
    onView(withId(R.id.cardNumber)).perform(setError(null));
  }

  @Test
  public void SetError02() {
    onView(withId(R.id.cardNumber)).perform(setError("null"));
  }

  @Test
  public void SetError03() {
    onView(withId(R.id.cardNumber)).perform(setError("Card Number error"));
  }


  private ViewAction setLabel(final String label) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        CardNumber cardNumber = (CardNumber) view;
        cardNumber.SetLabel(label);

      }

      @Override
      public String getDescription() {
        return "Set label for Card Number";

      }

      @Override
      public Matcher<View> getConstraints() {
        return isAssignableFrom(CardNumber.class);
      }
    };
  }

  private ViewAction setHint(final String hint) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        CardNumber cardNumber = (CardNumber) view;
        cardNumber.SetHint(hint);

      }

      @Override
      public String getDescription() {
        return "Set hint for Card Number";
      }

      @Override
      public Matcher<View> getConstraints() {
        return isAssignableFrom(CardNumber.class);
      }
    };
  }

  private ViewAction setError(final String error) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        CardNumber cardNumber = (CardNumber) view;
        cardNumber.SetError(error);

      }

      @Override
      public String getDescription() {
        return "Set error for Card Number";
      }

      @Override
      public Matcher<View> getConstraints() {
        return isAssignableFrom(CardNumber.class);
      }
    };
  }

}