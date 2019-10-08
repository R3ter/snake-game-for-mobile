package com.mygdx.game.Levels;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.Start;

public class LevelsHandler implements Screen {
    private int level;
    private MyGdxGame game;
    private SpriteBatch batch;
    private AssetManager manager;
    public LevelsHandler(SpriteBatch batch, AssetManager manager,
                  MyGdxGame game, int level){
               this.level=level;
               this.batch=batch;
               this.game=game;
               this.manager=manager;
    }

    @Override
    public void show() {
        if(level==1){
            game.setScreen(new Level1(batch,manager,game,1));
        }else if(level==2){
            game.setScreen(new Level2(batch,manager,game,2));
        }else if(level==3){
            game.setScreen(new Level3(batch,manager,game,3));
        }else if(level==4){
            game.setScreen(new level4(batch,manager,game,4));
        }else if(level==5){
        game.setScreen(new Level5(batch,manager,game,5));
        }else {
            game.setScreen(new Start(batch,manager,game));
        }
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
