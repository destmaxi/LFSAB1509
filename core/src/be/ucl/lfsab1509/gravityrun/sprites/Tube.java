package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by maxime on 01/03/2018.
 */

public class Tube {
    public static final int TUBE_WIDTH = 52;
    private static final int FLUCTUATION = 95;
    private static final int TUBE_GAP = 100;
    private static final int LOWEST_OPENING =50;
    private Texture topTube, bottomTube;
    private Vector2 posTopTube, posBotTube;
    private Rectangle boundsTop, boundsBot;
    private Random rand;


    public Tube(float y){
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        rand = new Random();

        posTopTube = new Vector2(rand.nextInt(FLUCTUATION) + LOWEST_OPENING + TUBE_GAP, y);
        posBotTube = new Vector2(posTopTube.x - TUBE_GAP - bottomTube.getWidth(),y);

        boundsTop = new Rectangle(posTopTube.x, posTopTube.y, topTube.getWidth(), topTube.getHeight());
        boundsBot = new Rectangle(posBotTube.x, posBotTube.y, bottomTube.getWidth(), bottomTube.getHeight());
    }

    public Texture getTopTube() {
        return topTube;
    }

    public Texture getBottomTube() {
        return bottomTube;
    }

    public Vector2 getPosTopTube() {
        return posTopTube;
    }

    public Vector2 getPosBotTube() {
        return posBotTube;
    }

    public void reposition(float y){
        posTopTube.set(rand.nextInt(FLUCTUATION) + LOWEST_OPENING + TUBE_GAP, y);
        posBotTube.set(posTopTube.x - TUBE_GAP - bottomTube.getWidth(), y);
        boundsTop.setPosition(posTopTube.x,y );
        boundsBot.setPosition(posBotTube.x, y);
    }

    public boolean collides(Rectangle player){
        return player.overlaps(boundsTop) || player.overlaps(boundsBot);
    }

    public void dispose(){
        topTube.dispose();
        bottomTube.dispose();
    }
}