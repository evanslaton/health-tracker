package com.evanslaton.health_tracker;

import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

// Tests the Stopwatch activity
@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITestStopwatch {
    @Rule
    public ActivityTestRule<Stopwatch> mActivityRule =
            new ActivityTestRule<>(Stopwatch.class);

    // Tests that the Stopwatch is displaying 0:00:00.000 and the buttons are clickable
    @Test
    public void testStopWatch() {
        onView(withId(R.id.timer)).check(matches(withText("0:00:00.000")));
        onView(withId(R.id.startStopButton)).check(matches(isClickable()));
        onView(withId(R.id.resetButton)).check(matches(isClickable()));
    }

    // https://stackoverflow.com/questions/23381459/how-to-get-text-from-textview-using-espresso
    // Gets the current value of the timer
    String getText(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }
}
