package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;


public class GamePlay implements Screen {
   private TextureRegion corner;
   private ArrayList<Point> array;
    private int x=0;
    private int y=0;
    private Point food;
    private int time=0;
    private ArrayList<Point> turn;
    private ArrayList<String> turns,moving;
    private Random rand;
    private int steps=5;
    private boolean dontdraw;
    private String dir="",moveto="";
    private SpriteBatch batch;
    int num=0;

    private OrthographicCamera cam;
    GamePlay(SpriteBatch batch){
        this.batch=batch;
        turn=new ArrayList<Point>();
        turns=new ArrayList<String>();
        moving=new ArrayList<String>();
        array=new ArrayList<Point>();
        for(int i=0; i<20; i++){
            array.add(new Point(x,y));
            moving.add("");
        }
    }
    private TextureRegion head,body,headup,bodyup,tail,tailup,foodtex;
    @Override
    public void show() {
        cam = new OrthographicCamera(500,250);
        cam.position.set(new Vector2(500/2f,250/2f),1);
        cam.update();

        head=new TextureRegion(new Texture("snake.png"),253 ,0,67,61);
        headup=new TextureRegion(new Texture("snake.png"),187 ,0,67,61);

        corner=new TextureRegion(new Texture("snake.png"),90 ,202,56,54);

        foodtex=new TextureRegion(new Texture("snake.png"),0 ,190,62,66);

        tailup=new TextureRegion(new Texture("snake.png"),195 ,128,56,64);
        tail=new TextureRegion(new Texture("snake.png"),259 ,128,56,64);

        body=new TextureRegion(new Texture("snake.png"),66 ,0,56,63);
        bodyup=new TextureRegion(new Texture("snake.png"),125 ,61,67,68);

        rand=new Random();
        food=new Point(rand.nextInt(25)*20,rand.nextInt(10)*20);
    }

    @Override
    public void render(float delta) {
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        batch.draw(foodtex,food.x,food.y,20,20);
        batch.end();
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
                moving.remove(0);
            }
        }
            if(x==food.x&&y==food.y){
                for(int i=0;i<5; i++){
                    array.add(new Point(-100,-100));
                    moving.add(moveto);
                }
                food=new Point(rand.nextInt(25)*20,rand.nextInt(10)*20);
            }

        if(x%20==0&&y%20==0){
            if(!moveto.equals(dir)){
                if(dir.equals("up")){
                    if(headup.isFlipY())
                    headup.flip(false,true);}
                 if(dir.equals("down")){
                    if(!headup.isFlipY())
                    headup.flip(false,true);}

                 if(dir.equals("right")){
                    if(head.isFlipX())
                    head.flip(true,false);}
                 if(dir.equals("left")){
                    if(!head.isFlipX())
                    head.flip(true,false);}


                turns.add(moveto+","+dir);
                moveto=dir;
                turn.add(new Point(x,y));
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
            moving.add(moveto);
            array.add(new Point(x, y));
        }

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
                } else if(!(i+3>array.size()&&i-3<array.size())){

                    dontdraw=false;
                    System.out.println(turn.size());
                    for(int f=0; f<turn.size(); f++){
                        if((turn.get(f).x+10>array.get(i).x&&
                                turn.get(f).x-10<array.get(i).x)&&(
                                turn.get(f).y+10>array.get(i).y&&
                                        turn.get(f).y-10<array.get(i).y
                                )){
                            System.out.println(x);
                            dontdraw=true;
                            break;
                        }
                    }

                    if(dontdraw){
                            if(turns.get(getbyvalue(i)).equals("right,down")){
                                if(!corner.isFlipX())
                                corner.flip(true,false);
                            }else if (turns.get(getbyvalue(i)).equals("down,right")){
                                if(corner.isFlipX())
                                    corner.flip(true,false);
                            }else if(turns.get(getbyvalue(i)).equals("left,up")){
                                if(!corner.isFlipY())
                                    corner.flip(false,true);
                            }else if(turns.get(getbyvalue(i)).equals("up,left")){
                                if(!corner.isFlipX())
                                    corner.flip(true,false);
                            }
//                            batch.begin();
//                                batch.draw(corner,array.get(i).x,array.get(i).y, 20, 20);
//                            batch.end();
                        }else {
                                if(moving.size()>0&&moving.get(i).equals("up")||moving.get(i).equals("down")){
                                batch.begin();
                                batch.draw(bodyup, array.get(i).x, array.get(i).y, 20, 20);
                                batch.end();
                            }else{
                            batch.begin();
                            batch.draw(body, array.get(i).x, array.get(i).y, 20, 20);
                            batch.end();
                            }
                                if(num>0)
                                num=num-1;
                            }

                        }

                }
            if(turn.size()>0&&(turn.get(0).x==array.get(0).x
            &&turn.get(0).y==array.get(0).y)){
                turn.remove(0);
            }

        time++;
    }


    private int getbyvalue(int value){
        return 0;
//        for(int i=0; i<turn.size(); i++){
//            if(value==turn.get(i)){
//                return i;
//            }
//        }
//        return 0;
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
