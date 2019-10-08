package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Player.SnakePlayer;
import com.mygdx.game.Stages.GameStage;
import com.mygdx.game.MyGdxGame;


import java.util.ArrayList;
import java.util.Random;


public class GamePlay implements Screen {
    private TextureRegion corner;
    protected ArrayList<Vector2> array,perv;
    protected int x=20;
    protected int y=20;
    public static Vector2 food;
    protected float time=0;
    private ArrayList<Vector2> turn;
    public static ArrayList<Vector2> wall;
    private Random rand;
    protected float steps=10;
    public static float power=2;
    protected int score,grow=2;
    private boolean pause=false;
    protected boolean start=false;
    private Texture texture;
    private GameStage gamestage;
    protected OrthographicCamera cam;
    protected boolean drawbackground=true;
    private boolean hit=false;
    public static ArrayList<Vector2> rocks;
    private boolean win=false;
    protected AssetManager manager;
    protected SpriteBatch batch;
    private SnakePlayer snake;
    public InputMultiplexer inputMultiplexer;

    int level;
    protected MyGdxGame game;
   public GamePlay(SpriteBatch batch,AssetManager manager,MyGdxGame game){
       super();
       this.level=level;
        this.game=game;
        this.manager=manager;
        this.batch=batch;
        rocks=new ArrayList<Vector2>();
       inputMultiplexer = new InputMultiplexer();
       Gdx.input.setInputProcessor(inputMultiplexer);


   }
    private TextureRegion foodtex;
    protected Texture wallimg,apple;
    protected Texture background;
    private Sprite loadingimg;
    private Texture rock;

    protected boolean loading=true;

    protected void restart(){



        score=0;
        x=20;y=20;

        turn.clear();
//        perv.clear();
        array.clear();

        for(int i=0; i<4; i++){
            array.add(new Vector2(-20,-20));
        }



    }

