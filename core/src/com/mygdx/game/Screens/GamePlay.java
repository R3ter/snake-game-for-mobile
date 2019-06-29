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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameStage;
import com.mygdx.game.Levels.Level1;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.SimpleDirectionGestureDetector;


import java.util.ArrayList;
import java.util.Random;


public class GamePlay implements Screen {
    private TextureRegion corner;
    private ArrayList<Vector2> array;
    protected int x=20;
    protected int y=20;
    private Vector2 food;
    protected float time=0;
    private ArrayList<Vector2> turn;
    protected ArrayList<Vector2> wall;
    private Random rand;
    protected int steps=10,score,grow=2;
    protected boolean pause=false;
    private boolean dontdraw;
    private String dir="",moveto="";
    protected boolean start=false;
    private Texture texture;
    public InputMultiplexer inputMultiplexer;
    private GameStage gamestage;
    protected OrthographicCamera cam;
    protected boolean drawbackground=true;
    protected AssetManager manager;
    protected SpriteBatch batch;
    int level;
    protected MyGdxGame game;
   public GamePlay(SpriteBatch batch, AssetManager manager,MyGdxGame game){
       super();
       this.level=level;
        this.game=game;
        this.manager=manager;
        this.batch=batch;


    }
    private TextureRegion head,body,headup,bodyup,tail,tailup,foodtex;
    protected Texture wallimg;
    protected Texture background;
    private Sprite loadingimg;
    protected boolean loading=true;


