package be.ucl.lfsab1509.gravityrun.tools;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Skin extends com.badlogic.gdx.scenes.scene2d.ui.Skin {

    private BitmapFont font;

    public void createSkin(int size, FreeTypeFontGenerator generator, TextureAtlas atlas) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.incremental = false;
        parameter.size = (int) (size * GravityRun.DENSITY);

        font = generator.generateFont(parameter);

        this.add("arial", font);

        this.addRegions(atlas);

        this.load(Gdx.files.internal("skin/uiskin.json"));
    }

    public void dispose() {
        font.dispose();
    }

}
