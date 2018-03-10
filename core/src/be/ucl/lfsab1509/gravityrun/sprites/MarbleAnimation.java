package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class MarbleAnimation {


    private Array<TextureRegion> frames;
    private float currentFrameTime, maxFrameTime;
    private int frame, frameCount;

    public MarbleAnimation(Texture marbles, int frameCount, float cycleTime) {
        TextureRegion region = new TextureRegion(marbles);
        frames = new Array<TextureRegion>();
        int frameHeight = region.getRegionHeight() / frameCount;
        int frameWidth = region.getRegionWidth();
        for (int i = 0; i < frameCount; i++) {
            frames.add(new TextureRegion(region, 0, i * frameHeight, frameWidth, frameHeight));
        }
        currentFrameTime = 0;
        frame = 0;
        this.frameCount = frameCount;
        maxFrameTime = cycleTime / frameCount;
    }

    public void update(float dt, boolean gameOver) {
        if (!gameOver)
            currentFrameTime += dt;
        if (currentFrameTime > maxFrameTime) {
            frame = (frame + 1) % frameCount;
            currentFrameTime = 0;
        }
    }

    public void dispose() {

    }

    public TextureRegion getFrame() {
        return frames.get(frame);
    }

}
