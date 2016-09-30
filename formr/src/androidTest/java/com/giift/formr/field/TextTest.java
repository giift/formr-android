package com.giift.formr.field;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.giift.formr.R;
import com.giift.formr.activity.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * @author vieony on 9/30/2016.
 */
@RunWith(AndroidJUnit4.class)
public class TextTest {
  @Rule
  public ActivityTestRule<MainActivity> activityTestRule_ = new ActivityTestRule<>(
      MainActivity.class);

  @Before
  public void InitButtonChoice() {
    onView(withId(R.id.buttonChoice)).perform(scrollTo());
  }
}