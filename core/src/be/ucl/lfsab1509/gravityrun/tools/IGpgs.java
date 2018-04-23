package be.ucl.lfsab1509.gravityrun.tools;

public interface IGpgs {

    boolean isConnected();

    void onPause();

    void onResume();

    void signInSilently();

    void signOut();

    boolean showAchievements();

    boolean showLeaderboards();

    void startSignInIntent();

    void submitScore(String leaderboardId, int score);

}
