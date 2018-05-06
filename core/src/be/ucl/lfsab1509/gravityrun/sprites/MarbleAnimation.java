package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class MarbleAnimation {

    private static final float CYCLE_TIME = 1;
    private static final int FRAME_COUNT = 5;
    private static final int MARBLE_COUNT = 8 + 1 + 4;
    private static final float MAX_FRAME_TIME = CYCLE_TIME / FRAME_COUNT;

    private Array<Array<TextureRegion>> frames;
    private float currentFrameTime;
    private int frame;

    MarbleAnimation(Texture marbles, float standardWidth) {
        TextureRegion region = new TextureRegion(marbles);
        frames = new Array<>();
        for (int i = 0; i < MARBLE_COUNT; i++) {
            frames.add(new Array<>());
            for (int j = 0; j < FRAME_COUNT; j++) {
                int diameter = (int) (i * standardWidth / 80);
                int x = (int) ((i * (i - 1)) * standardWidth / 160);
                int y = j * diameter;
                frames.get(i).add(new TextureRegion(region, x, y, diameter, diameter));
            }
        }
        currentFrameTime = 0;
        frame = 0;
    }

    int getDiameter(float z) {
        int marble = getMarble(z);
        return frames.get(marble).get(frame).getRegionWidth();
    }

    TextureRegion getFrame(float z) {
        int marble = getMarble(z);
        return frames.get(marble).get(frame);
    }

    private int getMarble(float z) {
        int marble;
        if (z == 0)
            marble = 8;
        else if (z < Marble.JUMP_HEIGHT * -1.75f)
            marble = 0;
        else if (z < Marble.JUMP_HEIGHT * -1.5f)
            marble = 1;
        else if (z < Marble.JUMP_HEIGHT * -1.25f)
            marble = 2;
        else if (z < Marble.JUMP_HEIGHT * -1f)
            marble = 3;
        else if (z < Marble.JUMP_HEIGHT * -.75f)
            marble = 4;
        else if (z < Marble.JUMP_HEIGHT * -.5f)
            marble = 5;
        else if (z < Marble.JUMP_HEIGHT * -.25f)
            marble = 6;
        else if (z < Marble.JUMP_HEIGHT * -0f)
            marble = 7;
        else if (z <= Marble.JUMP_HEIGHT * .25f)
            marble = 9;
        else if (z <= Marble.JUMP_HEIGHT * .5f)
            marble = 10;
        else if (z <= Marble.JUMP_HEIGHT * .75f)
            marble = 11;
        else    // z > Marble.JUMP_HEIGHT * .75f
            marble = 12;
        return marble;
    }

    public void update(float dt, boolean gameOver) {
        if (!gameOver)
            currentFrameTime += dt;
        if (currentFrameTime > MAX_FRAME_TIME) {
            frame = (frame + 1) % FRAME_COUNT;
            currentFrameTime = 0;
        }
    }

}
