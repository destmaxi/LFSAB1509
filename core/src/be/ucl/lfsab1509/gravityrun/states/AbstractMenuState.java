package be.ucl.lfsab1509.gravityrun.states;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import be.ucl.lfsab1509.gravityrun.tools.SoundManager;

abstract class AbstractMenuState extends State {
    /** Container width */
    float containerWidth;
    /** Container height */
    float containerHeight;
    Stage stage;

    AbstractMenuState(GameStateManager gsm, SoundManager soundManager) {
        super(gsm, soundManager);
        containerWidth = width * 0.9f;
        containerHeight = height * 0.9f;

        stage = new Stage(new ScreenViewport());
    }
}
