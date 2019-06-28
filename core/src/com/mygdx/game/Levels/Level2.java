package com.mygdx.game.Levels;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.GamePlay;
import com.mygdx.game.MyGdxGame;

public class Level2 extends GamePlay {
    public Level2(SpriteBatch batch, AssetManager manager, MyGdxGame game){
        super(batch,manager,game);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if(loading){
            load("image.jpg");
            load("image3.jpg");
            load("image2.jpg");
        }else{
            movesnake(2);
        }
    }

    @Override
    protected void initimages() {
        super.initimages();
        manager.get("image3.jpg");
        manager.get("image2.jpg");
    }
}
