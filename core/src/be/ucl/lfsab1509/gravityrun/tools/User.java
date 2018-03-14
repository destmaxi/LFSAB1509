package be.ucl.lfsab1509.gravityrun.tools;

import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.ucl.lfsab1509.gravityrun.GravityRun;

public class User {
    private String username;
    private boolean firstTime;
    private ArrayList<Integer> beginner, inter, expert;
    private Integer indexSelected;

    public User(Map<String,?> userMap){

        username = userMap.get(GravityRun.USERNAME).toString();
        firstTime = (Boolean) userMap.get(GravityRun.FIRSTTIME);
        indexSelected = (Integer) userMap.get(GravityRun.INDEX);
        beginner = convertStoA((String)userMap.get(GravityRun.DEB));
        inter = convertStoA((String)userMap.get(GravityRun.INTER));
        expert = convertStoA((String)userMap.get(GravityRun.EXPERT));
    }

    public User(){
        firstTime = false;
    }

    private String convertAtoS(ArrayList<Integer> arrayList){
        Json json = new Json();
        return json.toJson(arrayList, ArrayList.class);
    }

    private ArrayList<Integer> convertStoA(String text){
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

    public Map<String,Object> toMap(){
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put(GravityRun.USERNAME,username);
        map.put(GravityRun.DEB,convertAtoS(beginner));
        map.put(GravityRun.INTER,convertAtoS(inter));
        map.put(GravityRun.EXPERT,convertAtoS(expert));
        map.put(GravityRun.FIRSTTIME,firstTime);
        map.put(GravityRun.INDEX,indexSelected);

        return map;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public Integer getIndexSelected() {
        return indexSelected;
    }

    public void setIndexSelected(Integer indexSelected) {
        this.indexSelected = indexSelected;
    }
}
