package be.ucl.lfsab1509.gravityrun.tools;

public interface IGpgs {

    void incrementAchievement(String achievementId, int increment);

    boolean isSignedIn();

    void onPause();

    void onResume();

    void signInSilently();

    void signOut();

    boolean showAchievements();

    boolean showLeaderboards();

    void signIn();

    void submitScore(String leaderboardId, int score);

    void unlockAchievement(String achievementId);

}
