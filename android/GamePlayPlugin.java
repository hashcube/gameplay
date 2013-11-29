package com.tealeaf.plugin.plugins;

import com.google.android.gms.appstate.AppStateClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.GamesClient.*;
import com.google.android.gms.plus.PlusClient;
import com.google.example.games.basegameutils.*;

import com.tealeaf.plugin.IPlugin;
import com.tealeaf.logger;
import com.tealeaf.EventQueue;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.view.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.EnumSet;
import java.util.Iterator;
import java.lang.Long;
import java.lang.Float;

import android.R.id.*;


public class GamePlayPlugin implements IPlugin, GameHelper.GameHelperListener {
	Context _context;
	Activity _activity;

    // The game helper object. This class is mainly a wrapper around this object.
    protected GameHelper mHelper;

    // We expose these constants here because we don't want users of this class
    // to have to know about GameHelper at all.
    public static final int CLIENT_GAMES = GameHelper.CLIENT_GAMES;
    public static final int CLIENT_APPSTATE = GameHelper.CLIENT_APPSTATE;
    public static final int CLIENT_PLUS = GameHelper.CLIENT_PLUS;
    public static final int CLIENT_ALL = GameHelper.CLIENT_ALL;

    // Requested clients. By default, that's just the games client.
    protected int mRequestedClients = CLIENT_GAMES;

    // stores any additional scopes.
    private String[] mAdditionalScopes;

    protected String mDebugTag = "BaseGameActivity";
    protected boolean mDebugLog = false;

	public class GPStateEvent extends com.tealeaf.event.Event {
		String state;

		public GPStateEvent(String state) {
			super("gameplayLogin");
			this.state = state;
		}
	}

    /** Constructs a BaseGameActivity with default client (GamesClient). */
    public GamePlayPlugin() {
        mHelper = new GameHelper(_activity);
    }

    /**
     * Constructs a BaseGameActivity with the requested clients.
     * @param requestedClients The requested clients (a combination of CLIENT_GAMES,
     *         CLIENT_PLUS and CLIENT_APPSTATE).
     */
    public GamePlayPlugin(int requestedClients) {
        setRequestedClients(requestedClients);
    }

    /**
     * Sets the requested clients. The preferred way to set the requested clients is
     * via the constructor, but this method is available if for some reason your code
     * cannot do this in the constructor. This must be called before onCreate in order to
     * have any effect. If called after onCreate, this method is a no-op.
     *
     * @param requestedClients A combination of the flags CLIENT_GAMES, CLIENT_PLUS
     *         and CLIENT_APPSTATE, or CLIENT_ALL to request all available clients.
     * @param additionalScopes.  Scopes that should also be requested when the auth
     *         request is made.
     */
    protected void setRequestedClients(int requestedClients, String ... additionalScopes) {
        mRequestedClients = requestedClients;
        mAdditionalScopes = additionalScopes;
    }

	public void onCreateApplication(Context applicationContext) {
		_context = applicationContext;
	}

	public void onCreate(Activity activity, Bundle savedInstanceState) {
		_activity = activity;
        mHelper = new GameHelper(activity);
        if (mDebugLog) {
            mHelper.enableDebugLog(mDebugLog, mDebugTag);
        }
        mHelper.setup(this, mRequestedClients, mAdditionalScopes);
	}

	public void onResume() {
	}

	public void onStart() {
	}

	public void onPause() {
	}

	@Override
	public void onSignInFailed(){
		// Sign in has failed. So show the user the sign-in button.
		EventQueue.pushEvent(new GPStateEvent("close"));
	}

	@Override
	public void onSignInSucceeded(){
		// show sign-out button, hide the sign-in button
	    EventQueue.pushEvent(new GPStateEvent("open"));
	}

	public void onStop() {
		mHelper.onStop();
	}

	public void onDestroy() {
	}

	public void onNewIntent(Intent intent) {
	}

	public void setInstallReferrer(String referrer) {
	}

	public void onActivityResult(Integer request, Integer result, Intent data) {
		mHelper.onActivityResult(request, result, data);
	}

	public boolean consumeOnBackPressed() {
		return true;
	}

	public void onBackPressed() {
	}

    protected GamesClient getGamesClient() {
        return mHelper.getGamesClient();
    }

    protected AppStateClient getAppStateClient() {
        return mHelper.getAppStateClient();
    }

