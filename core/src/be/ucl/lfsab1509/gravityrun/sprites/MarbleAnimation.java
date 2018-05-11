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
        if (z == 0)
            return  8;

        for (int i = -7; i <= 0; i++)
            if (z < i * .25f * Marble.JUMP_HEIGHT)
                return 7 + i;

        for (int i = 0; i < 3; i++)
            if (z <= i * .25f * Marble.JUMP_HEIGHT)
                return 9 + i;

        return 12;
    }

    public void update(float dt) {
        currentFrameTime += dt;
        if (currentFrameTime > MAX_FRAME_TIME) {
            frame = (frame + 1) % FRAME_COUNT;
            currentFrameTime = 0;
        }
    }

}
