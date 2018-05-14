package be.ucl.lfsab1509.gravityrun.gpgs;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import be.ucl.lfsab1509.gravityrun.tools.GpgsMappers;
import be.ucl.lfsab1509.gravityrun.tools.IGpgs;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class Gpgs implements IGpgs {

    private static final int RC_SIGN_IN = 9001;
    private static final int RC_ACHIEVEMENT_UI = 9003;
    private static final int RC_LEADERBOARD_UI = 9004;
    private static final String TAG = "Gpgs";

    private AchievementsClient mAchievementsClient = null;
    private Activity context;
    private boolean connected = false;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount mSignedInAccount = null;
    private LeaderboardsClient mLeaderboardsClient = null;

    public Gpgs(Activity context) {
        Log.d(TAG, "Gpgs()");

        this.context = context;
        this.mGoogleSignInClient = GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
    }

    public void incrementAchievement(String achievementId, int increment) {
        Log.d(TAG, "incrementAchievement() " + achievementId + " from " + increment);

        if (isSignedIn())
            mAchievementsClient.increment(GpgsMappers.mapToGpgsAchievement(achievementId), increment);
    }

    public boolean isSignedIn() {
//        Log.d(TAG, "isSignedIn()");

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
                // toast
            }
        }
    }

    private void onConnected(GoogleSignInAccount googleSignInAccount) {
        Log.d(TAG, "onConnected()");

        connected = true;
        mAchievementsClient = Games.getAchievementsClient(context, googleSignInAccount);
        mLeaderboardsClient = Games.getLeaderboardsClient(context, googleSignInAccount);
        mSignedInAccount = googleSignInAccount;
    }

    private void onDisconnected() {
        Log.d(TAG, "onDisconnected()");

        connected = false;
        mAchievementsClient = null;
        mLeaderboardsClient = null;
        mSignedInAccount = null;
    }

    public void onPause() {
        Log.d(TAG, "onPause()");
    }

    public void onResume() {
        Log.d(TAG, "onResume()");

        signInSilently();
    }

    public void showAchievements() {
        Log.d(TAG, "showAchievements()");

        if (!isSignedIn()) {
//            errorCallback.error("Vous n'êtes pas connecté.");
            return;
        }

        mAchievementsClient.getAchievementsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        context.startActivityForResult(intent, RC_ACHIEVEMENT_UI);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                errorCallback.error("Erreur lors de l'affichage des Réussites.");
            }
        });
    }

    public void showLeaderboards() {
        Log.d(TAG, "showLeaderboards()");

        if (!isSignedIn()) {
//            errorCallback.error("Vous n'êtes pas connecté.");
            return;
        }

        mLeaderboardsClient.getAllLeaderboardsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        context.startActivityForResult(intent, RC_LEADERBOARD_UI);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                errorCallback.error("Erreur lors de l'affichage des Classements.");
            }
        });
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
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                errorCallback.error("Erreur lors de la tentative de connexion.");
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
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                errorCallback.error("Erreur lors de la tentative de déconnexion.");
            }
        });
    }

    public void startSignInIntent() {
        Log.d(TAG, "startSignInIntent()");

        context.startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    public void submitScore(String leaderboardId, int score) {
        Log.d(TAG, "submitScore() " + score + " on " + leaderboardId);

        if (isSignedIn())
            mLeaderboardsClient.submitScore(GpgsMappers.mapToGpgsLeaderBoard(leaderboardId), score);
    }

    public void unlockAchievement(String achievementId) {
        Log.d(TAG, "unlockAchiement() " + achievementId);

        if (isSignedIn())
            mAchievementsClient.unlock(GpgsMappers.mapToGpgsAchievement(achievementId));
    }

}
