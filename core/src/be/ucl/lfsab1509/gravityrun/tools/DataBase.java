package be.ucl.lfsab1509.gravityrun.tools;

import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import be.ucl.lfsab1509.gravityrun.GravityRun;

/**
 * Created by maxde on 06-03-18.
 */

public class DataBase {
    private static final String TABLE_NAME = "Scoreboard";
    public static final String COLUMN_EXPERT = "expert";
    public static final String COLUMN_BEGINNER = "beginner";
    public static final String COLUMN_INTERMEDIATE = "intermediate";

    private static final String DATABASE_NAME = "scoreboard.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table if not exists " + TABLE_NAME + " ( " + COLUMN_BEGINNER
            + "  integer, " + COLUMN_INTERMEDIATE + " integer, "+COLUMN_EXPERT+" integer);";

    private Database dbHandler;
    public static ArrayList<Integer> scoreList;

    public DataBase(){
        dbHandler = DatabaseFactory.getNewDatabase(DATABASE_NAME, DATABASE_VERSION, DATABASE_CREATE, null);

        dbHandler.setupDatabase();
        try {
            dbHandler.openOrCreateDatabase();
            dbHandler.execSQL(DATABASE_CREATE);
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
    }

    public void add(String col){
        DatabaseCursor cursor;

        int length ;
        if(scoreList == null)
            scoreList = new ArrayList<Integer>();

        try {
            cursor = dbHandler.rawQuery("SELECT "+col+" FROM "+TABLE_NAME);
            while(cursor.next()){
                scoreList.add(cursor.getInt(0));
            }

            sortASC(scoreList);

            for(int i = 0; i< GravityRun.scoreList.size(); i++){
                length = scoreList.size();
                if(length < 3 && !(scoreList.contains(GravityRun.scoreList.get(i)))){
                    this.dbHandler.execSQL("INSERT INTO "+TABLE_NAME +" ('"+col+ "') VALUES (" + GravityRun.scoreList.get(i)+ ")");
                    scoreList.add(GravityRun.scoreList.get(i));
                }
                else if(GravityRun.scoreList.get(i) > scoreList.get(0) && !(scoreList.contains(GravityRun.scoreList.get(i)))){
                    Integer remove = scoreList.get(0);
                    scoreList.remove(0);
                    this.dbHandler.execSQL("DELETE FROM "+ TABLE_NAME +" WHERE "+ COLUMN_BEGINNER + "="+ remove);
                    this.dbHandler.execSQL("INSERT INTO "+TABLE_NAME +" ('"+col+ "') VALUES (" + GravityRun.scoreList.get(i)+ ")");
                    scoreList.add(GravityRun.scoreList.get(i));

                    sortASC(scoreList);
                }
            }

        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> getColumn(String col){
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        DatabaseCursor cursor;

        try{
            cursor = dbHandler.rawQuery("SELECT "+col+" FROM "+TABLE_NAME);
            while (cursor.next()){
                arrayList.add(cursor.getInt(0));
            }
        }
        catch (SQLiteGdxException e){
            e.printStackTrace();
        }



        return arrayList;
    }

    public void sortDESC(ArrayList<Integer> arrayList){
        Collections.sort(arrayList, new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
               return t1.compareTo(integer);
            }
        });
    }

    private void sortASC(ArrayList<Integer> arrayList){
        Collections.sort(arrayList);
    }

    public void dispose(){
        try {
            dbHandler.closeDatabase();
        }
        catch (SQLiteGdxException e){
            e.printStackTrace();
        }
    }
}
