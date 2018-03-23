package be.ucl.lfsab1509.gravityrun.tools;

import com.badlogic.gdx.utils.Json;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {

    private String username;
    private boolean firstTime;
    private ArrayList<Integer> beginner, inter, expert, highScore;
    private Integer indexSelected;
    private static final String DEB = "beginner", INTER = "intermediate",EXPERT = "expert",USERNAME="username",INDEX = "index", HIGHSCORE = "highscore";
    public static final String FIRSTTIME = "firstTime";


    public User(Map<String,?> userMap) {
        username = userMap.get(USERNAME).toString();

        Object firstTime1 = userMap.get(FIRSTTIME);
        if (firstTime1 instanceof Boolean)
            firstTime = (Boolean) firstTime1;
        else
            firstTime = Boolean.parseBoolean((String)firstTime1);

        Object indexSelected1 = userMap.get(INDEX);
        if (indexSelected1 instanceof Integer)
            indexSelected = (Integer) indexSelected1;
        else
            indexSelected = Integer.parseInt((String)indexSelected1);

        highScore = convertStoA((String)userMap.get(HIGHSCORE));
        beginner = convertStoA((String)userMap.get(DEB));
        inter = convertStoA((String)userMap.get(INTER));
        expert = convertStoA((String)userMap.get(EXPERT));
    }

    public User() {
        firstTime = false;
    }

    private String convertAtoS(ArrayList<Integer> arrayList) {
        Json json = new Json();
        return json.toJson(arrayList, ArrayList.class);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Integer> convertStoA(String text) {
        Json json = new Json();
        if(text == null)
            return new ArrayList<Integer>();
        return json.fromJson(ArrayList.class, text);
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

    public void setHighScore(ArrayList<Integer> highScore) {
        this.highScore = highScore;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Map<String,Object> toMap() {
        HashMap<String,Object> map = new HashMap<String, Object>();

        map.put(USERNAME,username);
        map.put(DEB,convertAtoS(beginner));
        map.put(INTER,convertAtoS(inter));
        map.put(EXPERT,convertAtoS(expert));
        map.put(FIRSTTIME,firstTime);
        map.put(INDEX,indexSelected);
        map.put(HIGHSCORE,convertAtoS(highScore));

        return map;
    }

    public void setFirstTimeTrue() {
        this.firstTime = true;
    }

    public Integer getIndexSelected() {
        return indexSelected;
    }

    public void setIndexSelected(Integer indexSelected) {
        this.indexSelected = indexSelected;
    }
}