    @Override
    public void show() {
        loading=true;
        start=false;

        score=0;


        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));


        turn=new ArrayList<Vector2>();
        wall=new ArrayList<Vector2>();


        array=new ArrayList<Vector2>();

        for(int i=0; i<4; i++){
            array.add(new Vector2(-20,-20));
        }

        cam = new OrthographicCamera(500,260);
        cam.position.set(new Vector2(500/2f,260/2f),1);
        cam.update();





        for (int i=0; i<26; i++){
            wall.add(new Vector2(i*20,0));
            wall.add(new Vector2(i*20,220));
            wall.add(new Vector2(i*20,240));
        }
        for (int i=0; i<13; i++) {
            wall.add(new Vector2(0,20*i));
            wall.add(new Vector2(480,20*i));
        }

        Pixmap pixel=new Pixmap(20,20, Pixmap.Format.RGB565);
        pixel.setColor(0,.6f,.4f,1f);
        pixel.fillRectangle(0,0,20,20);

        wallimg = new Texture(pixel);


        rand=new Random();
        food=new Vector2((rand.nextInt(23)+1)*20,(rand.nextInt(8)+1)*20);



        gamestage=new GameStage(batch,2,this);
        loadingimg=new Sprite(manager.get("loading.png",Texture.class));

    }
    Animation<TextureRegion> animation;
    Animation<TextureRegion> animation2,animation3;

    protected void initimages(){

        texture=manager.get("snake2.png",Texture.class);
        background = manager.get("background.png",Texture.class);
        apple=manager.get("apple/newapple/apple.png",Texture.class);

       TextureAtlas textureAtlas = manager.get("apple/newapple/apple.atlas");

        TextureAtlas textureRegions=manager.get("apple/newapple/applestill.atlas");

        rock=manager.get("objects/rock.png");


        animation = new Animation<TextureRegion>(5/15f, textureAtlas.getRegions());
        animation2 = new Animation<TextureRegion>(5/15f,textureRegions.getRegions());

        Array<TextureRegion> textureRegions1=new Array<TextureRegion>();
        textureRegions1.add(textureAtlas.getRegions().get(textureAtlas.getRegions().size-1));
        textureRegions1.add(textureAtlas.getRegions().get(textureAtlas.getRegions().size-2));

        animation3 = new Animation<TextureRegion>(5/15f,textureRegions1);

        foodtex=new TextureRegion(texture,0 ,190,62,66);



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
    protected void loadingfinished(){
        snake=new SnakePlayer(batch,manager,this,inputMultiplexer,steps);
    }
    protected boolean load(String text,Class classname){
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
    private void loading(){
        start=false;
        batch.begin();
        batch.draw(loadingimg,0,0,40/2f,26/2f,40,26
                ,1,1,alpha);
        batch.end();

    }
    private float alpha;

    protected void drawfirst(){
        if(drawbackground){
            batch.begin();
            batch.draw(background,0,0);
            batch.end();
        }
        drawwalls();
    }
    int num=0;
    @Override
    public void render(float delta) {
        alpha=7+alpha;
        cam.update();

        batch.setProjectionMatrix(cam.combined);
        if(loading){
            if(load("snake.png",Texture.class)) {
                return;
            } if(load("snake2.png",Texture.class)) {
                return;
            }
            if (load("background.png",Texture.class)) {
                return;
            }if (load("apple/newapple/apple.atlas",TextureAtlas.class)) {
                return;
            }
            if (load("apple/newapple/applestill.atlas",TextureAtlas.class)) {
                return;
            }if(load("objects/rock.png",Texture.class)){
                return;
            }

            loading();
        }
        else {
            snake.render(start,pause,hit,grow);
            drawfirst();
            drawapple();

            keepsnakeinrange();

            snake.draw();
            for(Vector2 v: rocks){
                batch.begin();
                batch.draw(rock,v.x,v.y,20,30);
                batch.end();
                if (snake.x < v.x + 20 &&
                        snake.x + 20 > v.x &&
                        snake.y < v.y + 10 &&
                        snake.y + 20 > v.y) {
                    pause=true;
                }
            }

            gamestage.drawstage(score);



            //keyboard controller

            if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                pause = !pause;
            }

            ////////////



        }


    }
    public void eat(){
        newfood();
        score=score+1;
    }
    protected void newfood(){
        food = new Vector2((rand.nextInt(23) + 1) * 20, (rand.nextInt(8) + 1) * 20);
        if(wall.contains(food)||rocks.contains(food)||
        snake.array.contains(food)){
            newfood();
        }


    }

    public void lose(){

        restart();
    }




    private float timers;
    private float an=1;
    private boolean finished=false;
    protected void drawapple(){
        an += Gdx.graphics.getDeltaTime();
        timers++;
        if(snake.x+40>food.x&&snake.x-40<food.x&&
                snake.y+40>food.y&&snake.y-40<food.y){
            if(finished){
                batch.begin();
                batch.draw(animation3.getKeyFrame(an,true),food.x,food.y,20,20);
                batch.end();
            }else{
                batch.begin();
                batch.draw(animation.getKeyFrame(an/2,false),food.x,food.y,20,20);
                batch.end();
            }
            if(animation.isAnimationFinished(an/2)){
                finished=true;

            }
        }else{
            finished=false;
            if(timers>500){
                batch.begin();
                batch.draw(animation2.getKeyFrame(an/2,false),food.x,food.y,20,20);
                batch.end();
                if(animation2.isAnimationFinished(an/2)){
                    if(timers>550){
                        timers=0;
                        an=0;
                    }
                }
            }else{
                batch.begin();
                batch.draw(animation2.getKeyFrame(0,false),food.x,food.y,20,20);
                batch.end();
            }

        }
    }

    private void drawwalls(){
        for(Vector2 i :wall){
            batch.begin();
            batch.draw(wallimg,i.x,i.y,20,20);
            batch.end();
        }
    }

    public void stop(){
        pause=!pause;
    }


    protected void keepsnakeinrange(){
        if(snake.x>500){
            snake.x=-20;
        }
        if(snake.y>270){
            snake.y=-20;
        }
        if(snake.x<-20){
            snake.x=500;
        }
        if(snake.y<-20){
            snake.y=500/2f;
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        pause=true;
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        pause=true;
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
