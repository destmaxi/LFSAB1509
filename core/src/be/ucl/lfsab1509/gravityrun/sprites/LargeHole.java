package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.PlayScreen;
import com.badlogic.gdx.graphics.Texture;

public class LargeHole extends Hole {

    public LargeHole(float y, int marbleWidth, PlayScreen playScreen, Texture texture) {
        super(0, y, playScreen, texture);
        bounds.set(position.x, position.y + marbleWidth / 2,
                texture.getWidth(), texture.getHeight() - marbleWidth);
    }

}
