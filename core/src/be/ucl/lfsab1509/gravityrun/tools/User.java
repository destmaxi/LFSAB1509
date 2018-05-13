package be.ucl.lfsab1509.gravityrun.tools;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {

    private static final int HIGH_SCORE_MAX_COUNT = 3;
    private static final String KEY_DEB = "beginner";
    private static final String KEY_EXPERT = "expert";
    public static final String KEY_FIRSTTIME = "firstTime";
    private static final String KEY_HIGHSCORE = "highscore";
    private static final String KEY_INDEX = "index";
    private static final String KEY_INTER = "intermediate";
    private static final String KEY_MUSIC_LEVEL = "musicLevel";
    private static final String KEY_SOUND_LEVEL = "soundLevel";
    private static final String KEY_USERNAME = "username";
    private static final int MAX_USERNAME_LENGTH = 32;  // FIXME probablement trop long.

    public static I18NBundle i18n;

    // La liste des scores obtenus, triés du plus grand au plus petit.
    private ArrayList<Integer> beginnerScoreList, expertScoreList, intermediateScoreList;
    private ArrayList<Integer> highScoreList;
    private boolean firstTime = true;
    private float musicLevel = .8f, soundLevel = .5f;
    private GravityRun game;
    private int indexSelected = 1, multiIndexSelected = 1, multiLives = 3, multiMode = 0;
    private String username;

    public User(GravityRun gravityRun, String username) {
        game = gravityRun;
        this.username = username;

        beginnerScoreList = new ArrayList<>();
        expertScoreList = new ArrayList<>();
        intermediateScoreList = new ArrayList<>();

        highScoreList = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            highScoreList.add(0);
    }

    public User(GravityRun gravityRun, Map<String, ?> userMap) {
        game = gravityRun;
        username = userMap.get(KEY_USERNAME).toString();
        musicLevel = (Float) userMap.get(KEY_MUSIC_LEVEL);
        soundLevel = (Float) userMap.get(KEY_SOUND_LEVEL);

        Object firstTime1 = userMap.get(KEY_FIRSTTIME);
        if (firstTime1 instanceof Boolean)
            firstTime = (Boolean) firstTime1;
        else
            firstTime = Boolean.parseBoolean((String) firstTime1);

        Object indexSelected1 = userMap.get(KEY_INDEX);
        if (indexSelected1 instanceof Integer)
            indexSelected = (Integer) indexSelected1;
        else
            indexSelected = Integer.parseInt((String) indexSelected1);

        highScoreList = convertStoA((String) userMap.get(KEY_HIGHSCORE));
        beginnerScoreList = convertStoA((String) userMap.get(KEY_DEB));
        intermediateScoreList = convertStoA((String) userMap.get(KEY_INTER));
        expertScoreList = convertStoA((String) userMap.get(KEY_EXPERT));
    }

    public boolean addScore(int score) {
        boolean result = addScore(score, this.indexSelected);
        shrinkScoreList(indexSelected);
        return result;
    }

    public boolean addScore(int score, int level) {
        ArrayList<Integer> liste = getScoreList(level);

        int insertionIndex = 0;
        for (; insertionIndex < liste.size() && liste.get(insertionIndex) > score; insertionIndex++)
            ;

        if (insertionIndex >= liste.size() || score != liste.get(insertionIndex)) {
            liste.add(insertionIndex, score);
            return insertionIndex == 0;
        } else
            return false;
    }

    public static boolean checkUsername(String username) {
        return (username.length() > 0) && (username.length() <= MAX_USERNAME_LENGTH) && (!username.equals(i18n.format("username")));
    }

    private String convertAtoS(ArrayList<Integer> arrayList) {
        Json json = new Json();
        return json.toJson(arrayList, ArrayList.class);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Integer> convertStoA(String text) {
        Json json = new Json();
        if (text == null)
            return new ArrayList<>();
        return json.fromJson(ArrayList.class, text);
    }

    public ArrayList<Integer> getBeginnerScoreList() {
        return beginnerScoreList;
    }

    public ArrayList<Integer> getExpertScoreList() {
        return expertScoreList;
    }

    public int getHighScore() {
        return getHighScore(indexSelected);
    }

    public int getMultiIndexSelected() {
        return multiIndexSelected;
    }

    public Integer getMultiLives() {
        return multiLives;
    }

    public int getMultiMode() {
        return multiMode;
    }

    private int getHighScore(int level) {
        ArrayList<Integer> list = getScoreList(level);
        if (list.size() == 0)
            return 0;
        else
            return list.get(0);
    }

    public ArrayList<Integer> getHighScores(int level, int count) {
        ArrayList<Integer> scoreList = getScoreList(level);
        ArrayList<Integer> ret = new ArrayList<>(scoreList.subList(0, Math.min(count, scoreList.size())));
        if (count > scoreList.size())   // 0-padding
            for (int i = scoreList.size(); i < count; i++)
                ret.add(0); // FIXME simplifier ce code qui est assez moche...
        return ret;
    }

    public Integer getIndexSelected() {
        return indexSelected;
    }

    public ArrayList<Integer> getIntermediateScoreList() {
        return intermediateScoreList;
    }

    public String getLevelDescription() {
        return getLevelDescription(indexSelected);
    }

    private static String getLevelDescription(int level) {
        switch (level) {
            case 0:
                return i18n.format("beginner");
            case 1:
            default:
                return i18n.format("inter");
            case 2:
                return i18n.format("expert");
        }
    }

    public String getMultiModeDescription() {
        switch (multiMode) {
            default:
            case 0:
                return game.i18n.format("mode1");
            case 1:
                return game.i18n.format("mode2");
        }
    }

    public float getMusicLevel() {
        return musicLevel;
    }

    private ArrayList<Integer> getScoreList(int level) {
        switch (level) {
            case 0:
                return beginnerScoreList;
            case 2:
                return expertScoreList;
            default:
                return intermediateScoreList;
        }
    }

    public float getSoundLevel() {
        return soundLevel;
    }

    public String getUsername() {
        return username;
    }

    public static String getUsernameError(String username) {
        if (username.length() > 42)
            return i18n.format("error_username_length");
        else if (username.length() <= 0)
            return i18n.format("error_username_empty");
        else if (username.equals(i18n.format("username")))
            return i18n.format("error_username_default");
        else
            return i18n.format("error_username_default");
    }

    public void setIndexSelected(Integer indexSelected) {
        this.indexSelected = indexSelected;
    }

    public void setMultiIndexSelected(int multiIndexSelected) {
        this.multiIndexSelected = multiIndexSelected;
    }

    public void setMultiLives(int multiLives) {
        this.multiLives = multiLives;
    }

    public void setMultiMode(int multiMode) {
        this.multiMode = multiMode;
    }

    public void setMusicLevel(float musicLevel) {
        this.musicLevel = Math.min(musicLevel, 1.0f);
    }

    public void setSoundLevel(float soundLevel) {
        this.soundLevel = Math.min(soundLevel, 1.0f);
    }

    // Utilisé uniquement dans FirstScreen...
    public boolean setUsername(String username) {
        if (checkUsername(username)) {
            this.username = username;
            return true;
        } else
            return false;
    }

    private void shrinkScoreList(int level) {
        ArrayList<Integer> list = getScoreList(level);
        while (list.size() > HIGH_SCORE_MAX_COUNT)
            list.remove(list.size() - 1);
    }

    private Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();

        map.put(KEY_USERNAME, username);
        map.put(KEY_DEB, convertAtoS(beginnerScoreList));
        map.put(KEY_INTER, convertAtoS(intermediateScoreList));
        map.put(KEY_EXPERT, convertAtoS(expertScoreList));
        map.put(KEY_FIRSTTIME, firstTime);
        map.put(KEY_INDEX, indexSelected);
        map.put(KEY_HIGHSCORE, convertAtoS(highScoreList));
        map.put(KEY_MUSIC_LEVEL, musicLevel);
        map.put(KEY_SOUND_LEVEL, soundLevel);

        return map;
    }

    public void write() {
        game.preferences.put(toMap());
        game.preferences.flush();
    }

}
