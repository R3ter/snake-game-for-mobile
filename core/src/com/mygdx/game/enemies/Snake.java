package com.mygdx.game.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.Random;

import static com.mygdx.game.Screens.GamePlay.food;
import static com.mygdx.game.Screens.GamePlay.rocks;
import static com.mygdx.game.Screens.GamePlay.wall;

public class Snake {
    public ArrayList<Vector2> array;
    public boolean die=false;
    private int x=480/3,y=240/2;
    private int steps=5;
    private String moveto="left";
    private ArrayList<Vector2> turn;
    private boolean dontdraw;
    private SpriteBatch batch;
    private String dir;
    private Texture snake;
    private Random rand;
    private ArrayList herosnake;
    private TextureRegion head,headup,corner,tailup,tail,body,bodyup;
    private AssetManager manager;
    public Snake(SpriteBatch batch, AssetManager manager,ArrayList herosnake) {
        this.batch=batch;
        this.manager=manager;
        this.herosnake=herosnake;
        array=new ArrayList<Vector2>();
        for(int i=0; i<5; i++){
            array.add(new Vector2(-480,-240));
        }
        rand=new Random();
        turn=new ArrayList<Vector2>();
        dir="left";
    }
    public void init(){
        snake=manager.get("snake2.png",Texture.class);

        head=new TextureRegion(snake,297 ,0,203,168);
        headup=new TextureRegion(snake,24 ,291,168,203);

        corner=new TextureRegion(snake,283 ,355,203,168);

        tailup=new TextureRegion(snake,24 ,88,168,203);
        tail=new TextureRegion(snake,220 ,194,203,168);

        body=new TextureRegion(snake,260 ,194,203,168);
        bodyup=new TextureRegion(snake,24 ,42,168,203);
    }
    public void load(){
        manager.load("snake2.png",Texture.class);
    }
    private float time;
    private void newfood(){
        food=new Vector2((rand.nextInt(23)+1)*20,(rand.nextInt(8)+1)*20);

    }
    public void render(){
        System.out.println(Gdx.graphics.getFramesPerSecond());
        time++;
        draw();
        if (time > 5) {
            if(!die)
                move();
                for(int i=0; i<array.size(); i++){
                    if(i!=array.size()-1&&(array.get(i).equals(new Vector2(x,y)))){
                        die();
                    }
                }
                if(wall.contains(new Vector2(x,y))||rocks.contains(new Vector2(x,y))){
                    die();
                }
                if(herosnake.contains(new Vector2(x,y))){
                    die();
                }
            if(new Vector2(x,y).equals(food)) {
                newfood();
                for (int i=0; i<4; i++)
                    array.add(0,new Vector2(-200,0));
            }
            }
        if(array.contains(food)){
            newfood();
        }
        controll();
        dodge();
        if(x%20==0&&y%20==0){
            changemove();
        }

    }
    private void face(){
        if(array.get(0).x<array.get(1).x){
            if(tail.isFlipX())
                tail.flip(true,false);
        }
        else if(array.get(0).x>array.get(1).x){
            if(!tail.isFlipX())
                tail.flip(true,false);
        }
        else if(array.get(0).y<array.get(1).y){
            if(tailup.isFlipY())
                tailup.flip(false,true);
        }
        else{
            if(!tailup.isFlipY())
                tailup.flip(false,true);
        }

        if (moveto.equals("up")) {
            if (headup.isFlipY()) {
                headup.flip(false, true);
            }
        }
        if (moveto.equals("down")) {
            if (!headup.isFlipY()) {
                headup.flip(false, true);
            }
        }

        if (moveto.equals("right")) {
            if (head.isFlipX()) {
                head.flip(true, false);
            }
        }
        if (moveto.equals("left")) {
            if (!head.isFlipX()) {
                head.flip(true, false);
            }}
    }
    private void changemove(){
        if(!dir.equals(moveto)){
            moveto=dir;
            turn.add(new Vector2(x,y));
        }
    }
    private void die(){
//        die=true;
        array.clear();
        x=460;
        y=60;
        moveto="";
        dir="left";
        for(int i=0; i<4; i++){
            array.add(new Vector2(500,200));
        }
    }
    private void controll(){
       if(array.get(array.size()-1).y!=array.get(array.size()-2).y){
            if(x<food.x){
                if(!array.contains(new Vector2(x+20,y)))
                dir="right";
            }else if(x>food.x){
                if(!array.contains(new Vector2(x-20,y)))
                dir="left";
            }
        }else if(array.get(array.size()-1).x!=array.get(array.size()-2).x) {
            if(y>food.y){
                if(!array.contains(new Vector2(x,y-20)))
                    dir="down";
            }else if(y<food.y){
                if(!array.contains(new Vector2(x,y+20)))
                dir="up";
            }
        }
    }
    private void dodge(){
        if (moveto.equals("up")&&(array.contains(new Vector2(x,y+20))||
                rocks.contains(new Vector2(x,y+20))||
                wall.contains(new Vector2(x,y+20))||
                herosnake.contains(new Vector2(x,y+20)))){
            dir="right";
        }
        if (moveto.equals("down")&&(array.contains(new Vector2(x,y-20))||
                rocks.contains(new Vector2(x,y-20))||
                wall.contains(new Vector2(x,y-20))||
                herosnake.contains(new Vector2(x,y-20)))){
                dir="left";
        }
        if (moveto.equals("left")&&(array.contains(new Vector2(x-20,y))||
                rocks.contains(new Vector2(x-20,y))||
                wall.contains(new Vector2(x-20,y))||
                herosnake.contains(new Vector2(x-20,y)))){
            dir="up";
        }
        if (moveto.equals("right")&&(array.contains(new Vector2(x+20,y))||
                rocks.contains(new Vector2(x+20,y))||
                wall.contains(new Vector2(x+20,y))||
                herosnake.contains(new Vector2(x+20,y)))) {
            dir="down";
        }
        }
    private void draw(){
        face();
           for(int i=0; i<array.size(); i++){
               if(i==array.size()-1){
                   if(i>0&&array.get(i).y!=array.get(i-1).y){
                       batch.begin();
                       batch.draw(headup,array.get(i).x,array.get(i).y,20,20);
                       batch.end();
                   }else{
                       batch.begin();
                       batch.draw(head,array.get(i).x,array.get(i).y,20,20);
                       batch.end();
                   }
               }else if(i==0){
                   if(array.get(0).y!=array.get(1).y){
                       batch.begin();
                       batch.draw(tailup,array.get(i).x,array.get(i).y,20,20);
                       batch.end();
                   }else{
                       batch.begin();
                       batch.draw(tail,array.get(i).x,array.get(i).y,20,20);
                       batch.end();
                   }
               }else{
                   if(turn.contains(array.get(i))){
                       continue;
                   }
                   if(i>0&&array.get(i).y!=array.get(i-1).y){
                       batch.begin();
                       batch.draw(bodyup,array.get(i).x,array.get(i).y,20,20);
                       batch.end();
                   }else{
                       batch.begin();
                       batch.draw(body,array.get(i).x,array.get(i).y,20,20);
                       batch.end();
                   }
               }
           }
        }

    private void move(){
        time=0;
        array.remove(0);
        if (moveto.equals("right")) {
            x = x + steps;
        } else if (moveto.equals("up")) {
            y = y + steps;
        } else if (moveto.equals("left")) {
            x = x - steps;
        } else if (moveto.equals("down")) {
            y = y - steps;
        }
        array.add(new Vector2(x, y));
    }
}
