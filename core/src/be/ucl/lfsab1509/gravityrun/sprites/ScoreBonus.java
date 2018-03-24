package be.ucl.lfsab1509.gravityrun.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import be.ucl.lfsab1509.gravityrun.GravityRun;

public class ScoreBonus extends Bonus {
    
    public ScoreBonus(float y, int sw) {
        super();
        bonusTexture = new Texture("drawable-" + sw + "/pause.png");
        position = new Vector2(rand.nextInt(GravityRun.WIDTH - bonusTexture.getWidth()), y);
        bounds = new Rectangle(position.x, position.y, bonusTexture.getWidth(), bonusTexture.getHeight());
    }

    @Override
    public void reposition(float y) {
        position = new Vector2(rand.nextInt(GravityRun.WIDTH - bonusTexture.getWidth()), y);
        bounds = new Rectangle(position.x, position.y, bonusTexture.getWidth(), bonusTexture.getHeight());
    }

    @Override
    public boolean collides(Marble marble) {
        return Intersector.overlaps(marble.getBounds(), (Rectangle) bounds);
    }
}
