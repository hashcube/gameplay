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

https://firebase.google.com/docs/android/setup

After plugins install completed (dekit install),  follow this  [url](https://firebase.google.com/docs/android/setup and obtain google-services.json) and obtain google-services.json file for your application using package "yourdomain.yourstudio.yourgamename", then put it into yourgamename/modules/gameplay/android/ directory


