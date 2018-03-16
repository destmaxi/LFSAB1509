package be.ucl.lfsab1509.gravityrun.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import be.ucl.lfsab1509.gravityrun.GravityRun;

public class Hole extends Obstacle {

    public Hole(float y) {
        super();
        obstacleTexture = new Texture("hole.png");
        position = new Vector2(rand.nextInt((GravityRun.WIDTH - obstacleTexture.getWidth()) / 2), y);
        bounds = new Rectangle(position.x, position.y, obstacleTexture.getWidth(), obstacleTexture.getHeight());
    }

    @Override
    public void reposition(float y) {
        position = new Vector2(rand.nextInt((GravityRun.WIDTH - obstacleTexture.getWidth()) / 2), y);
        bounds = new Rectangle(position.x, position.y, obstacleTexture.getWidth(), obstacleTexture.getHeight());
    }

    @Override
    public boolean collides(Marble marble) {
        return Intersector.overlaps(marble.getBounds(), (Rectangle) bounds);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
