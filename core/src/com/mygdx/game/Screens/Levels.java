package com.mygdx.game.Screens;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Levels.LevelsHandler;
import com.mygdx.game.MyGdxGame;



public class Levels implements Screen {

        private AssetManager manager;
        private SpriteBatch batch;
        private MyGdxGame game;
        Stage stage;

        OrthographicCamera cam;
        StretchViewport viewport;
        boolean loading=true;
        Sprite loadingimg;

    public  Levels(SpriteBatch batch, AssetManager manager, MyGdxGame game){
            this.manager=manager;
            this.batch=batch;
            this.game=game;
    }
    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        cam = new OrthographicCamera(1280,720);
        viewport=new StretchViewport(1280,720,cam);
        cam.position.set(new Vector2(1280/2f,720/2f),1);
        cam.update();

        loadingimg=new Sprite(manager.get("loading.png", Texture.class));

    }

    private void createUI(){
        Skin skin=new Skin(Gdx.files.internal("fonts/skin.json"),
                manager.get("buttons/newbuttons/buttons.atlas", TextureAtlas.class));

        int max=0;
        for(int i=1; i<5; i++){
            for(int f=1; f<5; f++){
                max++;
                final int level=max;
                skin.getFont("medium").getData().setScale(.5f);
//                ImageButton start=new ImageButton(skin,"default");
                TextButton start=new TextButton(max+"",skin);
                start.setBounds(f*100,(i*70)+100,100,70);
                start.addListener(new InputListener(){
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        game.setScreen(new LevelsHandler(batch,manager,game,level));
                        return true;
                    }
                });
                stage.addActor(start);
            }
        }
    }
int alpha=0;
    @Override
    public void render(float delta) {
        alpha+=10;
        cam.update();
        batch.setProjectionMatrix(cam.combined);

        if(loading){
            if (load("start.jpg",Texture.class)) {
                return;
            }if (load("snake.png",Texture.class)) {
                return;
            }if (load("buttons/startbutton.png",Texture.class)) {
                return;
            }if (load("buttons/selectlevels.png",Texture.class)) {
                return;
            }if (load("buttons/arcade.png",Texture.class)) {
                return;
            }if (load("buttons/settings.png",Texture.class)) {
                return;
            }if (load("buttons/newbuttons/buttons.atlas",TextureAtlas.class)) {
                return;
            }
            loading();
        }else{


            stage.act();
            stage.draw();

    }
    }
    private void dispos(){

    }
    private void loading(){
        batch.begin();
        batch.draw(loadingimg,0,0,40/2f,26/2f,40,26
                ,2f,2f,alpha);
        batch.end();

    }
    private void initimages(){

        manager.get("buttons/newbuttons/buttons.atlas");


    }
    private boolean loadtexture(String texture,Class classname){
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
    private boolean load(String text,Class classname){
        try{
            initimages();
            createUI();
            loading=false;
            manager.finishLoading();
            return true;
        }catch (Exception e){
            if(loadtexture(text,classname)){
                return false;
            }

        }
        return false;

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
