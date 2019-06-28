package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Levels.Level1;
import com.mygdx.game.MyGdxGame;


import java.util.ArrayList;

public class Start implements Screen {
    TextureRegion parts;
    Image apple;
    Texture background,main,button;
    AssetManager manager;
    boolean loading=true,start=false;
    int alpha=0;



    SpriteBatch batch;
    Stage stage,stage1;
    Sprite loadingimg;
    OrthographicCamera cam;
    StretchViewport viewport;
    ArrayList<Vector2> list;
    private InputMultiplexer inputMultiplexer;

    MyGdxGame game;
    public Start(SpriteBatch batch, AssetManager manager, MyGdxGame game){

        this.manager=manager;
        this.batch=batch;
        this.game=game;
    }
    @Override
    public void show() {
        cam = new OrthographicCamera(1280,720);
        viewport=new StretchViewport(1280,720,cam);
        cam.position.set(new Vector2(1280/2f,720/2f),1);
        cam.update();
        loadingimg=new Sprite(manager.get("loading.png",Texture.class));
        list=new ArrayList<Vector2>();
        for(int i=0; i<50; i++){
            list.add(new Vector2(100,i*64));
        }
    }
    private void createUI(){
        stage=new Stage(viewport);
        Image buttonimage=new Image(button);
        Image arcade=new Image(manager.get("buttons/arcade.png",Texture.class));
        Image settings=new Image(manager.get("buttons/settings.png",Texture.class));
        final Table table=new Table();
        table.top();
        table.add().size(300,0);
        table.add(buttonimage).size(350,70);
        table.row();
        table.add().size(300,200);
        table.add(arcade).size(350,70);
        table.add().size(10,100);
        table.row();
        table.add().size(300,0);

        table.add(settings).size(350,70);
//        table.add().size(10,100);

        table.add().size(10,170);
//        stage.setDebugAll(true);
        table.setPosition(350,0);
        table.pack();

        Table table1=new Table();
        table1.add(apple).size(100,100);
        table1.setPosition(400,450);
         stage1 =new Stage(viewport);
        stage1.addActor(table1);

        inputMultiplexer=new InputMultiplexer();

        apple.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                apple=new Image(new TextureRegion(main,0,126,59,66));
                createUI();
                return true;
            }
        });

        buttonimage.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dispos();
                game.setScreen(new Level1(batch,manager,game));
                return true;
            }
        });


        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(stage1);

        stage.addActor(table);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }
    private void dispos(){
        manager.unload("start.jpg");
        manager.unload("snake.png");
        manager.unload("buttons/startbutton.png");
        manager.unload("buttons/selectlevels.png");
    }
    private void loading(){
        start=false;
        batch.begin();
        batch.draw(loadingimg,0,0,40/2f,26/2f,40,26
                ,1,1,alpha);
        batch.end();

    }

    @Override
    public void render(float delta) {
        alpha+=10;
        cam.update();
        batch.setProjectionMatrix(cam.combined);

        if(loading){
            if (load("start.jpg")) {
                return;
            }if (load("snake.png")) {
                return;
            }if (load("buttons/startbutton.png")) {
                return;
            }if (load("buttons/selectlevels.png")) {
                return;
            }if (load("buttons/arcade.png")) {
                return;
            }if (load("buttons/settings.png")) {
                return;
            }
            loading();
        }else{

            batch.begin();
            batch.draw(background,0,0,background.getWidth(),background.getHeight());

            for(Vector2 v:list){
                if(v.y<-64){
                    v.y=720;
                }
                v.y=v.y-5;
                batch.draw(parts,v.x,v.y,100,parts.getRegionHeight());
            }
            batch.end();
            stage.act();
            stage.draw();

            stage1.act();
            stage1.draw();
        }
    }


    private void initimages(){

        background = manager.get("start.jpg", Texture.class);
        main = manager.get("snake.png", Texture.class);
        parts=new TextureRegion(main,132 ,62,53,64);
        button=manager.get("buttons/startbutton.png");
//        manager.get("buttons/selectlevels.png");
        manager.get("buttons/settings.png");
        manager.get("buttons/arcade.png");

        apple=new Image(new TextureRegion(main,0,190,59,66));
//        apple.setBounds(350,400,100,100);


    }
    private boolean loadtexture(String texture){
        try{
            manager.get(texture);
            return false;
        }catch (Exception l){
            System.out.println(l);
            manager.load(texture,Texture.class);
            manager.update();
            return true;
        }
    }
    private boolean load(String text){
        try{
            initimages();
            createUI();
            loading=false;
            manager.finishLoading();
            return true;
        }catch (Exception e){
            if(loadtexture(text)){
                return false;
            }

        }
        return false;

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
        viewport.apply();
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
