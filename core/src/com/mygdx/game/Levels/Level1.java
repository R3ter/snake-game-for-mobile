package com.mygdx.game.Levels;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.GamePlay;
import com.mygdx.game.MyGdxGame;


public class Level1 extends GamePlay {
SpriteBatch batch;
    public Level1(SpriteBatch batch, AssetManager manager, MyGdxGame game) {
        super(batch, manager, game);
        this.batch=batch;
    }

    @Override
    public void show() {
        super.show();


    }
int f=0;
    @Override
    public void render(float delta) {
        super.render(delta);
        movesnake(2);
        if(loading){
            load("image3.jpg");
        }else {
            f=f+1;
            if(f>500){
                dispos();
                game.setScreen(new Level2(batch,manager,game));
            }
        }
    }

   private void dispos(){
        manager.unload("image3.jpg");
        loading=true;
    }


    @Override
    protected void initimages() {
        super.initimages();
    }


}
