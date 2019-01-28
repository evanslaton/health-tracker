package com.evanslaton.health_tracker;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

// Tests the Profile activity
@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITestProfile {
    @Rule
    public ActivityTestRule<Profile> mActivityRule =
            new ActivityTestRule<>(Profile.class);

    // Tests that the buttons on the profile activity are clickable and an image is displayed
    @Test
    public void testProfileButtons() {
        onView(withId(R.id.takePicture)).check(matches(isClickable()));
        onView(withId(R.id.choosePic)).check(matches(isClickable()));
        onView(withId(R.id.profilePicture)).check(matches(isDisplayed()));
    }

}
