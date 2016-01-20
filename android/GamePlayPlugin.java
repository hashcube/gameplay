package com.tealeaf.plugin.plugins;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.games.Games;
import com.google.android.gms.common.api.GoogleApiClient;
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
  public static final int CLIENT_PLUS = GameHelper.CLIENT_PLUS;
  public static final int CLIENT_ALL = GameHelper.CLIENT_ALL;

  // Requested clients. By default, that's just the games client.
  protected int mRequestedClients = CLIENT_GAMES;

  // stores any additional scopes.
  private String[] mAdditionalScopes;

  protected String mDebugTag = "BaseGameActivity";
  protected boolean mDebugLog = false;
  public boolean logged_in = false;

  private GoogleApiClient mGoogleApiClient;
  public class GPStateEvent extends com.tealeaf.event.Event {
    String state;

    public GPStateEvent(String state) {
      super("gameplayLogin");
      this.state = state;
    }
  }

  public GamePlayPlugin() {
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
    if (mHelper == null) {
      mHelper = new GameHelper(_activity, CLIENT_GAMES);
      mHelper.setup(this);
      mGoogleApiClient = mHelper.getApiClient();
    }
  }

  public void onResume() {
  }

  public void onStart() {
    mHelper.onStart(_activity);
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

  protected boolean isSignedIn() {
    return mHelper.isSignedIn();
  }

  public void beginUserInitiatedSignIn(String dummyParam) {
    mHelper.beginUserInitiatedSignIn();
  }

  public void signOut(String dummyParam) {
    mHelper.signOut();
  }

  public void setLoginVariable(String param) {
    String connected_to = "";
    logger.log("{gameplay-native} value of connected_to", connected_to);

    final Bundle params = new Bundle();
    logged_in = false;

    try {
      JSONObject ldrData = new JSONObject(param);
      Iterator<?> keys = ldrData.keys();
      while( keys.hasNext() ){
        String key = (String)keys.next();
        Object o = ldrData.get(key);
        if(key.equals("connected_to")){
          connected_to = (String) o;
        }
      }
    } catch(JSONException e) {
      logger.log("{gameplay-native} Error in Params of setLoginVariable"
                 + " because "+ e.getMessage());
    }
    if(connected_to.equals("no")){
      logged_in = false;
      logger.log("{gameplay-native} logged_in : false");
    }
    else if(connected_to.equals("yes")){
      logged_in = true;
      logger.log("{gameplay-native} logged_in : true");
    }
  }

  protected void showAlert(String title, String message) {
    mHelper.makeSimpleDialog(title, message);
  }

  protected void showAlert(String message) {
    mHelper.makeSimpleDialog(message);
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

  protected void reconnectClient() {
    mHelper.reconnectClient();
  }

  protected boolean hasSignInError() {
    return mHelper.hasSignInError();
  }

  protected GameHelper.SignInFailureReason getSignInError() {
    return mHelper.getSignInError();
  }

  public void sendAchievement(String param) {
    if(logged_in) {
      mHelper.beginUserInitiatedSignIn();
    }

    if(!(mHelper.isSignedIn())) {
      logger.log("{gameplay-native} not signed in");
      return;
    }

    logger.log("{gameplay-native} Inside sendAchievement");
    final Bundle params = new Bundle();
    String achievementID = "";
    Float percentSolved = 0F;

    try {
      JSONObject ldrData = new JSONObject(param);
      Iterator<?> keys = ldrData.keys();
      while( keys.hasNext() ){
        String key = (String)keys.next();
        Object o = ldrData.get(key);
        if(key.equals("achievementID")) {
          achievementID = (String) o;
          continue;
        }
        if(key.equals("percentSolved")) {
          percentSolved = new Float(o.toString());
          continue;
        }
        params.putString(key, (String) o);
      }
      if (percentSolved == 100F) {
          Games.Achievements.unlock(mGoogleApiClient, achievementID);
      }
      /*** else {
        We should implement incremental achievements here
        Games.Achievements.increment(mGoogleApiClient, "my_incremental_achievment_id", 1);
      } ***/

    } catch(JSONException e) {
      logger.log("{gameplay-native} Error in Params of sendAchievement because "+ e.getMessage());
    }
  }

  public void showLeaderBoard(String dummyParam)
  {
    if(logged_in) {
      mHelper.beginUserInitiatedSignIn();
    }

    if(!(mHelper.isSignedIn())){
      logger.log("{gameplay-native} not signed in");
      return;
    }
    //TODO: getlLeaderboardsIndent accepts id as parameter to show
    //a specific leaderboard.
    _activity.startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient), 1);
  }

  public void showAchievements(String dummyParam)
  {
    if(logged_in) {
      mHelper.beginUserInitiatedSignIn();
    }

    if(!(mHelper.isSignedIn())){
      logger.log("{gameplay-native} not signed in");
      return;
    }
    _activity.startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), 1);
  }

  public void sendScore(String param)
  {
    if(logged_in) {
      mHelper.beginUserInitiatedSignIn();
    }

    if(!(mHelper.isSignedIn())) {
      logger.log("{gameplay-native} not signed in");
      return;
    }

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
      Games.Leaderboards.submitScore(mGoogleApiClient, leaderBoardID, score);
    } catch(JSONException e) {
      logger.log("{gameplay-native} Error in Params of sendScore because "+ e.getMessage());
    }
  }

  public void logError(String errorDesc) {
    logger.log("{gameplay-native} logError "+ errorDesc);
  }
}
