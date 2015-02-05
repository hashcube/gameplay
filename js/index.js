'use strict';
/* global Class, exports:true, NATIVE, setProperty, logger */

/* jshint ignore:start */
import util.setProperty as setProperty;
/* jshint ignore:end */

function pluginSend(evt, params) {
  NATIVE.plugins.sendEvent('GamePlayPlugin', evt, JSON.stringify(params || {}));
}

function pluginOn(evt, next) {
  NATIVE.events.registerHandler(evt, next);
}

function invokeCallbacks(list) {
  // Pop off the first two arguments and keep the rest
  var args = Array.prototype.splice.call(arguments, 1),
    length = list.length,
    i, item;

  // For each callback,
  for (i = 0; i < length; i++) {
    item = list[i];

    // If callback was actually specified,
    if (item) {
      // Run it
      item.apply(null, args);
    }
  }

  list.length = 0;
}

exports = new (Class(function () {
  var loginCB = [],
    onSyncUpdate;

  this.init = function () {
    logger.log('{gameplay} Registering for events on startup');
    setProperty(this, 'onSyncUpdate', {
      set: function (f) {
        // If a callback is being set,
        if (typeof f === 'function') {
          onSyncUpdate = f;
        } else {
          onSyncUpdate = null;
        }
      },
      get: function () {
        return onSyncUpdate;
      }
    });

    pluginOn('gameplayLogin', function (evt) {
      logger.log('{gameplay} State updated:', evt.state);

      invokeCallbacks(loginCB, evt.state === 'open', evt);
    });
  };

  this.sendAchievement = function (achievementID, percentSolved) {
    var param = {
      'achievementID': achievementID,
      'percentSolved': percentSolved
    };

    logger.log('{gameplay} Sending of achievement');
    pluginSend('sendAchievement', param);
  };

  this.sendScore = function (leaderBoardID, score) {
    var param = {
      'leaderBoardID': leaderBoardID,
      'score': score
    };

    logger.log('{gameplay} Sending of Score to leaderboard');
    pluginSend('sendScore', param);
  };

  this.setNumber = function () {
    return;
  };

  this.initSync = function () {
    return;
  };

  this.logout = function () {
    logger.log('{gameplay} Logging Out a user');
    pluginSend('signOut');
  };

  this.login = function (next) {
    logger.log('{gameplay} Logging in a user');
    loginCB.push(next);
    pluginSend('beginUserInitiatedSignIn');
  };

  this.showLeaderBoard = function () {
    logger.log('{gameplay} Showing Leaderboard');
    pluginSend('showLeaderBoard');
  };

  this.showAchievements = function () {
    logger.log('{gameplay} Showing Achievements');
    pluginSend('showAchievements');
  };
}))();
