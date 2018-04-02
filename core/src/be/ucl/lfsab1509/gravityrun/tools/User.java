package be.ucl.lfsab1509.gravityrun.tools;

import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.ucl.lfsab1509.gravityrun.GravityRun;

public class User {

    private static final String DEB = "beginner";
    private static final String EXPERT = "expert";
    public static final String FIRSTTIME = "firstTime";
    private static final String HIGHSCORE = "highscore";
    private static final String INDEX = "index";
    private static final String INTER = "intermediate";
    private static final String USERNAME = "username";

    private ArrayList<Integer> beginner, expert, highScore, inter;
    private boolean firstTime;
    private Integer indexSelected;
    private String username;

    public User() {
        firstTime = false;
    }

    public User(Map<String, ?> userMap) {
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

        highScore = convertStoA((String) userMap.get(HIGHSCORE));
        beginner = convertStoA((String) userMap.get(DEB));
        inter = convertStoA((String) userMap.get(INTER));
        expert = convertStoA((String) userMap.get(EXPERT));
    }

    private String convertAtoS(ArrayList<Integer> arrayList) {
        Json json = new Json();
        return json.toJson(arrayList, ArrayList.class);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Integer> convertStoA(String text) {
        Json json = new Json();
        if (text == null)
            return new ArrayList<Integer>();
        return json.fromJson(ArrayList.class, text);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put(USERNAME, username);
        map.put(DEB, convertAtoS(beginner));
        map.put(INTER, convertAtoS(inter));
        map.put(EXPERT, convertAtoS(expert));
        map.put(FIRSTTIME, firstTime);
        map.put(INDEX, indexSelected);
        map.put(HIGHSCORE, convertAtoS(highScore));

        return map;
    }

    public static boolean checkUsername(String username) {
        return (username.length() > 0) && (username.length() <= 42) && (!username.equals(GravityRun.i18n.format("username")));
    }

    public static String getUsernameError(String username) {
        if (username.length() > 42)
            return GravityRun.i18n.format("error_username_length");
        else if (username.length() <= 0)
            return GravityRun.i18n.format("error_username_empty");
        else if (username.equals(GravityRun.i18n.format("username")))
            return GravityRun.i18n.format("error_username_default");
        else
            return GravityRun.i18n.format("error_username_default");
        // TODO trouver un message d'errur si ce n'est ni l'un ni l'autre (s'il n'y a pas d'autre erreur, alors juste virer le else if)
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<Integer> getBeginner() {
        return beginner;
    }

    public ArrayList<Integer> getInter() {
        return inter;
    }

    public ArrayList<Integer> getExpert() {
        return expert;
    }

    public ArrayList<Integer> getHighScore() {
        return highScore;
    }

    public Integer getIndexSelected() {
        return indexSelected;
    }

    public boolean setUsername(String username) {
        if (checkUsername(username)) {
            this.username = username;
            return true;
        } else {
            return false;
        }
    }

    public void setBeginner(ArrayList<Integer> beginner) {
        this.beginner = beginner;
    }

    public void setInter(ArrayList<Integer> inter) {
        this.inter = inter;
    }

    public void setExpert(ArrayList<Integer> expert) {
        this.expert = expert;
    }

    public void setFirstTimeTrue() {
        this.firstTime = true;
    }

    public void setIndexSelected(Integer indexSelected) {
        this.indexSelected = indexSelected;
    }

    public void setHighScore(ArrayList<Integer> highScore) {
        this.highScore = highScore;
    }

}