    protected PlusClient getPlusClient() {
        return mHelper.getPlusClient();
    }

    protected boolean isSignedIn() {
        return mHelper.isSignedIn();
    }

    public void beginUserInitiatedSignIn(String dummyParam) {
        mHelper.beginUserInitiatedSignIn(_context);
    }

    public void signOut(String dummyParam) {
        mHelper.signOut();
    }

    protected void showAlert(String title, String message) {
        mHelper.showAlert(title, message);
    }

    protected void showAlert(String message) {
        mHelper.showAlert(message);
    }

    protected void enableDebugLog(boolean enabled, String tag) {
        mDebugLog = true;
        mDebugTag = tag;
        if (mHelper != null) {
            mHelper.enableDebugLog(enabled, tag);
        }
    }

    protected String getInvitationId() {
        return mHelper.getInvitationId();
    }

    protected void reconnectClients(int whichClients) {
        mHelper.reconnectClients(whichClients);
    }

    protected String getScopes() {
        return mHelper.getScopes();
    }

    protected String[] getScopesArray() {
        return mHelper.getScopesArray();
    }

    protected boolean hasSignInError() {
        return mHelper.hasSignInError();
    }

    protected GameHelper.SignInFailureReason getSignInError() {
        return mHelper.getSignInError();
    }

	public void sendAchievement(String param)
	{
		logger.log("{gameplay-native} Inside sendAchievement");
		if(!(mHelper.isSignedIn())){
			return;
		}
	    final Bundle params = new Bundle();
	    //logger.log(1);
	    String achievementID = "";
	    //logger.log(2);
	    Float percentSolved = 0F;
	    //logger.log(3);
	    try {
	    	JSONObject ldrData = new JSONObject(param);	
	    	//logger.log(4);
	        Iterator<?> keys = ldrData.keys();
	        //logger.log(5);
	        while( keys.hasNext() ){
	        	//logger.log(6);
	            String key = (String)keys.next();
	            //logger.log(7);
	    		Object o = ldrData.get(key);
	    		//logger.log(8);
	    		if(key.equals("achievementID")){
	    			//logger.log(9);
	    			achievementID = (String) o;
	    			continue;
	    		}
	    		if(key.equals("percentSolved")){
	    			//logger.log(10);
	    			percentSolved = new Float(o.toString());
	    			continue;
	    		}
	    		//logger.log(11);
	    		params.putString(key, (String) o);
	        }
		} catch(JSONException e) {
			logger.log("{gameplay-native} Error in Params of sendAchievement because "+ e.getMessage());
		}
		//logger.log(12);
		//logger.log(13);
		//logger.log(achievementID);
		//logger.log(percentSolved);
		//logger.log("============");
		mHelper.mGamesClient.unlockAchievement(achievementID);
	}

	public void showLeaderBoard(String dummyParam)
	{
		if(!(mHelper.isSignedIn())){
			return;
		}
		logger.log("{gameplay-native} Inside showLeaderBoard");
	}

	public void sendScore(String param)
	{
		if(!(mHelper.isSignedIn())){
			return;
		}
		logger.log("{gameplay-native} Inside sendScore");
	    final Bundle params = new Bundle();
	    String leaderBoardID = "";
	    Long score = 0L;
	    try {
	    	JSONObject ldrData = new JSONObject(param);	
	        Iterator<?> keys = ldrData.keys();
	        while( keys.hasNext() ){
	            String key = (String)keys.next();
	    		Object o = ldrData.get(key);
	    		if(key.equals("leaderBoardID")){
	    			leaderBoardID = (String) o;
	    			continue;
	    		}
	    		if(key.equals("score")){
	    			score =  Long.parseLong(o.toString());
	    			continue;
	    		}
	    		params.putString(key, (String) o);
	        }
		} catch(JSONException e) {
			logger.log("{gameplay-native} Error in Params of sendScore because "+ e.getMessage());
		}
		//logger.log("Sending score");
		//logger.log("=================");
		mHelper.mGamesClient.submitScore(leaderBoardID, score);
		//logger.log(score);
		//_activity.startActivityForResult(mHelper.mGamesClient.getLeaderboardIntent(leaderBoardID), 777);
	}

	public void logError(String errorDesc) {
		logger.log("{gameplay-native} logError "+ errorDesc);
	}
}