package be.ucl.lfsab1509.gravityrun.tools;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {

    private static final String DEB = "beginner";
    private static final String EXPERT = "expert";
    public static final String FIRSTTIME = "firstTime";
    private static final String HIGHSCORE = "highscore";
    private static final String INDEX = "index";
    private static final String INTER = "intermediate";
    private static final String MULTILIVES = "multilives";
    private static final String MULTI_INDEX = "multi_index";
    private static final String USERNAME = "username";
    private static final int MAX_USERNAME_LENGTH = 32; // FIXME probablement trop long.
    private static final int HIGH_SCORE_MAX_COUNT = 3;
    public static I18NBundle i18n;

    // La liste des scores obtenus, triés du plus grand au plus petit.
    private ArrayList<Integer> beginnerScoreList, expertScoreList, intermediateScoreList;
    private ArrayList<Integer> highScoreList;
    private boolean firstTime = true;
    private GravityRun game;
    private int indexSelected = 1, multi_IndexSelected = 1, multiLives = 3;
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
        username = userMap.get(USERNAME).toString();

        Object firstTime1 = userMap.get(FIRSTTIME);
        if (firstTime1 instanceof Boolean)
            firstTime = (Boolean) firstTime1;
        else
            firstTime = Boolean.parseBoolean((String) firstTime1);

        Object indexSelected1 = userMap.get(INDEX);
        if (indexSelected1 instanceof Integer)
            indexSelected = (Integer) indexSelected1;
        else
            indexSelected = Integer.parseInt((String) indexSelected1);

        highScoreList = convertStoA((String) userMap.get(HIGHSCORE));
        beginnerScoreList = convertStoA((String) userMap.get(DEB));
        intermediateScoreList = convertStoA((String) userMap.get(INTER));
        expertScoreList = convertStoA((String) userMap.get(EXPERT));
    }

    /**
     * Ajoute {@code score} à la liste des scores pour le niveau spécifié,
     * et renvoie true ou false selon qu'il s'agit du nouveau high score.
     *
     * @param score le score à ajouter à la liste
     * @param level le niveau auquel il faut ajouter le score ; commence à 0.
     * @return {@code true} s'il s'agit du nouveau high score, {@code false} sinon.
     */
    public boolean addScore(int score, int level) {
        ArrayList<Integer> liste = getScoreList(level);

        int insertionIndex = 0;
        for (; insertionIndex < liste.size() && liste.get(insertionIndex) > score; insertionIndex++) ;

        if (insertionIndex >= liste.size() || score != liste.get(insertionIndex)) {
            liste.add(insertionIndex, score);
            return insertionIndex == 0;
        } else
            return false;
    }

    /**
     * Ajoute {@code score} à la liste des scores pour le niveau actuellement sélectionné,
     * et renvoie true ou false selon qu'il s'agit du nouveau high score.
     *
     * @param score le score à ajouter à la liste du niveau courant
     * @return {@code true} s'il s'agit du nouveau high score, {@code false} sinon.
     */
    public boolean addScore(int score) {
        boolean result = addScore(score, this.indexSelected);
        shrinkScoreList(indexSelected);
        return result;
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

    public int getMulti_IndexSelected() {
        return multi_IndexSelected;
    }

    public Integer getMultiLives() {
        return multiLives;
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

    // Utilisé uniquement dans FirstScreen...
    public boolean setUsername(String username) {
        if (checkUsername(username)) {
            this.username = username;
            return true;
        } else
            return false;
    }

    public void setMulti_IndexSelected(int multi_IndexSelected) {
        this.multi_IndexSelected = multi_IndexSelected;
    }

    public void setMultiLives(int multiLives) {
        this.multiLives = multiLives;
    }

    private void shrinkScoreList(int level) {
        ArrayList<Integer> list = getScoreList(level);
        while (list.size() > HIGH_SCORE_MAX_COUNT)
            list.remove(list.size() - 1);
    }

    private Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();

        map.put(USERNAME, username);
        map.put(DEB, convertAtoS(beginnerScoreList));
        map.put(INTER, convertAtoS(intermediateScoreList));
        map.put(EXPERT, convertAtoS(expertScoreList));
        map.put(FIRSTTIME, firstTime);
        map.put(INDEX, indexSelected);
        map.put(HIGHSCORE, convertAtoS(highScoreList));

        return map;
    }

    public void write() {
        game.preferences.put(toMap());
        game.preferences.flush();
    }

}
