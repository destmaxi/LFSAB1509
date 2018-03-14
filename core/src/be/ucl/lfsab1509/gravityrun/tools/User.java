package be.ucl.lfsab1509.gravityrun.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.ucl.lfsab1509.gravityrun.GravityRun;

/**
 * Created by maxde on 14-03-18.
 */

public class User {
    private String username;
    private boolean firstTime;
    private ArrayList<Integer> beginner, inter, expert;
    private Integer indexSelected;

    public User(Map<String,?> userMap){

        username = userMap.get(GravityRun.USERNAME).toString();
        firstTime = (Boolean) userMap.get(GravityRun.FIRSTTIME);
        indexSelected = (Integer) userMap.get(GravityRun.INDEX);
        beginner = (ArrayList<Integer>) userMap.get(GravityRun.DEB);
        inter = (ArrayList<Integer>) userMap.get(GravityRun.INTER);
        expert = (ArrayList<Integer>) userMap.get(GravityRun.EXPERT);
    }

    public User(){
        firstTime = false;
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
        map.put(GravityRun.DEB,beginner);
        map.put(GravityRun.INTER,inter);
        map.put(GravityRun.EXPERT,expert);
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
