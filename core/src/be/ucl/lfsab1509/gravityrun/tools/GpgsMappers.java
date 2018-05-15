package be.ucl.lfsab1509.gravityrun.tools;

public class GpgsMappers {

    public static String mapToGpgsAchievement(String independantId) {
        if (independantId == null)
            return null;

        String gpgsId;
        switch (independantId) {
            case "SCORE_1000": gpgsId = "CgkI0ITMmsAMEAIQDw"; break;
            case "SCORE_10000": gpgsId = "CgkI0ITMmsAMEAIQEA"; break;
            case "SCORE_100000": gpgsId = "CgkI0ITMmsAMEAIQEQ"; break;
            case "SCORE_1000000": gpgsId = "CgkI0ITMmsAMEAIQEg"; break;
            case "HOLE_100": gpgsId = "CgkI0ITMmsAMEAIQCQ"; break;
            case "HOLE_500": gpgsId = "CgkI0ITMmsAMEAIQCg"; break;
            case "HOLE_1000": gpgsId = "CgkI0ITMmsAMEAIQCw"; break;
            case "BOTTOM_100": gpgsId = "CgkI0ITMmsAMEAIQDA"; break;
            case "BOTTOM_500": gpgsId = "CgkI0ITMmsAMEAIQDQ"; break;
            case "BOTTOM_1000": gpgsId = "CgkI0ITMmsAMEAIQDg"; break;
            case "INVINCIBLE_10": gpgsId = "CgkI0ITMmsAMEAIQEw"; break;
            case "INVINCIBLE_50": gpgsId = "CgkI0ITMmsAMEAIQFA"; break;
            case "INVINCIBLE_100": gpgsId = "CgkI0ITMmsAMEAIQFQ"; break;
            case "NEW_LIFE_10": gpgsId = "CgkI0ITMmsAMEAIQFg"; break;
            case "NEW_LIFE_50": gpgsId = "CgkI0ITMmsAMEAIQFw"; break;
            case "NEW_LIFE_100": gpgsId = "CgkI0ITMmsAMEAIQGA"; break;
            case "SCORE_BONUS_10": gpgsId = "CgkI0ITMmsAMEAIQGQ"; break;
            case "SCORE_BONUS_50": gpgsId = "CgkI0ITMmsAMEAIQGg"; break;
            case "SCORE_BONUS_100": gpgsId = "CgkI0ITMmsAMEAIQGw"; break;
            case "SLOW_DOWN_10": gpgsId = "CgkI0ITMmsAMEAIQHA"; break;
            case "SLOW_DOWN_50": gpgsId = "CgkI0ITMmsAMEAIQHQ"; break;
            case "SLOW_DOWN_100": gpgsId = "CgkI0ITMmsAMEAIQHg"; break;
            case "SPEED_UP_10": gpgsId = "CgkI0ITMmsAMEAIQHw"; break;
            case "SPEED_UP_50": gpgsId = "CgkI0ITMmsAMEAIQIA"; break;
            case "SPEED_UP_100": gpgsId = "CgkI0ITMmsAMEAIQIQ"; break;
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
