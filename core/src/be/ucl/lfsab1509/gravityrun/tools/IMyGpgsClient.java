package be.ucl.lfsab1509.gravityrun.tools;

public interface IMyGpgsClient {

    boolean isConnected();

    boolean isSessionActive();

    void onPause();

    void onResume();

    void signInSilently();

    void signOut();

    boolean showAchievements();

    boolean showLeaderboards();

    void startSignInIntent();

    void submitScore(String leaderboardId, int score);

}
