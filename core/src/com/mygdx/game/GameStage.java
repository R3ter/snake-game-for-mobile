package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Screens.GamePlay;


public class GameStage {
    private Batch batch;
    private int score;
    public Stage stage;
    private Texture img;
    private Label label;
    public GameStage(Batch batch, int score, final GamePlay main){
        this.score=score;
        this.batch=batch;
        img =new Texture(Gdx.files.internal("stage.png"));
        Texture texture=new Texture("snake.png");
        TextureRegion foodtex=new TextureRegion(texture,0 ,190,62,66);
        final Image pause=new Image(new TextureRegion(texture,73 ,126,43,57));



        Image im=new Image(img);
        stage = new Stage(new StretchViewport(500,260),batch);


//        Gdx.input.setInputProcessor(main.inputMultiplexer);

        BitmapFont font=new BitmapFont();
         label=new Label(" : "+score+"",new Label.LabelStyle(font, Color.BLACK));
        Table table=new Table();


        table.top();
        table.left();
        table.setPosition(0,250);
        table.pack();
        table.add().size(30,20);
        table.add(pause).size(20,20);
        table.add().size(150,20).right();
        table.add(new Image(foodtex)).size(20,20);
//        table.add(im);
        table.add(label);
        stage.addActor(table);
//        stage.getViewport().update(500,260, true);
        stage.setDebugAll(true);

        main.inputMultiplexer.addProcessor(stage);

        pause.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                main.stop();
                pressed(pause);
                return true;
            }
        });
    }

    void pressed(Image image){
            image.setColor(0,0,1,1);
    }
    public void drawstage(int score){
        label.setText(" : "+score);
        stage.act();
        stage.draw();
    }

}
