package be.ucl.lfsab1509.gravityrun.gpgs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import be.ucl.lfsab1509.gravityrun.tools.GpgsMappers;
import be.ucl.lfsab1509.gravityrun.tools.IMyGpgsClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MyGpgsClient implements IMyGpgsClient {

    private static final int RC_SIGN_IN = 9001;
    private static final int RC_ACHIEVEMENT_UI = 9003;
    private static final int RC_LEADERBOARD_UI = 9004;
    private static final String TAG = "MyGpgsClient";

    private Activity context;
    private boolean connected = false;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount mSignedInAccount = null;
    private RealTimeMultiplayerClient mRealTimeMultiplayerClient = null;
    private String mDisplayName = null, mPlayerId = null;

    public MyGpgsClient(Activity context) {
        Log.d(TAG, "MyGpgsClient()");
        this.context = context;
        mGoogleSignInClient = GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        signInSilently();
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isSessionActive() {
        Log.d(TAG, "isSignedIn()");
        return GoogleSignIn.getLastSignedInAccount(context) != null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {    // TODO ???
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
                new AlertDialog.Builder(context).setMessage(message)
                        .setNeutralButton(android.R.string.ok, null).show();
            }
        }
    }

    public void onConnected(GoogleSignInAccount googleSignInAccount) {
        Log.d(TAG, "onConnected()");
        connected = true;
        mRealTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(context, googleSignInAccount);
        mSignedInAccount = googleSignInAccount;

        PlayersClient playersClient = Games.getPlayersClient(context, googleSignInAccount);
        playersClient.getCurrentPlayer().addOnSuccessListener(new OnSuccessListener<Player>() {
            @Override
            public void onSuccess(Player player) {
                mDisplayName = player.getDisplayName();
                mPlayerId = player.getPlayerId();
            }
        }); // TODO add onFailureListener
    }

    public void onDisconnected() {
        Log.d(TAG, "onDisconnected()");
        connected = false;
        mDisplayName = null;
        mPlayerId = null;
        mSignedInAccount = null;
        mRealTimeMultiplayerClient = null;
    }

    public void onStop() {

    }

    public void onPause() {

    }

    public void onResume() {
        signInSilently();
    }

    public void signInSilently() {
        Log.d(TAG, "signInSilently()");
        mGoogleSignInClient.silentSignIn().addOnCompleteListener(context, new OnCompleteListener<GoogleSignInAccount>() {
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
        mGoogleSignInClient.signOut().addOnCompleteListener(context, new OnCompleteListener<Void>() {
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
        if (!isConnected())
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
        if (!isConnected())
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

    public void startSignInIntent() {
        Log.d(TAG, "startSignInIntent()");
        Intent intent = mGoogleSignInClient.getSignInIntent();
        context.startActivityForResult(intent, RC_SIGN_IN);
    }

    public void submitScore(String leaderboardId, int score) {
        if (isConnected())
            Games.getLeaderboardsClient(context, mSignedInAccount)
                    .submitScore(GpgsMappers.mapToGpgsLeaderBoard(leaderboardId), score);
    }

}
