package be.ucl.lfsab1509.gravityrun.tools;

public class GpgsMappers {

    public static final String LEADERBOARD = "LEADERBOARD";

    public static String mapToGpgsEvent(String eventId) {
        if (eventId == null)
            return null;

        String gpgsId;

        // TODO
        switch (eventId) {
            case "TODO":
                gpgsId = "TODO";
                break;
            default:
                gpgsId = null;
        }

        return gpgsId;
    }

    public static String mapToGpgsAchievement(String independantId) {
        if (independantId == null)
            return null;

        String gpgsId;

        // TODO
        switch (independantId) {
            case "TODO":
                gpgsId = "TODO";
                break;
            default:
                gpgsId = null;
        }

        return gpgsId;
    }

    public static String mapToGpgsLeaderBoard(String independantId) {
        if (independantId == null)
            return null;

        String gpgsId;

        switch (independantId) {
            case LEADERBOARD + 1:
                gpgsId = "CgkI0ITMmsAMEAIQAw";
                break;
            case LEADERBOARD + 2:
                gpgsId = "CgkI0ITMmsAMEAIQAg";
                break;
            case LEADERBOARD + 3:
                gpgsId = "CgkI0ITMmsAMEAIQBA";
                break;
            default:
                gpgsId = null;
        }

        return gpgsId;
    }

}
