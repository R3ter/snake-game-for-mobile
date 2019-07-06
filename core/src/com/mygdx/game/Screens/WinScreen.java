package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Levels.LevelsHandler;
import com.mygdx.game.MyGdxGame;

public class WinScreen implements Screen {
    SpriteBatch batch;
    AssetManager manager;
    MyGdxGame game;
    boolean loading=true;
    Sprite loadingimg;
    Texture background;
    Texture win;
    int level;
   public WinScreen(SpriteBatch batch, AssetManager manager, MyGdxGame game,int level){
        this.game=game;
        this.manager=manager;
        this.level=level;
        this.batch=batch;
    }
    OrthographicCamera cam;
    ParticleEffect pe;
    StretchViewport viewport;
   @Override
    public void show() {
        cam = new OrthographicCamera(500,260);
       viewport=new StretchViewport(500,260,cam);
       cam.position.set(new Vector2(500/2f,260/2f),1);
        cam.update();
        loadingimg=new Sprite(manager.get("loading.png",Texture.class));
        pe=new ParticleEffect();
        pe.load(Gdx.files.internal("firework"),Gdx.files.internal(""));
        pe.setPosition(0,0);
        pe.start();

       stage=new Stage(viewport);

   }

    private boolean loadtexture(String texture, Class classname){
        try{
            manager.get(texture);
            return false;
        }catch (Exception l){
            System.out.println(l);
            manager.load(texture,classname);
            manager.update();
            return true;
        }

    }
    private void loadingfinished(){


    }
    Stage stage;

    private void initimages() {

        win=manager.get("win.png",Texture.class);
        background=(manager.get("start.jpg",Texture.class));


        Skin skin=new Skin(Gdx.files.internal("buttons/winbuttons/skin.json"),
                manager.get("buttons/winbuttons/winbuttons.atlas",TextureAtlas.class));

        ImageButton home=new ImageButton(skin);
        ImageButton next=new ImageButton(skin,"next");
        ImageButton restart=new ImageButton(skin,"restart");

        home.setBounds(100,50,40,40);
        restart.setBounds(220,50,40,40);
        next.setBounds(330,50,40,40);

        stage.addActor(home);
        stage.addActor(restart);
        stage.addActor(next);


        restart.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new LevelsHandler(batch,manager,game,level));
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        next.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new LevelsHandler(batch,manager,game,level+1));
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        home.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new Start(batch,manager,game));
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {return true;}
        });

        Gdx.input.setInputProcessor(stage);
    }
        private boolean load(String text,Class classname){
        try{
            initimages();
            loading=false;
            manager.finishLoading();
            loadingfinished();
            return true;
        }catch (Exception e){
            System.out.println(e);
            if(loadtexture(text,classname)){
                return false;
            }

        }
        return false;

    }
    private int alpha;
    private void loading(){
        alpha++;
        batch.begin();
        batch.draw(loadingimg,0,0,40/2f,26/2f,40,26
                ,1,1,alpha);
        batch.end();

    }


    @Override
    public void render(float delta) {
        cam.update();
        batch.setProjectionMatrix(cam.combined);

        if(loading){
            if(load("snake.png", Texture.class)) {
                return;
            }
            if(load("win.png", Texture.class)) {
                return;
            }if(load("loading.png", Texture.class)) {
                return;
            }if(load("start.jpg", Texture.class)) {
                return;
            }if(load("buttons/winbuttons/winbuttons.atlas",TextureAtlas.class)){
                return;
            }
            loading();
        }
        else {
            stage.act();
            pe.update(delta);
            batch.begin();
            batch.draw(background,0,0,500,260);
            pe.draw(batch);
            batch.draw(win,0,0,500,260);
            batch.end();

            if(pe.isComplete()) {
                pe.reset();
            }
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
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
