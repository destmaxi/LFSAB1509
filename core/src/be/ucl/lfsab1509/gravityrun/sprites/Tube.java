package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

public class Tube {

    private static final int FLUCTUATION = 95;
    private static final int LOWEST_OPENING = 50;
    private static final int TUBE_GAP = 100;
    public static final int TUBE_WIDTH = 52;

    private Random rand;
    private Rectangle boundsBot, boundsTop;
    private Texture bottomTube, topTube;
    private Vector2 posBotTube, posTopTube;

    public Tube(float y){
        rand = new Random();

        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");

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

    public void reposition(float y) {
        posTopTube.set(rand.nextInt(FLUCTUATION) + LOWEST_OPENING + TUBE_GAP, y);
        posBotTube.set(posTopTube.x - TUBE_GAP - bottomTube.getWidth(), y);
        boundsTop.setPosition(posTopTube.x, y);
        boundsBot.setPosition(posBotTube.x, y);
    }

    public boolean collides(Rectangle player){
        return player.overlaps(boundsTop) || player.overlaps(boundsBot);
    }

    public void dispose(){
        bottomTube.dispose();
        topTube.dispose();
    }
}