package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

public class Tube {

    public static final int TUBE_WIDTH = 52;
    private static final int MIN_SPACE = 3 * 34;

    private Random rand;
    private Rectangle boundsBot, boundsTop;
    private Texture bottomTube, topTube;
    private Vector2 posBotTube, posTopTube;

    public Tube(float y){
        rand = new Random();

        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");

        posBotTube = new Vector2(rand.nextInt(GravityRun.WIDTH / 2 - MIN_SPACE) - bottomTube.getWidth(), y);
        posTopTube = new Vector2(posBotTube.x + topTube.getWidth() + MIN_SPACE, y);

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
        posBotTube = new Vector2(rand.nextInt(GravityRun.WIDTH / 2 - MIN_SPACE) - bottomTube.getWidth(), y);
        posTopTube = new Vector2(posBotTube.x + topTube.getWidth() + MIN_SPACE, y);
        boundsTop.setPosition(posTopTube.x, y);
        boundsBot.setPosition(posBotTube.x, y);
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(boundsTop) || player.overlaps(boundsBot);
    }

    public void dispose(){
        bottomTube.dispose();
        topTube.dispose();
    }

}