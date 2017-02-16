Google Play Game Services plugin for Gameclosure
=============

This is a Gameclosure(www.gameclosure.com) plugin for Google Play Game Services.
Right now we support only android.

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
