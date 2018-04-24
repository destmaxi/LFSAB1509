package be.ucl.lfsab1509.gravityrun.tools;

public interface IGpgs {

    void incrementAchievement(String achievementId, int increment);

    void invitePlayers();

    boolean isSignedIn();

    void onPause();

    void onResume();

    void signInSilently();

    void signOut();

    boolean showAchievements();

    void showInvitationInbox();

    boolean showLeaderboards();

    void startQuickGame(long role);

    void startSignInIntent();

    void submitScore(String leaderboardId, int score);

    void unlockAchievement(String achievementId);

}
