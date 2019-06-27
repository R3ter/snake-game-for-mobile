package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.desktop.SimpleDirectionGestureDetector;
import java.util.ArrayList;
import java.util.Random;


public class GamePlay implements Screen {
   private TextureRegion corner;
   private ArrayList<Vector2> array;
    private int x=20;
    private int y=20;
    private Vector2 food;
    private int time=0;
    private ArrayList<Vector2> turn,wall;
    private ArrayList<String> turns;
    private Random rand;
    private int steps=10;
    private boolean pause=false;
    private boolean dontdraw;
    private String dir="",moveto="";
    private SpriteBatch batch;


    private OrthographicCamera cam;
    GamePlay(SpriteBatch batch){
        this.batch=batch;
        turn=new ArrayList<Vector2>();
        turns=new ArrayList<String>();

            wall=new ArrayList<Vector2>();

        array=new ArrayList<Vector2>();
        for(int i=0; i<20; i++){
//            array.add(new Vector2(-10,-10));

        }
    }
    private TextureRegion head,body,headup,bodyup,tail,tailup,foodtex;
    private Texture background,wallimg;
    @Override
    public void show() {

        cam = new OrthographicCamera(500,260);
        cam.position.set(new Vector2(500/2f,260/2f),1);
        cam.update();


        for (int i=0; i<26; i++){
            wall.add(new Vector2(i*20,0));
            wall.add(new Vector2(i*20,240));
        }
        for (int i=0; i<13; i++) {
            wall.add(new Vector2(0,20*i));
            wall.add(new Vector2(480,20*i));
        }

        background = new Texture("background.png");
        head=new TextureRegion(new Texture("snake.png"),253 ,0,67,61);
        headup=new TextureRegion(new Texture("snake.png"),187 ,0,67,61);

        corner=new TextureRegion(new Texture("snake.png"),90 ,202,56,54);

        foodtex=new TextureRegion(new Texture("snake.png"),0 ,190,62,66);

        tailup=new TextureRegion(new Texture("snake.png"),195 ,128,56,64);
        tail=new TextureRegion(new Texture("snake.png"),259 ,128,56,64);

        body=new TextureRegion(new Texture("snake.png"),66 ,0,56,63);
        bodyup=new TextureRegion(new Texture("snake.png"),125 ,61,67,68);


        Pixmap pixel=new Pixmap(20,20, Pixmap.Format.RGB565);
        pixel.setColor(0,.5f,0,1);
        pixel.fillRectangle(0,0,20,20);


        wallimg = new Texture(pixel);


        rand=new Random();
        food=new Vector2((rand.nextInt(23)+1)*20,(rand.nextInt(8)+1)*20);

        Cotroll();
    }

    @Override
    public void render(float delta) {
        cam.update();
        batch.setProjectionMatrix(cam.combined);

        draw();

        for (Vector2 v:wall){
            if(v.x==x&&v.y==y)
                System.out.println("loser");
        }
        for(int i=0; i<array.size(); i++){
            if((x==array.get(i).x&&y==array.get(i).y&&(i!=array.size()-1)))
                System.out.println("loser");
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
           pause=!pause;
        }
            if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)&&
        !dir.equals("right")){
            dir="left";
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)&&
                !dir.equals("left")){
            dir="right";
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.UP) &&
                !dir.equals("down")){
            dir="up";
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)&&
                !dir.equals("up")){
            dir="down";
        }


        if(time>5) {
            if (array.size() > 1) {
                array.remove(0);

            }
        }
        if(x%20==0&&y%20==0){
            if(!moveto.equals(dir)){
                if(dir.equals("up")){
                    if(headup.isFlipY()) {
                        headup.flip(false,true);
                    }
                }
                if(dir.equals("down")){
                    if(!headup.isFlipY()) {
                        headup.flip(false,true);
                    }
                }

                if(dir.equals("right")){
                    if(head.isFlipX()) {
                        head.flip(true,false);
                    }
                }
                if(dir.equals("left")){
                    if(!head.isFlipX()) {
                        head.flip(true,false);
                    }
                }
                if(time>5){
                    turns.add(moveto+","+dir);
                    moveto=dir;
                    turn.add(new Vector2(x,y));

                }


            }
        }

        if(time>5) {
            if (moveto.equals("right")) {
                x=x+steps;
            } else if (moveto.equals("up")) {
                y = y + steps;
            } else if (moveto.equals("left")) {
                x = x - steps;
            } else if (moveto.equals("down")) {
                y = y - steps;
            }
            time=0;
            array.add(new Vector2(x, y));

            if(turn.size()>0&&(turn.get(0).x==array.get(0).x
                    &&turn.get(0).y==array.get(0).y)){
                turn.remove(0);
            }
        }




        if(x==food.x&&y==food.y){

            for(int i=0;i<3; i++){
            array.add(0,new Vector2(0,0));
            }
            food=new Vector2((rand.nextInt(23)+1)*20,(rand.nextInt(8)+1)*20);
        }
        if(!pause)
        time++;
    }


    private void draw(){
        batch.begin();
        batch.draw(background,0,0);
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
    private void Cotroll(){

        Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {
            @Override
            public void onLeft() {
            if(!dir.equals("right")){
                dir="left";
            }
            }

            @Override
            public void onRight() {
                if(!dir.equals("left")){
                    dir="right";
            }}

            @Override
            public void onUp() {
                    if(!dir.equals("down")){
                        dir="up";
            }}

            @Override
            public void onDown() {
                    if(!dir.equals("up")){
                        dir="down";
            }}
            }));
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
