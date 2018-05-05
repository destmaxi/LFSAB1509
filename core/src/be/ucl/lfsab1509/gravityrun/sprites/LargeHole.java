package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.PlayScreen;
import com.badlogic.gdx.graphics.Texture;

public class LargeHole extends Hole {

    public LargeHole(float y, int marbleWidth, Texture texture, PlayScreen playScreen) {
        super(0, y, marbleWidth, texture, playScreen);

        bounds.set(position.x, position.y + marbleWidth / 2,
                texture.getWidth(), texture.getHeight() - marbleWidth);
    }
}
