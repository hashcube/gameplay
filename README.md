Google Play Game Services plugin for Gameclosure
=============

This is a Gameclosure(www.gameclosure.com) plugin for Google Play Game Services.
Right now we support only android.

Android version: 11.8.0

Features
  * Login/Logout
  * PlayerStats
  * Leaderboard
  * Achievements

How to Install
-------------
clone this repo to '''addons''' folder inside devkit and do following
```
$ cd gameplay
$ android update project -p android/google_play_services_lib/
```

To register playerStats callback in game:
```
  import gameplay as gameplay;
  gameplay.onPlayerStats = callbackFn;
```

Note: basement lib we are using in utils module also. if you are using utils module, if we updating basement lib, update utils also with the same

## Android installation
Requires google play services configuration to be registered on game project package


Now for every game, after devkit init game, you can check the package name of your android project in devkit/YOURGAME/modules/devkit-core/modules/native-android/gradleops/YOURGAME/app/build.gradle file, line 7, applicationId variable

then to FCM console and create the project for the package of the game you have created and obtain google-services.json file right after FCM project creation in console.


https://firebase.google.com/docs/android/setup

Then put google-services.json file into devkit/YOURGAME/modules/gameplay/android/

