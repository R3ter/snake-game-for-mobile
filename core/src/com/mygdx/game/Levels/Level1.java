package com.mygdx.game.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Screens.GamePlay;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.WinScreen;



public class Level1 extends GamePlay {
    private SpriteBatch batch;
    private int level;
    public Level1(SpriteBatch batch, AssetManager manager, MyGdxGame game,int level) {
        super(batch, manager, game);
        this.level=level;
        this.batch=batch;
    }
    private Stage stage;
    private StretchViewport viewport;
    @Override
    public void show() {
        super.show();
        steps=5;

        viewport=new StretchViewport(1400,400);
        stage=new Stage(viewport);

        viewport.apply();
        inputMultiplexer.addProcessor(stage);
    }

    private void dialog(){
        Skin skin=new Skin(Gdx.files.internal("buttons/dialog/skin.json"),
                manager.get("buttons/dialog/winbuttons.atlas",TextureAtlas.class));
        Dialog dialog=new Dialog("title",skin){
            @Override
            protected void result(Object object) {
                super.result(object);
                start=true;
            }
        };
        ImageButton button=new ImageButton(skin);
        dialog.setBounds(0,0,500,250);
        button.setSize(1,1);
        dialog.add().height(20);
        dialog.row();
        Label label=new Label("eat 10 apples",skin);

        dialog.text(label).pad(100);
        dialog.button(button);
        dialog.getButtonTable().getCells().first().height(80).width(80);
        dialog.pack();
        dialog.show(stage);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        movesnake(2);
        if(loading){
            if(load("buttons/dialog/winbuttons.atlas", TextureAtlas.class)){
                return;
            }
        }else {
            stage.act(delta);
            stage.draw();
            if(score>10){
                game.setScreen(new WinScreen(batch,manager,game,level));
            }
        }
    }

//   private void dispos(){
//        manager.unload("image3.jpg");
//        loading=true;
//    }

    @Override
    protected void drawapple() {
        super.drawapple();
    }

    @Override
    protected void initimages() {
        super.initimages();
        dialog();
    }


    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width,height);
    }
}
