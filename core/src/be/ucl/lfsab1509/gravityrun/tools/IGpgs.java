package be.ucl.lfsab1509.gravityrun.tools;

public interface IGpgs {

    void incrementAchievement(String achievementId, int increment);

    void invitePlayers();

    boolean isSignedIn();

    void onPause();

    void onResume();

    void setStartGameCallbask(StartGameCallback startGameCallbask);

    void signInSilently();

    void signOut();

    void showAchievements();

    void showInvitationInbox();

    void showLeaderboards();

    void startQuickGame(long role);

    void startSignInIntent();

    void submitScore(String leaderboardId, int score);

    void unlockAchievement(String achievementId);

    interface StartGameCallback {
        void startGame();
    }

}