    @Override
    public void show() {
        loading=true;
        start=false;
        dir="";
        moveto="";
        score=0;

        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));


        turn=new ArrayList<Vector2>();
        wall=new ArrayList<Vector2>();

        array=new ArrayList<Vector2>();

        for(int i=0; i<4; i++){
            array.add(new Vector2(-10,-10));
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


        Cotroll();
        gamestage=new GameStage(batch,2,this);
        loadingimg=new Sprite(manager.get("loading.png",Texture.class));

    }

    protected void initimages(){

        background = manager.get("background.png",Texture.class);
        texture=manager.get("snake.png",Texture.class);
        head=new TextureRegion(texture,253 ,0,67,61);
        headup=new TextureRegion(texture,187 ,0,67,61);
        corner=new TextureRegion(texture,90 ,202,56,54);
        foodtex=new TextureRegion(texture,0 ,190,62,66);
        tailup=new TextureRegion(texture,195 ,128,56,64);
        tail=new TextureRegion(texture,259 ,128,56,64);
        body=new TextureRegion(texture,66 ,0,56,63);
        bodyup=new TextureRegion(texture,125 ,61,67,68);


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

    }
    protected boolean load(String text,Class classname){
        try{
            initimages();
            loading=false;
            manager.finishLoading();
            loadingfinished();
            return true;
        }catch (Exception e){
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
    }
    @Override
    public void render(float delta) {
        alpha=7+alpha;
        cam.update();
        batch.setProjectionMatrix(cam.combined);

        if(loading){
            if(load("snake.png",Texture.class)) {
                return;
            }
            if (load("background.png",Texture.class)) {
                return;
            }
            loading();
        }
        else {
            drawfirst();
            draw();
            gamestage.drawstage(score);
//            pause=false;
            start = true;


            for (Vector2 v : wall) {
                if (v.x == x && v.y == y &&
                        !moveto.equals(""))
                    pause = true;
            }
            for (int i = 0; i < array.size(); i++) {
                if ((x == array.get(i).x && y == array.get(i).y && (i != array.size() - 1)) &&
                        !moveto.equals(""))
                    pause = true;
            }


            //keyboard controller
            if(dir.equals("")&&Gdx.input.justTouched()){
                dir="right";
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                pause = !pause;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) &&
                    !dir.equals("right") && start) {
                dir = "left";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) &&
                    !dir.equals("left") && start) {
                dir = "right";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) &&
                    !dir.equals("down") && start) {
                dir = "up";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) &&
                    !dir.equals("up") && start) {
                dir = "down";
            }
            ////////////


            if (time > 5) {
                if (array.size() > 1) {
                    array.remove(0);

                }
            }
            if (x % 20 == 0 && y % 20 == 0) {
                if (!moveto.equals(dir)) {
                    if (dir.equals("up")) {
                        if (headup.isFlipY()) {
                            headup.flip(false, true);
                        }
                    }
                    if (dir.equals("down")) {
                        if (!headup.isFlipY()) {
                            headup.flip(false, true);
                        }
                    }

                    if (dir.equals("right")) {
                        if (head.isFlipX()) {
                            head.flip(true, false);
                        }
                    }
                    if (dir.equals("left")) {
                        if (!head.isFlipX()) {
                            head.flip(true, false);
                        }
                    }
                    if (time > 5) {
                        moveto = dir;
                        turn.add(new Vector2(x, y));

                    }
                }
            }

            if (time > 5) {
                if (moveto.equals("right")) {
                    x = x + steps;
                } else if (moveto.equals("up")) {
                    y = y + steps;
                } else if (moveto.equals("left")) {
                    x = x - steps;
                } else if (moveto.equals("down")) {
                    y = y - steps;
                }
                time = 0;
                array.add(new Vector2(x, y));

//                if (turn.size() > 0 && (turn.get(0).x == array.get(0).x
//                        && turn.get(0).y == array.get(0).y)) {
//                    turn.remove(0);
//                }


                if(turn.size()>0&&!array.contains(turn.get(0))){
                    turn.remove(0);
                }


            }

            if (x == food.x && y == food.y) {
                for (int i = 0; i < grow; i++)
                    array.add(0, new Vector2(-20, -20));
                food = new Vector2((rand.nextInt(23) + 1) * 20, (rand.nextInt(8) + 1) * 20);
                score = score + 1;
            }
            if (array.contains(food)||wall.contains(food))
                food = new Vector2((rand.nextInt(23) + 1) * 20, (rand.nextInt(8) + 1) * 20);
        }
    }

    protected void movesnake(float f){
        if(!pause&&start)
            time=time+f;

    }

    private void draw(){
        batch.begin();
        batch.draw(foodtex,food.x,food.y,20,20);
        batch.end();

        for(int i=0; i<turn.size(); i++){
            batch.begin();
            batch.draw(corner,turn.get(i).x,turn.get(i).y, 20, 20);
            batch.end();
        }
        for(int i=0; i<array.size(); i++) {

            if (i == array.size()-1) {
                batch.begin();
                if(moveto.equals("up")||moveto.equals("down"))
                    batch.draw(headup, array.get(i).x, array.get(i).y, 20, 20);
                else
                    batch.draw(head, array.get(i).x, array.get(i).y, 20, 20);
                batch.end();
            }else if(i==0){
                batch.begin();

                if(array.get(0).x<array.get(1).x){
                    if(tail.isFlipX())
                        tail.flip(true,false);
                    batch.draw(tail, array.get(i).x, array.get(i).y, 20, 20);
                }
                else if(array.get(0).x>array.get(1).x){
                    if(!tail.isFlipX())
                        tail.flip(true,false);
                    batch.draw(tail, array.get(i).x, array.get(i).y, 20, 20);
                }
                else if(array.get(0).y<array.get(1).y){
                    if(tailup.isFlipY())
                        tailup.flip(false,true);
                    batch.draw(tailup, array.get(i).x, array.get(i).y, 20, 20);
                }
                else{
                    if(!tailup.isFlipY())
                        tailup.flip(false,true);
                    batch.draw(tailup, array.get(i).x, array.get(i).y, 20, 20);
                }
                batch.end();
            } else {
//                    if(!(i+3>array.size()&&i-3<array.size())){

                dontdraw=false;
                for(int f=0; f<turn.size(); f++){
                    if((turn.get(f).x+10>array.get(i).x&&
                            turn.get(f).x-10<array.get(i).x)&&(
                            turn.get(f).y+10>array.get(i).y&&
                                    turn.get(f).y-10<array.get(i).y
                    )){
                        dontdraw=true;
                        break;
                    }
                }
                if(steps<=5&&i+3>array.size()&&i-1<array.size()){
                    dontdraw=true;
                }
                if(!dontdraw) {
                    if(array.get(i).x==array.get(i+1).x){
                        batch.begin();
                        batch.draw(bodyup, array.get(i).x, array.get(i).y, 20, 20);
                        batch.end();
                    }else{
                        batch.begin();
                        batch.draw(body, array.get(i).x, array.get(i).y, 20, 20);
                        batch.end();
                    }
                }
            }
        }

        for(Vector2 i :wall){
            batch.begin();
            batch.draw(wallimg,i.x,i.y,20,20);
            batch.end();
        }
    }

    public void stop(){
        pause=!pause;
    }
    private void Cotroll(){
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {
            @Override
            public void onLeft() {
            if(!dir.equals("right")&&start){
                dir="left";
            }
            }

            @Override
            public void onRight() {
                if(!dir.equals("left")&&start){
                    dir="right";
            }}

            @Override
            public void onUp() {
                    if(!dir.equals("down")&&start){
                        dir="up";
            }}

            @Override
            public void onDown() {
                    if(!dir.equals("up")&&start){
                        dir="down";
            }}

        }));

                Gdx.input.setInputProcessor(inputMultiplexer);

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
