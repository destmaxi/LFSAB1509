package be.ucl.lfsab1509.gravityrun.gpgs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.games.*;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.InvitationCallback;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GpgsMultiplayer {

    final static int MIN_PLAYERS = 2;
    static final int RC_SIGN_IN = 9001;
    static final int RC_ACHIEVEMENT_UI = 9003;
    static final int RC_LEADERBOARD_UI = 9004;
    static final int RC_SELECT_PLAYERS = 9006;
    static final int RC_WAITING_ROOM = 9007;
    static final int RC_INVITATION_INBOX = 9008;
    static final String TAG = "GpgsMultiplayer";

    AchievementsClient mAchievementsClient = null;
    Activity context;
    boolean connected = false, mPlaying = false, mWaitingRoomFinishedFromCode = false;
    ErrorCallback errorCallback;
    GamesClient mGamesClient = null;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount mSignedInAccount = null;
    HashSet<Integer> pendingMessageSet = new HashSet<>();
    InvitationsClient mInvitationsClient = null;
    LeaderboardsClient mLeaderboardsClient = null;
    PlayersClient mPlayersClient = null;
    RealTimeMultiplayerClient mRealTimeMultiplayerClient = null;
    Room mRoom;
    RoomConfig mJoinedRoomConfig;
    Set<String> mFinishedRacers;
    StartGameCallback startGameCallback;
    String mDisplayName = null, mMyParticipantId = null, mPlayerId = null;

    /*
        Multiplayer methods
    */

    private void checkForInvitation() {
        mGamesClient.getActivationHint()
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
                ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                errorCallback.error("Erreur lors de la vérification des Invitations");
            }
        });
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

    public void invitePlayers() {
        mRealTimeMultiplayerClient.getSelectOpponentsIntent(1, 1, true)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        context.startActivityForResult(intent, RC_SELECT_PLAYERS);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                errorCallback.error("Erreur lors de l'invitation des autres joueurs.");
            }
        });
    }

    private void onStartGameMessageReceived() {
        mWaitingRoomFinishedFromCode = true;
        context.finishActivity(RC_WAITING_ROOM);
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
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                errorCallback.error("Erreur lors de l'envoi des données");
                            }
                        });
            }
        }
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

    public void showInvitationInbox() {
        mInvitationsClient.getInvitationInboxIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        context.startActivityForResult(intent, RC_INVITATION_INBOX);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                errorCallback.error("Impossible de charger les Invitations.");
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
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                errorCallback.error("Erreur lors de l'affichage de la salle d'attente");
            }
        });
    }

    public void startQuickGame(long role) {
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

//        showWaitingRoom(mRoom, 2);
    }

    synchronized void recordMessageToken(int tokenId) {
        pendingMessageSet.add(tokenId);
    }

    InvitationCallback mInvitationCallbackHandler = new InvitationCallback() {
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

    OnRealTimeMessageReceivedListener mMessageReceivedHandler = new OnRealTimeMessageReceivedListener() {
        @Override
        public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
            // Handle messages received here.
            byte[] message = realTimeMessage.getMessageData();
            // process message contents...
        }
    };

    RealTimeMultiplayerClient.ReliableMessageSentCallback handleMessageSentCallback = new RealTimeMultiplayerClient.ReliableMessageSentCallback() {
        @Override
        public void onRealTimeMessageSent(int statusCode, int tokenId, String recipientId) {
            // handle the message being sent.
            synchronized (this) {
                pendingMessageSet.remove(tokenId);
            }
        }
    };

    RoomStatusUpdateCallback mRoomStatusCallbackHandler = new RoomStatusUpdateCallback() {
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
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    errorCallback.error("Erreur lors de la connexion à la salle.");
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

    RoomUpdateCallback mRoomUpdateCallback = new RoomUpdateCallback() {
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

    public interface ErrorCallback {
        void error(String message);
    }

    public interface StartGameCallback {
        void startGame();
    }

}
