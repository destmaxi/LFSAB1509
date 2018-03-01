package be.ucl.lfsab1509.gravityrun.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.sprites.Tube;




/**
 * Created by maxde on 28-02-18.
 */

public class PlayState extends State {
    private static final int TUBE_SPACING = 80;
    private static final int TUBE_COUNT = 4;


    private Marble marble;
    private Texture bg; //will be usefull when we got a background picture
    private Texture gameOverImage;
    private Vector2 bg1, bg2; //will be usefull when we got a background picture
    private boolean gameOver;

    private Array<Tube> tubes;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, GravityRun.WIDTH/2, GravityRun.HEIGHT/2);
        gameOverImage = new Texture("gameover.png");
        // bg1 = new Vector2(0, cam.position.y - cam.viewportHeight/2); //will be usefull when we got a background picture
        //  bg2 = new Vector2(0, (cam.position.y - cam.viewportHeight/2) + bg.getHeight() );//will be usefull when we got a background picture
        marble = new Marble(100,0);
        tubes = new Array<Tube>();

        for(int i=1; i<=TUBE_COUNT;i++)
            tubes.add(new Tube(i*(TUBE_SPACING + Tube.TUBE_WIDTH)));
        gameOver = false;

    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched())
            if(gameOver)
                gsm.set(new PlayState(gsm));
    }

    @Override
    public void update(float dt) {
        handleInput();
        // updateGround(); //will be usefull when we got a background picture
        marble.update(dt);
        cam.position.y = marble.getPosition().y + 80;

        for(int i=0; i<tubes.size;i++) {
            Tube tube = tubes.get(i);

            if ((cam.position.y - cam.viewportHeight / 2) >= tube.getPosTopTube().y + tube.getTopTube().getHeight())
                tube.reposition(tube.getPosTopTube().y + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));

            if(tube.collides(marble.getBounds())){
                marble.colliding = true;
                gameOver = true;
            }

        }

        if(marble.getPosition().x <= 0){
            marble.colliding = true;
            gameOver = true;
        }

        cam.update();

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        for(Tube tube : tubes){
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }
        sb.draw(marble.getMarble(), marble.getPosition().x, marble.getPosition().y);
        if(gameOver)
            sb.draw(gameOverImage,cam.position.x - gameOverImage.getWidth()/2, cam.position.y);
        sb.end();

    }

    @Override
    public void dispose() {
        marble.dispose();
        for(Tube tube : tubes){
            tube.dispose();
        }
    }

    //will be usefull when we got a background picture
   /* private void updateGround(){
        if(cam.position.y - (cam.viewportHeight/2) > bg1.y + bg.getHeight())
            bg1.add(0, bg.getHeight()*2);

        if(cam.position.y - (cam.viewportHeight/2) > bg2.y + bg.getHeight())
            bg2.add(0, bg.getHeight()*2);

    }*/
}