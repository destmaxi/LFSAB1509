package be.ucl.lfsab1509.gravityrun.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Skin extends com.badlogic.gdx.scenes.scene2d.ui.Skin {

    private BitmapFont font;

    public Skin() {
        super();
    }

    public void createSkin(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();

        parameter.size = (int) (size * Gdx.graphics.getDensity());
        font = generator.generateFont(parameter);
        generator.dispose();
        this.add("arial", font);
        this.addRegions(new TextureAtlas("skin/uiskin.atlas"));
        this.load(Gdx.files.internal("skin/uiskin.json"));
    }

    public void dispose() {
        font.dispose();
    }

}
