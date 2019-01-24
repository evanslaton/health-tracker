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
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITestFingerExercise {
    // Checks Finger Counter Exercise Button
    @Rule
    public ActivityTestRule<FingerExercise> mActivityRule =
            new ActivityTestRule<>(FingerExercise.class);

    @Test
    public void counterIncrementsOnButtonPressed() {
        for (int i = 0; i < 10; i++) {
            String counterValue = getText(withId(R.id.counter));
            String counterIncrementedByOne = String.valueOf(Integer.parseInt(counterValue) + 1);
            onView(withId(R.id.button)).perform(click());
            onView(withId(R.id.counter)).check(matches(withText(counterIncrementedByOne)));
        }
    }

    // https://stackoverflow.com/questions/23381459/how-to-get-text-from-textview-using-espresso
    // Gets the current value of the finger click counter (since it no longer starts at 0 every time)
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
