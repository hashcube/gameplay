import util.setProperty as setProperty;

var onSyncUpdate;

function pluginSend(evt, params) {
	NATIVE && NATIVE.plugins && NATIVE.plugins.sendEvent &&
		NATIVE.plugins.sendEvent("GamePlayPlugin", evt,
				JSON.stringify(params || {}));
}

function pluginOn(evt, next) {
	NATIVE && NATIVE.events && NATIVE.events.registerHandler &&
		NATIVE.events.registerHandler(evt, next);
}

var Gameplay = Class(function () {
	this.init = function(opts) {
		logger.log("{gameplay} Registering for events on startup");
		setProperty(this, "onSyncUpdate", {
			set: function(f) {
				//logger.log("Am seting it");
				// If a callback is being set,
				if (typeof f === "function") {
					onSyncUpdate = f;
				} else {
					onSyncUpdate = null;
				}
			},
			get: function() {
				//logger.log("Am getting it");
				return onSyncUpdate;
			}
		});
	}

	this.sendAchievement = function(achievementID, percentSolved) {
		logger.log("{gameplay} Sending of achievement");

		var param = {"achievementID":achievementID,"percentSolved":percentSolved};

		pluginSend("sendAchievement",param);
	}

	this.sendScore = function(leaderBoardID, score) {
		logger.log("{gameplay} Sending of Score to leaderboard");

		var param = {"leaderBoardID":leaderBoardID,"score":score};

		pluginSend("sendScore",param);
	}

	this.setNumber = function(name, val) {
		return;
	}

	this.initSync = function(param_name) {
		return;
	}

	this.showLeaderBoard = function() {
		logger.log("{gameplay} Showing Leaderboard");

		pluginSend("showLeaderBoard");
	}
});

exports = new Gameplay();