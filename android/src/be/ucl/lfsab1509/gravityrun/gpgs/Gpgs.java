package be.ucl.lfsab1509.gravityrun.gpgs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import be.ucl.lfsab1509.gravityrun.tools.GpgsMappers;
import be.ucl.lfsab1509.gravityrun.tools.IGpgs;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.games.*;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.InvitationCallback;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Gpgs implements IGpgs {

    private final static int MIN_PLAYERS = 2;
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_ACHIEVEMENT_UI = 9003;
    private static final int RC_LEADERBOARD_UI = 9004;
    private static final int RC_SELECT_PLAYERS = 9006;
    private static final int RC_WAITING_ROOM = 9007;
    private static final int RC_INVITATION_INBOX = 9008;
    private static final String TAG = "Gpgs";

    private Activity context;
    private boolean connected = false, mPlaying = false, mWaitingRoomFinishedFromCode = false;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount mSignedInAccount = null;
    private HashSet<Integer> pendingMessageSet = new HashSet<>();
    private PlayersClient mPlayersClient = null;
    private RealTimeMultiplayerClient mRealTimeMultiplayerClient = null;
    private Room mRoom;
    private RoomConfig mJoinedRoomConfig;
    private Set<String> mFinishedRacers;
    private String mDisplayName = null, mMyParticipantId = null, mPlayerId = null;

    public Gpgs(Activity context) {
        Log.d(TAG, "Gpgs()");

        this.context = context;
        mGoogleSignInClient = GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        signInSilently();
    }

//    Sign in methods

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

    public void onStop() {
        mRealTimeMultiplayerClient.leave(mJoinedRoomConfig, mRoom.getRoomId());
    }

    void sendToAllReliably(byte[] message) {
        for (String participantId : mRoom.getParticipantIds()) {
            if (!participantId.equals(mMyParticipantId)) {
                Task<Integer> task = mRealTimeMultiplayerClient.sendReliableMessage(message, mRoom.getRoomId(), participantId, handleMessageSentCallback)
                        .addOnCompleteListener(new OnCompleteListener<Integer>() {
                            @Override
                            public void onComplete(@NonNull Task<Integer> task) {
                                // Keep track of which messages are sent, if desired.
                                recordMessageToken(task.getResult());
                            }
                        });
            }
        }
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

    public void startSignInIntent() {
        Log.d(TAG, "startSignInIntent()");

        context.startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

//    Achievements and Leaderboards methods

    public void incrementAchievement(String achievementId, int increment) {
        Log.d(TAG, "incrementAchievement() " + achievementId + " from " + increment);

        if (isSignedIn())
            Games.getAchievementsClient(context, mSignedInAccount)
                    .increment(GpgsMappers.mapToGpgsAchievement(achievementId), increment);
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

//    Multiplayer methods

    private void checkForInvitation() {
        Games.getGamesClient(context, mSignedInAccount)
                .getActivationHint()
                .addOnSuccessListener(new OnSuccessListener<Bundle>() {
                                          @Override
                                          public void onSuccess(Bundle bundle) {
                                              Invitation invitation = bundle.getParcelable(Multiplayer.EXTRA_INVITATION);
                                              if (invitation != null) {
                                                  RoomConfig.Builder builder = RoomConfig.builder(mRoomUpdateCallback)
                                                          .setInvitationIdToAccept(invitation.getInvitationId());
                                                  mJoinedRoomConfig = builder.build();
                                                  mRealTimeMultiplayerClient.join(mJoinedRoomConfig);
                                              }
                                          }
                                      }
                );
    }

    boolean haveAllRacersFinished(Room room) {
        for (Participant p : room.getParticipants()) {
            String pid = p.getParticipantId();
            if (p.isConnectedToRoom() && !mFinishedRacers.contains(pid)) {
                // at least one racer is connected but hasn't finished
                return false;
            }
        }
        // all racers who are connected have finished the race
        return true;
    }

    private void invitePlayers() {
        mRealTimeMultiplayerClient.getSelectOpponentsIntent(1, 1, true)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        context.startActivityForResult(intent, RC_SELECT_PLAYERS);
                    }
                });
    }

    private void onStartGameMessageReceived() {
        mWaitingRoomFinishedFromCode = true;
        context.finishActivity(RC_WAITING_ROOM);
    }

    // Returns whether the room is in a state where the game should be canceled.
    boolean shouldCancelGame(Room room) {
        // TODO: Your game-specific cancellation logic here. For example, you might decide to
        // cancel the game if enough people have declined the invitation or left the room.
        // You can check a participant's status with Participant.getStatus().
        // (Also, your UI should have a Cancel button that cancels the game too)
        return false;
    }

    // returns whether there are enough players to start the game
    boolean shouldStartGame(Room room) {
        int connectedPlayers = 0;
        for (Participant p : room.getParticipants()) {
            if (p.isConnectedToRoom()) {
                ++connectedPlayers;
            }
        }
        return connectedPlayers >= MIN_PLAYERS;
    }

    private void showInvitationInbox() {
        Games.getInvitationsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .getInvitationInboxIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        context.startActivityForResult(intent, RC_INVITATION_INBOX);
                    }
                });
    }

    private void showWaitingRoom(Room room, int maxPlayersToStartGame) {
        mRealTimeMultiplayerClient.getWaitingRoomIntent(room, maxPlayersToStartGame)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        context.startActivityForResult(intent, RC_WAITING_ROOM);
                    }
                });
    }

    private void startQuickGame(long role) {
        // auto-match criteria to invite one random automatch opponent.
        // You can also specify more opponents (up to 3).
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(1, 1, role);

        RoomConfig roomConfig = RoomConfig.builder(mRoomUpdateCallback)
                .setOnMessageReceivedListener(mMessageReceivedHandler)
                .setRoomStatusUpdateCallback(mRoomStatusCallbackHandler)
                .setAutoMatchCriteria(autoMatchCriteria)
                .build();

        // Save the roomConfig so we can use it if we call leave().
        mJoinedRoomConfig = roomConfig;

        // create room:
        mRealTimeMultiplayerClient.create(roomConfig);
    }

    private synchronized void recordMessageToken(int tokenId) {
        pendingMessageSet.add(tokenId);
    }

    private InvitationCallback mInvitationCallbackHandler = new InvitationCallback() {

        @Override
        public void onInvitationReceived(@NonNull Invitation invitation) {
            RoomConfig.Builder builder = RoomConfig.builder(mRoomUpdateCallback)
                    .setInvitationIdToAccept(invitation.getInvitationId());
            mJoinedRoomConfig = builder.build();
            mRealTimeMultiplayerClient.join(mJoinedRoomConfig);
        }

        @Override
        public void onInvitationRemoved(@NonNull String invitationId) {
            // Invitation removed.
        }

    };

    private OnRealTimeMessageReceivedListener mMessageReceivedHandler = new OnRealTimeMessageReceivedListener() {
        @Override
        public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
            // Handle messages received here.
            byte[] message = realTimeMessage.getMessageData();
            // process message contents...
        }
    };

    private RealTimeMultiplayerClient.ReliableMessageSentCallback handleMessageSentCallback = new RealTimeMultiplayerClient.ReliableMessageSentCallback() {
        @Override
        public void onRealTimeMessageSent(int statusCode, int tokenId, String recipientId) {
            // handle the message being sent.
            synchronized (this) {
                pendingMessageSet.remove(tokenId);
            }
        }
    };

    private RoomStatusUpdateCallback mRoomStatusCallbackHandler = new RoomStatusUpdateCallback() {

        @Override
        public void onRoomConnecting(@Nullable Room room) {
            // Update the UI status since we are in the process of connecting to a specific room.
        }

        @Override
        public void onRoomAutoMatching(@Nullable Room room) {
            // Update the UI status since we are in the process of matching other players.
        }

        @Override
        public void onPeerInvitedToRoom(@Nullable Room room, @NonNull List<String> list) {
            // Update the UI status since we are in the process of matching other players.
        }

        @Override
        public void onPeerDeclined(@Nullable Room room, @NonNull List<String> list) {
            // Peer declined invitation, see if game should be canceled
            if (!mPlaying && shouldCancelGame(room)) {
                mRealTimeMultiplayerClient.leave(mJoinedRoomConfig, room.getRoomId());
            }
        }

        @Override
        public void onPeerJoined(@Nullable Room room, @NonNull List<String> list) {
            // Update UI status indicating new players have joined!
        }

        @Override
        public void onPeerLeft(@Nullable Room room, @NonNull List<String> list) {
            // Peer left, see if game should be canceled.
            if (!mPlaying && shouldCancelGame(room)) {
                mRealTimeMultiplayerClient.leave(mJoinedRoomConfig, room.getRoomId());
            }
        }

        @Override
        public void onConnectedToRoom(@Nullable Room room) {
            // Connected to room, record the room Id.
            mRoom = room;
            mPlayersClient.getCurrentPlayerId()
                    .addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String playerId) {
                            mMyParticipantId = mRoom.getParticipantId(playerId);
                        }
                    });
        }

        @Override
        public void onDisconnectedFromRoom(@Nullable Room room) {
            // This usually happens due to a network error, leave the game.
            mRealTimeMultiplayerClient.leave(mJoinedRoomConfig, room.getRoomId());
            // show error message and return to main screen
            mRoom = null;
            mJoinedRoomConfig = null;
        }

        @Override
        public void onPeersConnected(@Nullable Room room, @NonNull List<String> list) {
            if (mPlaying) {
                // add new player to an ongoing game
            } else if (shouldStartGame(room)) {
                // start game!
            }
        }

        @Override
        public void onPeersDisconnected(@Nullable Room room, @NonNull List<String> list) {
            if (mPlaying) {
                // do game-specific handling of this -- remove player's avatar
                // from the screen, etc. If not enough players are left for
                // the game to go on, end the game and leave the room.
            } else if (shouldCancelGame(room)) {
                // cancel the game
                mRealTimeMultiplayerClient.leave(mJoinedRoomConfig, room.getRoomId());
            }
        }

        @Override
        public void onP2PConnected(@NonNull String participantId) {
            // Update status due to new peer to peer connection.
        }

        @Override
        public void onP2PDisconnected(@NonNull String participantId) {
            // Update status due to  peer to peer connection being disconnected.
        }

    };

    private RoomUpdateCallback mRoomUpdateCallback = new RoomUpdateCallback() {

        @Override
        public void onRoomCreated(int code, @Nullable Room room) {
            // Update UI and internal state based on room updates.
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room " + room.getRoomId() + " created.");
            } else {
                Log.w(TAG, "Error creating room: " + code);
            }
        }

        @Override
        public void onJoinedRoom(int code, @Nullable Room room) {
            // Update UI and internal state based on room updates.
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room " + room.getRoomId() + " joined.");
            } else {
                Log.w(TAG, "Error joining room: " + code);
            }
        }

        @Override
        public void onLeftRoom(int code, @NonNull String roomId) {
            Log.d(TAG, "Left room" + roomId);
        }

        @Override
        public void onRoomConnected(int code, @Nullable Room room) {
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room " + room.getRoomId() + " connected.");
            } else {
                Log.w(TAG, "Error connecting to room: " + code);
            }
        }

    };

}
