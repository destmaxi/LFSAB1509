package be.ucl.lfsab1509.gravityrun.gpgs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import be.ucl.lfsab1509.gravityrun.tools.GpgsMappers;
import be.ucl.lfsab1509.gravityrun.tools.IGpgs;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class Gpgs implements IGpgs {

    private static final int RC_SIGN_IN = 9001;
    private static final int RC_ACHIEVEMENT_UI = 9003;
    private static final int RC_LEADERBOARD_UI = 9004;
    private static final String TAG = "Gpgs";

    private Activity context;
    private boolean connected = false;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount mSignedInAccount = null;
    private RealTimeMultiplayerClient mRealTimeMultiplayerClient = null;
    private String mDisplayName = null, mPlayerId = null;

    public Gpgs(Activity context) {
        Log.d(TAG, "Gpgs()");

        this.context = context;
        mGoogleSignInClient = GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        signInSilently();
    }

    public void incrementAchievement(String achievementId, int increment) {
        Log.d(TAG, "incrementAchievement() " + achievementId + " from " + increment);

        if (isSignedIn())
            Games.getAchievementsClient(context, mSignedInAccount)
                    .increment(GpgsMappers.mapToGpgsAchievement(achievementId), increment);
    }

    public boolean isSignedIn() {
        Log.d(TAG, "isSignedIn()");

        return connected;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount signedInAccount = result.getSignInAccount();
                onConnected(signedInAccount);
            } else {
                String message = result.getStatus().getStatusMessage();
                if (message == null || message.isEmpty()) {
                    message = "Erreur de connexion";
                }
                new AlertDialog.Builder(context)
                        .setMessage(message)
                        .setNeutralButton(android.R.string.ok, null).show();    // TODO replace this message
            }
        }
    }

    private void onConnected(GoogleSignInAccount googleSignInAccount) {
        Log.d(TAG, "onConnected()");

        connected = true;
        mRealTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(context, googleSignInAccount);
        mSignedInAccount = googleSignInAccount;

        PlayersClient playersClient = Games.getPlayersClient(context, googleSignInAccount);
        playersClient.getCurrentPlayer()
                .addOnSuccessListener(new OnSuccessListener<Player>() {
                    @Override
                    public void onSuccess(Player player) {
                        mDisplayName = player.getDisplayName();
                        mPlayerId = player.getPlayerId();
                    }
                }); // TODO add onFailureListener
    }

    private void onDisconnected() {
        Log.d(TAG, "onDisconnected()");

        connected = false;
        mDisplayName = null;
        mPlayerId = null;
        mSignedInAccount = null;
        mRealTimeMultiplayerClient = null;
    }

    public void onPause() {
        Log.d(TAG, "onPause()");
    }

    public void onResume() {
        Log.d(TAG, "onResume()");

        signInSilently();
    }

    public void signIn() {
        Log.d(TAG, "signIn()");

        Intent intent = mGoogleSignInClient.getSignInIntent();
        context.startActivityForResult(intent, RC_SIGN_IN);
    }

    public void signInSilently() {
        Log.d(TAG, "signInSilently()");

        mGoogleSignInClient.silentSignIn()
                .addOnCompleteListener(context, new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInSilently(): success");
                            mSignedInAccount = task.getResult();
                            onConnected(mSignedInAccount);
                        } else {
                            Log.d(TAG, "signInSilently(): failure", task.getException());
                            onDisconnected();
                        }
                    }
                });
    }

    public void signOut() {
        Log.d(TAG, "signOut()");

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Log.d(TAG, "signOut(): success");
                        else
                            Log.d(TAG, "signOut(): failure");
                        onDisconnected();
                    }
                });
    }

    public boolean showAchievements() {
        Log.d(TAG, "showAchievements()");

        if (!isSignedIn())
            return false;

        Games.getAchievementsClient(context, mSignedInAccount)
                .getAchievementsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        context.startActivityForResult(intent, RC_ACHIEVEMENT_UI);
                    }
                });
        return true;
    }

    public boolean showLeaderboards() {
        Log.d(TAG, "showLeaderboards()");

        if (!isSignedIn())
            return false;

        Games.getLeaderboardsClient(context, mSignedInAccount)
                .getAllLeaderboardsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        context.startActivityForResult(intent, RC_LEADERBOARD_UI);
                    }
                });
        return true;
    }

    public void submitScore(String leaderboardId, int score) {
        Log.d(TAG, "submitScore() " + score + " on " + leaderboardId);

        if (isSignedIn())
            Games.getLeaderboardsClient(context, mSignedInAccount)
                    .submitScore(GpgsMappers.mapToGpgsLeaderBoard(leaderboardId), score);
    }

    public void unlockAchievement(String achievementId) {
        Log.d(TAG, "unlockAchiement() " + achievementId);

        if (isSignedIn())
            Games.getAchievementsClient(context, mSignedInAccount)
                    .unlock(GpgsMappers.mapToGpgsAchievement(achievementId));
    }

}
