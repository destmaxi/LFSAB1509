package be.ucl.lfsab1509.gravityrun.tools;

public class GpgsMappers {

    public static String mapToGpgsAchievement(String independantId) {
        if (independantId == null)
            return null;

        String gpgsId;

        switch (independantId) {
            case "SCORE1_000": gpgsId = "CgkI0ITMmsAMEAIQDw"; break;
            case "SCORE10_000": gpgsId = "CgkI0ITMmsAMEAIQEA"; break;
            case "SCORE100_000": gpgsId = "CgkI0ITMmsAMEAIQEQ"; break;
            case "SCORE1_000_000": gpgsId = "CgkI0ITMmsAMEAIQEg"; break;
            case "HOLE100": gpgsId = "CgkI0ITMmsAMEAIQCQ"; break;
            case "HOLE500": gpgsId = "CgkI0ITMmsAMEAIQCg"; break;
            case "HOLE1000": gpgsId = "CgkI0ITMmsAMEAIQCw"; break;
            case "BOTTOM100": gpgsId = "CgkI0ITMmsAMEAIQDA"; break;
            case "BOTTOM500": gpgsId = "CgkI0ITMmsAMEAIQDQ"; break;
            case "BOTTOM1000": gpgsId = "CgkI0ITMmsAMEAIQDg"; break;
            case "INVINCIBLE10": gpgsId = "CgkI0ITMmsAMEAIQEw"; break;
            case "INVINCIBLE50": gpgsId = "CgkI0ITMmsAMEAIQFA"; break;
            case "INVINCIBLE100": gpgsId = "CgkI0ITMmsAMEAIQFQ"; break;
            case "NEWLIFE10": gpgsId = "CgkI0ITMmsAMEAIQFg"; break;
            case "NEWLIFE50": gpgsId = "CgkI0ITMmsAMEAIQFw"; break;
            case "NEWLIFE100": gpgsId = "CgkI0ITMmsAMEAIQGA"; break;
            case "SCOREBONUS10": gpgsId = "CgkI0ITMmsAMEAIQGQ"; break;
            case "SCOREBONUS50": gpgsId = "CgkI0ITMmsAMEAIQGg"; break;
            case "SCOREBONUS100": gpgsId = "CgkI0ITMmsAMEAIQGw"; break;
            case "SLOWDOWN10": gpgsId = "CgkI0ITMmsAMEAIQHA"; break;
            case "SLOWDOWN50": gpgsId = "CgkI0ITMmsAMEAIQHQ"; break;
            case "SLOWDOWN100": gpgsId = "CgkI0ITMmsAMEAIQHg"; break;
            default: gpgsId = null;
        }

        return gpgsId;
    }

    public static String mapToGpgsLeaderBoard(String independantId) {
        if (independantId == null)
            return null;

        String gpgsId;

        switch (independantId) {
            case "LEADERBOARD1": gpgsId = "CgkI0ITMmsAMEAIQAw"; break;
            case "LEADERBOARD2": gpgsId = "CgkI0ITMmsAMEAIQAg"; break;
            case "LEADERBOARD3": gpgsId = "CgkI0ITMmsAMEAIQBA"; break;
            default: gpgsId = null;
        }

        return gpgsId;
    }

}
