package com.evanslaton.health_tracker;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

// Tests the Image Carousel next and previous buttons
@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITestImageCarousel {
    String[] captions = {
            "Be the best version of yourself (1/3)",
            "Only 7 minutes a day (2/3)",
            "Two arms with amazing forearms (3/3)"
    };

    @Rule
    public ActivityTestRule<ImageCarousel> mActivityRule =
            new ActivityTestRule<>(ImageCarousel.class);

    // Tests the 'Next' button on the ImageCarousel page by cycling through the captions
    @Test
    public void testImageCarouselNext() {
        for (int i = 0; i <= 3; i++) {
            if (i == 3) {
                i = 0;
                onView(withId(R.id.imageCaption)).check(matches(withText(captions[i])));
                break;
            }
            onView(withId(R.id.imageCaption)).check(matches(withText(captions[i])));
            onView(withId(R.id.nextButton)).perform(click());
        }
    }

    // Tests the 'Previous' button on the ImageCarousel page by cycling through the captions
    @Test
    public void testImageCarouselPrev() {
        int exitLoop = 0;
        for (int i = 0; i >= -1; i--) {
            if (i == -1) {
                if (exitLoop > 0) {
                    break;
                }
                i = 2;
                exitLoop++;
            }
            onView(withId(R.id.imageCaption)).check(matches(withText(captions[i])));
            onView(withId(R.id.prevButton)).perform(click());
        }
    }
}
