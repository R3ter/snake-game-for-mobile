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
import com.mygdx.game.Stages.GameStage;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.SimpleDirectionGestureDetector;
import com.mygdx.game.Stages.WinStage;


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
    private boolean hit=false;
    public static ArrayList<Vector2> rocks;
    private boolean win=false;
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
        rocks=new ArrayList<Vector2>();

    }
    private TextureRegion head,body,headup,bodyup,tail,tailup,foodtex;
    protected Texture wallimg,apple;
    protected Texture background;
    private Sprite loadingimg;
    private Texture rock;

    protected boolean loading=true;

    protected void restart(){


        dir="";
        moveto="";
        score=0;
        x=20;y=20;

        turn.clear();
        perv.clear();
        array.clear();

        for(int i=0; i<4; i++){
            array.add(new Vector2(-20,-20));
        }

        array.add(new Vector2(20,20));


    }

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
        perv=new ArrayList<Vector2>();

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


        Cotroll();
        gamestage=new GameStage(batch,2,this);
        loadingimg=new Sprite(manager.get("loading.png",Texture.class));

    }
    Animation<TextureRegion> animation;
    Animation<TextureRegion> animation2,animation3;

    protected void initimages(){

        background = manager.get("background.png",Texture.class);
        texture=manager.get("snake2.png",Texture.class);
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

        head=new TextureRegion(texture,297 ,0,203,168);
        headup=new TextureRegion(texture,24 ,291,168,203);

        corner=new TextureRegion(texture,283 ,355,203,168);

        foodtex=new TextureRegion(texture,0 ,190,62,66);

        tailup=new TextureRegion(texture,24 ,88,168,203);
        tail=new TextureRegion(texture,220 ,194,203,168);

        body=new TextureRegion(texture,260 ,194,203,168);
        bodyup=new TextureRegion(texture,24 ,42,168,203);


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
            drawfirst();
            draw();
            for(Vector2 v: rocks){
                batch.begin();
                batch.draw(rock,v.x,v.y,20,30);
                batch.end();
            }

            gamestage.drawstage(score);



            //keyboard controller

            if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                pause = !pause;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) &&
                    !moveto.equals("right") && start) {
                dir = "left";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) &&
                    !moveto.equals("left") && start) {
                dir = "right";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) &&
                    !moveto.equals("down") && start) {
                dir = "up";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) &&
                    !moveto.equals("up") && start) {
                dir = "down";
            }
            ////////////


            if ((time > 5&&!hit)&&start) {
                if (array.size() > 1) {
                    array.remove(0);
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
            }

             if (time > 5) {
                time=0;
                if(!hit){
                    if(turn.size()>0&&!array.contains(turn.get(0))){
                        turn.remove(0);
                    }
                    if(x!=array.get(array.size()-1).x&&
                            y!=array.get(array.size()-1).y){
                        turn.add(array.get(array.size()-1));
                    }
                    if (moveto.equals("right")) {
                        x = x + steps;
                    } else if (moveto.equals("up")) {
                        y = y + steps;
                    } else if (moveto.equals("left")) {
                        x = x - steps;
                    } else if (moveto.equals("down")) {
                        y = y - steps;
                    }
                    if(start){
                        array.add(new Vector2(x, y));
                        perv.add(new Vector2(x,y));
                    }
                }
                if(hit){
                    hit();
                }
                if(wall.contains(new Vector2(x,y))||
                rocks.contains(new Vector2(x,y))){

                    lose();

                    if(perv.size()<20&&!hit){
                    }else{
                        hit=true;
                    }
                }
            }


            for (int i = 0; i < array.size(); i++) {
                    if ((x == array.get(i).x && y == array.get(i).y && (i != array.size() - 1)) &&
                            !moveto.equals(""))
                        pause = true;
                }
                if (x == food.x && y == food.y) {
                    for (int i = 0; i < grow; i++)
                        array.add(0, new Vector2(-20, -20));
                    newfood();
                    score = score + 1;
                }
                if (array.contains(food)||wall.contains(food)||rocks.contains(food)) {
                    newfood();
                }

                if(perv.size()>array.size()+300){
                    perv.remove(0);
                }
        }


    }
    protected void newfood(){
        food = new Vector2((rand.nextInt(23) + 1) * 20, (rand.nextInt(8) + 1) * 20);
    }
    private int timer=0;
    protected void lose(){

        restart();
    }
    protected  void  hit(){
    timer++;

        if(timer>1) {
            timer=0;

            num++;
            if (num < 15) {
                for (int f = 1 ; f <= array.size() ; f++) {
                    if(perv.size()>f){
                        Vector2 x =perv.get(perv.size()-f);
                        array.set(array.size()-f,x);
                    }else {
                        perv.clear();
                        num=0;
                        hit=false;
                        y = (int) array.get(array.size() - 1).y;
                        x = (int) array.get(array.size() - 1).x;
                    }
                }
                if (array.get(array.size() - 1).y > array.get(array.size() - 2).y) {
                    moveto = "up";
                    dir = "up";
                } else if (array.get(array.size() - 1).y < array.get(array.size() - 2).y) {
                    moveto = "down";
                    dir = "down";
                } else if (array.get(array.size() - 1).x > array.get(array.size() - 2).x) {
                    moveto = "right";
                    dir = "right";
                } else {
                    moveto = "left";
                    dir = "left";
                }
                    if(perv.size()>0)
                    perv.remove(perv.size()-1);
            } else {
                num = 0;
                hit = false;
                y = (int) array.get(array.size() - 1).y;
                x = (int) array.get(array.size() - 1).x;
                perv.clear();
            }
        }

    }

    protected void movesnake(float f){
        if(!pause&&start)
            time=time+f;

    }

    private float timers;
    private float an;
    private boolean finished=false;
    protected void drawapple(){
        if(x+40>food.x&&x-40<food.x&&
                y+40>food.y&&y-40<food.y){
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
    private void draw(){
        an += Gdx.graphics.getDeltaTime();
        timers++;

        drawapple();

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
                    if((turn.get(f).x+6>array.get(i).x&&
                            turn.get(f).x-6<array.get(i).x)&&(
                            turn.get(f).y+6>array.get(i).y&&
                                    turn.get(f).y-6<array.get(i).y
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
