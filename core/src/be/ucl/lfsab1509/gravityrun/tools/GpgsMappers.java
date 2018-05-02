package be.ucl.lfsab1509.gravityrun.tools;

public class GpgsMappers {

    public static String mapToGpgsAchievement(String independantId) {
        if (independantId == null)
            return null;

        String gpgsId;
        switch (independantId) {
            case "SCORE_1_000": gpgsId = "CgkI0ITMmsAMEAIQDw"; break;
            case "SCORE_10_000": gpgsId = "CgkI0ITMmsAMEAIQEA"; break;
            case "SCORE_100_000": gpgsId = "CgkI0ITMmsAMEAIQEQ"; break;
            case "SCORE_1_000_000": gpgsId = "CgkI0ITMmsAMEAIQEg"; break;
            case "HOLE_100": gpgsId = "CgkI0ITMmsAMEAIQCQ"; break;
            case "HOLE_500": gpgsId = "CgkI0ITMmsAMEAIQCg"; break;
            case "HOLE_1000": gpgsId = "CgkI0ITMmsAMEAIQCw"; break;
            case "BOTTOM_100": gpgsId = "CgkI0ITMmsAMEAIQDA"; break;
            case "BOTTOM_500": gpgsId = "CgkI0ITMmsAMEAIQDQ"; break;
            case "BOTTOM_1000": gpgsId = "CgkI0ITMmsAMEAIQDg"; break;
            case "INVINCIBLE_10": gpgsId = "CgkI0ITMmsAMEAIQEw"; break;
            case "INVINCIBLE_50": gpgsId = "CgkI0ITMmsAMEAIQFA"; break;
            case "INVINCIBLE_100": gpgsId = "CgkI0ITMmsAMEAIQFQ"; break;
            case "NEWLIFE_10": gpgsId = "CgkI0ITMmsAMEAIQFg"; break;
            case "NEWLIFE_50": gpgsId = "CgkI0ITMmsAMEAIQFw"; break;
            case "NEWLIFE_100": gpgsId = "CgkI0ITMmsAMEAIQGA"; break;
            case "SCOREBONUS_10": gpgsId = "CgkI0ITMmsAMEAIQGQ"; break;
            case "SCOREBONUS_50": gpgsId = "CgkI0ITMmsAMEAIQGg"; break;
            case "SCOREBONUS_100": gpgsId = "CgkI0ITMmsAMEAIQGw"; break;
            case "SLOWDOWN_10": gpgsId = "CgkI0ITMmsAMEAIQHA"; break;
            case "SLOWDOWN_50": gpgsId = "CgkI0ITMmsAMEAIQHQ"; break;
            case "SLOWDOWN_100": gpgsId = "CgkI0ITMmsAMEAIQHg"; break;
            default: gpgsId = null;
        }
        return gpgsId;
    }

    public static String mapToGpgsLeaderBoard(String independantId) {
        if (independantId == null)
            return null;

        String gpgsId;
        switch (independantId) {
            case "LEADERBOARD_1": gpgsId = "CgkI0ITMmsAMEAIQAw"; break;
            case "LEADERBOARD_2": gpgsId = "CgkI0ITMmsAMEAIQAg"; break;
            case "LEADERBOARD_3": gpgsId = "CgkI0ITMmsAMEAIQBA"; break;
            default: gpgsId = null;
        }
        return gpgsId;
    }

}
