package be.ucl.lfsab1509.gravityrun.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;


/**
 * Created by maxime on 04/03/2018.
 */

public class Skin extends com.badlogic.gdx.scenes.scene2d.ui.Skin {

    public Skin() {
        super();
    }

    public void createSkin(int size){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size; //* (int) Gdx.graphics.getDensity();
        FreeTypeFontGenerator.setMaxTextureSize(FreeTypeFontGenerator.NO_MAXIMUM);
        BitmapFont font12 = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose();

        this.add("arial",font12);

        this.addRegions(new TextureAtlas("skin/uiskin.atlas"));
        this.load(Gdx.files.internal("skin/uiskin.json"));
    }
}
