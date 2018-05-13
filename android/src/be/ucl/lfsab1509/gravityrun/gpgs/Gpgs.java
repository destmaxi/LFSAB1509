package be.ucl.lfsab1509.gravityrun.gpgs;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import be.ucl.lfsab1509.gravityrun.tools.GpgsMappers;
import be.ucl.lfsab1509.gravityrun.tools.IGpgs;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class Gpgs extends GpgsMultiplayer implements IGpgs {

    public Gpgs(Activity context, ErrorCallback errorCallback) {
        super();
        Log.d(TAG, "Gpgs()");

        this.context = context;
        this.mGoogleSignInClient = GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        this.errorCallback = errorCallback;
    }

    public void setStartGameCallbask(StartGameCallback startGameCallbask) {
        this.startGameCallback = startGameCallbask;
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
                errorCallback.error(message);
            }
        } else if (requestCode == RC_SELECT_PLAYERS) {
            if (resultCode != Activity.RESULT_OK)
                return;

            final ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

            int minAutoPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
            int maxAutoPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);

            RoomConfig.Builder roomBuilder = RoomConfig.builder(mRoomUpdateCallback)
                    .setOnMessageReceivedListener(mMessageReceivedHandler)
                    .setRoomStatusUpdateCallback(mRoomStatusCallbackHandler)
                    .addPlayersToInvite(invitees);

            if (minAutoPlayers > 0)
                roomBuilder.setAutoMatchCriteria(RoomConfig.createAutoMatchCriteria(minAutoPlayers, maxAutoPlayers, 0));

            mJoinedRoomConfig = roomBuilder.build();
            mRealTimeMultiplayerClient.create(mJoinedRoomConfig);
        } else if (requestCode == RC_WAITING_ROOM) {
            // Look for finishing the waiting room from code, for example if a
            // "start game" message is received.  In this case, ignore the result.
            if (mWaitingRoomFinishedFromCode) {
                return;
            }

            if (resultCode == Activity.RESULT_OK) {
                // Start the game!
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Waiting room was dismissed with the back button. The meaning of this
                // action is up to the game. You may choose to leave the room and cancel the
                // match, or do something else like minimize the waiting room and
                // continue to connect in the background.

                // in this example, we take the simple approach and just leave the room:
                mRealTimeMultiplayerClient.leave(mJoinedRoomConfig, mRoom.getRoomId());
            } else if (resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                mRealTimeMultiplayerClient.leave(mJoinedRoomConfig, mRoom.getRoomId());
            }

            for (String opponent : mRoom.getParticipantIds())
                if (opponent != mMyParticipantId)
                    opponentId = opponent;
            startGameCallback.startGame();
        } else if (requestCode == RC_INVITATION_INBOX) {
            if (resultCode != Activity.RESULT_OK)
                return;

            Invitation invitation = data.getExtras().getParcelable(Multiplayer.EXTRA_INVITATION);
            if (invitation != null) {
                RoomConfig.Builder builder = RoomConfig.builder(mRoomUpdateCallback)
                        .setInvitationIdToAccept(invitation.getInvitationId());
                mJoinedRoomConfig = builder.build();
                mRealTimeMultiplayerClient.join(mJoinedRoomConfig);
            }
        }
    }

    private void onConnected(GoogleSignInAccount googleSignInAccount) {
        Log.d(TAG, "onConnected()");

        connected = true;
        mAchievementsClient = Games.getAchievementsClient(context, googleSignInAccount);
        mGamesClient = Games.getGamesClient(context, googleSignInAccount);
        mInvitationsClient = Games.getInvitationsClient(context, googleSignInAccount);
        mLeaderboardsClient = Games.getLeaderboardsClient(context, googleSignInAccount);
        mRealTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(context, googleSignInAccount);
        mSignedInAccount = googleSignInAccount;

        mPlayersClient = Games.getPlayersClient(context, googleSignInAccount);
        mPlayersClient.getCurrentPlayer()
                .addOnSuccessListener(new OnSuccessListener<Player>() {
                    @Override
                    public void onSuccess(Player player) {
                        mDisplayName = player.getDisplayName();
                        mPlayerId = player.getPlayerId();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                errorCallback.error("Erreur lors de la récupération des données de l'utilisateur.");
            }
        });
    }

    private void onDisconnected() {
        Log.d(TAG, "onDisconnected()");

        connected = false;
        mAchievementsClient = null;
        mDisplayName = null;
        mGamesClient = null;
        mInvitationsClient = null;
        mLeaderboardsClient = null;
        mPlayerId = null;
        mRealTimeMultiplayerClient = null;
        mSignedInAccount = null;
    }

    public void onPause() {
        Log.d(TAG, "onPause()");
    }

    public void onResume() {
        Log.d(TAG, "onResume()");

        signInSilently();
    }

    public void onStop() {
        mRealTimeMultiplayerClient.leave(mJoinedRoomConfig, mRoom.getRoomId());
    }

    /*
        Sign in methods
    */

    public boolean isSignedIn() {
//        Log.d(TAG, "isSignedIn()");

        return connected;
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
                errorCallback.error("Erreur lors de la tentative de connexion.");
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
                errorCallback.error("Erreur lors de la tentative de déconnexion.");
            }
        });
    }

    public void startSignInIntent() {
        Log.d(TAG, "startSignInIntent()");

        context.startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    /*
        Achievements and Leaderboards methods
    */

    public void incrementAchievement(String achievementId, int increment) {
        Log.d(TAG, "incrementAchievement() " + achievementId + " from " + increment);

        if (isSignedIn())
            mAchievementsClient.increment(GpgsMappers.mapToGpgsAchievement(achievementId), increment);
    }

    public void showAchievements() {
        Log.d(TAG, "showAchievements()");

        if (!isSignedIn()) {
            errorCallback.error("Vous n'êtes pas connecté.");
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
                errorCallback.error("Erreur lors de l'affichage des Réussites.");
            }
        });
    }

    public void showLeaderboards() {
        Log.d(TAG, "showLeaderboards()");

        if (!isSignedIn()) {
            errorCallback.error("Vous n'êtes pas connecté.");
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
                errorCallback.error("Erreur lors de l'affichage des Classements.");
            }
        });
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
