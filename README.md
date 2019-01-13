# Health-Tracker
## Lab 26: Android Fundamentals
* One key part of health is building finger strength and endurance. On the main page, display a number and a button. The number should increase when the button is clicked.
* Still on the main page, add a stopwatch. Have a button to [Start/Pause] and [Reset] the clock. The start/pause button should toggle between saying “start” and “pause.” And you should only be able to reset when the stopwatch is paused.
* At the top of our main page, we want to inspire our users with images of the type of person they can become. Display an image with a caption below it. (The caption should NOT be part of the image; it should be text.) Allow users to cycle through images and read the captions. Pressing next should go to the next image and its caption, pressing prev should go back. The app should display a (1/N), (2/N) … indicator so users know how many items are in the list

![Lab 26: Android Fundamentals](screenshots/screenshot.png)

## Lab 27: Intents, Notifications and XML
* Move your Finger Exercises and Stopwatch into their own pages of your app. Add buttons on the homepage to link to those pages, and ensure that the user can use the back button on the device to return to the app homepage.
* Create a new activity for Notifications. Allow users to set up reminders to drink water, which should appear every 2 hours in the notification bar. (For testing, you might want to shorten this to 15 or 30 seconds.)

![Lab 27: Intents, Notifications and XML](screenshots/screenshot2.png)

## Change Log
* 1/8/2019 - Added Lab 26 feature one (finger exerciser).
* 1/9/2019 - Added Lab 26 features two and three (stopwatch and image carousel).
* 1/10/2019 - Completed Lab 27:
    * Moved stopwatch and finger exercise to their own pages.
    * Added buttons to the homescreen so users access the new pages.
    * Created a button that will send a notification every 2 hours to the user reminding them to drink water (to test every 3 seconds uncomment line 57 and comment out line 58).

## Libraries / Third Party Sources
* Stopwatch code by Amit Kumar Singh - https://www.c-sharpcorner.com/article/creating-stop-watch-android-application-tutorial/
* Recurring notification code by https://stackoverflow.com/questions/9406523/android-want-app-to-perform-tasks-every-second