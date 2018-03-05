package be.ucl.lfsab1509.gravityrun.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Skin extends com.badlogic.gdx.scenes.scene2d.ui.Skin {

    private BitmapFont font12;
    private com.badlogic.gdx.scenes.scene2d.ui.Skin skin;

    public Skin() {
        super();
    }

    public void createSkin(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size * (int) Gdx.graphics.getDensity();
        font12 = generator.generateFont(parameter); // Font size : 12 pixels
        generator.dispose();
        skin = new com.badlogic.gdx.scenes.scene2d.ui.Skin();
        this.add("arial",font12);
        this.addRegions(new TextureAtlas("skin/uiskin.atlas"));
        this.load(Gdx.files.internal("skin/uiskin.json"));
    }

    public void dispose() {
        font12.dispose();
        skin.dispose();
    }

}
