package com.mygdx.game.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Screens.GamePlay;
import static com.mygdx.game.Screens.GamePlay.power;
import com.mygdx.game.SimpleDirectionGestureDetector;

import java.util.ArrayList;
import java.util.Random;

import static com.mygdx.game.Screens.GamePlay.food;
import static com.mygdx.game.Screens.GamePlay.wall;

public class SnakePlayer {
    public ArrayList<Vector2> array;
    public boolean die=false;
    public float x=20,y=20;
    private boolean pause;
    private float steps;
    private int timetotap=0;
    private String moveto="right";
    private ArrayList<Vector2> turn;
    private SpriteBatch batch;
    private String dir="right";
    private GamePlay gamePlay;
    private Texture snake;
    private Random rand;
    private InputMultiplexer inputMultiplexer;
    private ArrayList<Vector2> perv;
    private TextureRegion head,headup,corner,tailup,tail,body,bodyup;
    private AssetManager manager;
    public SnakePlayer(SpriteBatch batch, AssetManager manager,GamePlay gamePlay,
                       InputMultiplexer inputMultiplexer,float steps) {
        this.inputMultiplexer=inputMultiplexer;
        this.batch=batch;
        this.manager=manager;
        this.gamePlay=gamePlay;
        this.steps=steps;
        array=new ArrayList<Vector2>();
        for(int i=0; i<100; i++){
            array.add(new Vector2(-480,-240));
        }
        rand=new Random();
        turn=new ArrayList<Vector2>();
        perv=new ArrayList<Vector2>();
        snake=manager.get("snake2.png",Texture.class);

        touchCotroll();

        head=new TextureRegion(snake,297 ,0,203,168);
        headup=new TextureRegion(snake,24 ,291,168,203);
        corner=new TextureRegion(snake,328 ,382,110,110);

        tailup=new TextureRegion(snake,24 ,88,168,203);
        tail=new TextureRegion(snake,220 ,194,203,168);

        body=new TextureRegion(snake,260 ,194,203,168);
        bodyup=new TextureRegion(snake,24 ,42,168,203);

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
    private void speed(float power){
        move();
        power--;
        if(power>0){
            speed(power);
        }
    }
    private int speed=0;
    private void controll(){
        if(speed>0){
            speed(power);
            speed--;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            speed=20;
        }
        if(Gdx.input.isTouched()){
            if(timetotap<=0){
                speed(power);
            }
        }else{
            timetotap=15;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) &&
                !moveto.equals("right") ) {
            dir = "left";
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) &&
                !moveto.equals("left") ) {
            dir = "right";
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) &&
                !moveto.equals("down") ) {
            dir = "up";
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) &&
                !moveto.equals("up") ) {
            dir = "down";
        }

    }


public void drawtest(){
        if(turn.size()==0){
            if (array.get(0).y == array.get(array.size()-1).y) {
                batch.begin();
                batch.draw(body, array.get(array.size()-1).x, array.get(array.size()-1).y,
                        array.get(array.size() - 1).x-array.get(0).x<0?
                                Math.abs(array.get(0).x-array.get(array.size() - 1).x):
                                array.get(0).x-array.get(array.size() - 1).x,
                        20);
                batch.end();
            }else{
                batch.begin();
                batch.draw(bodyup, array.get(array.size()-1).x, array.get(array.size()-1).y,
                        20,
                        array.get(array.size() - 1).y-array.get(0).y<0?
                                Math.abs(array.get(0).y-array.get(array.size() - 1).y):
                                array.get(0).y-array.get(array.size() - 1).y);
                batch.end();
            }
        }else{
            int i=0;
            try{
                if(turn.get(i).y==array.get(0).y){
                    batch.begin();
                    batch.draw(bodyup, array.get(array.size()-1).x, turn.get(i).y,
                            20,
                            turn.get(i).y-array.get(0).y<0?
                                    Math.abs(array.get(0).y-turn.get(i).y):
                                    array.get(0).y-turn.get(i).y
                    );
                    batch.end();
                }

            }catch (Exception e){

            }
            /*
            for (int i=0; i<turn.size(); i++){
                if(turn.get(i).y==array.get(array.size()-1).y){
                    batch.begin();
                    batch.draw(bodyup, array.get(array.size()-1).x, turn.get(i).y,
                            20,
                            turn.get(i).y-array.get(0).y<0?
                                    Math.abs(array.get(0).y-turn.get(i).y):
                                    array.get(0).y-turn.get(i).y
                            );
                    batch.end();
                }else{
                    batch.begin();
                    batch.draw(body, array.get(array.size()-1).x, turn.get(i).y,
                            turn.get(i).x-array.get(0).x<0?
                                    Math.abs(array.get(0).x-turn.get(i).x):
                                    array.get(0).x-turn.get(i).x
                            ,20
                    );
                    batch.end();
                }
            }
            */
        }
        /*
            if (turn.get(i-1).x == array.get(i).x) {
                batch.begin();
                batch.draw(bodyup, array.get(i).x, array.get(i).y, 20,
                        array.get(0).y-array.get(array.size()-1).y);
                batch.end();
            } else {
                batch.begin();
                batch.draw(body, array.get(i).x, array.get(i).y,
                        array.get(0).x-array.get(array.size()-1).x, 20);
                batch.end();

            }
            */
        }



    public void draw() {

        for (int i = 0; i < turn.size(); i++) {
            batch.begin();
            batch.draw(corner, turn.get(i).x, turn.get(i).y, 20, 20);
            batch.end();
        }
        int current=array.size()-1;

        face();

        for (int i = 0; i < array.size(); i++) {


            if (i == array.size() - 1) {
                batch.begin();
                if (moveto.equals("up") || moveto.equals("down"))
                    batch.draw(headup, array.get(i).x, array.get(i).y, 20, 20);
                else
                    batch.draw(head, array.get(i).x, array.get(i).y, 20, 20);
                batch.end();
            } else if (i == 0) {

                batch.begin();
                if (array.get(0).x < array.get(1).x) {
                    if (tail.isFlipX())
                        tail.flip(true, false);
                    batch.draw(tail, array.get(i).x, array.get(i).y, 20, 20);
                } else if (array.get(0).x > array.get(1).x) {
                    if (!tail.isFlipX())
                        tail.flip(true, false);
                    batch.draw(tail, array.get(i).x, array.get(i).y, 20, 20);
                } else if (array.get(0).y < array.get(1).y) {
                    if (tailup.isFlipY())
                        tailup.flip(false, true);
                    batch.draw(tailup, array.get(i).x, array.get(i).y, 20, 20);
                } else if(array.get(0).y > array.get(1).y) {
                    if (!tailup.isFlipY())
                        tailup.flip(false, true);
                    batch.draw(tailup, array.get(i).x, array.get(i).y, 20, 20);
                }
                batch.end();
            } else {


                //to skip drawing the unnecessary parts
                boolean skip=false;
                for(int f=0; f<turn.size(); f++){
                    if((turn.get(f).x+5>array.get(i).x&&
                            turn.get(f).x-5<array.get(i).x)&&
                            (turn.get(f).y+5>array.get(i).y&& turn.get(f).y-5<array.get(i).y))
                    {skip=true; }}
                if(!skip){
                    if(array.get(i).y == array.get(current).y)
                        if (array.get(i).x < array.get(current).x + 19&&
                                array.get(i).x > array.get(current).x - 19){
                            continue;
                        }
                    if(array.get(i).x == array.get(current).x)
                        if(array.get(i).y < array.get(current).y + 19&&
                                array.get(i).y > array.get(current).y - 19){
                            continue;
                        }


                }

                //don't draw on corners
                boolean draw=true;
                for(int f=0; f<turn.size(); f++){
                    if((turn.get(f).x+3>array.get(i).x&&
                            turn.get(f).x-3<array.get(i).x)&&
                            (turn.get(f).y+3>array.get(i).y&& turn.get(f).y-3<array.get(i).y))
                    {draw=false; }}
                if(!draw){ continue;}

                current = i;

                if (array.get(i-1).x == array.get(i).x) {
                    batch.begin();
                    batch.draw(bodyup, array.get(i).x, array.get(i).y, 20,
                            20);
                    batch.end();
                } else {
                    batch.begin();
                    batch.draw(body, array.get(i).x, array.get(i).y,
                            20, 20);
                    batch.end();

                }


            }
        }

    }
        public void render(boolean start,boolean pause,boolean hit,int grow){
        System.out.println(Gdx.graphics.getFramesPerSecond());
            if(timetotap>=0){
                timetotap-=Gdx.graphics.getDeltaTime();
            }
        this.pause=pause;
        if (start) {

            if (x < food.x + 10 &&x + 20 > food.x &&y < food.y + 10 &&y + 20 > food.y){
                for (int i=0; i<10; i++){
                    array.add(0,new Vector2(-100,0));

                }
                gamePlay.eat();
            }

            controll();

                move();


                if (!moveto.equals(dir)) {
//                    if(steps*y==0&&y%steps*x==0) {
                    boolean canturn=true;
                    for(int i=0; i<turn.size();i++){
                        if(((turn.get(i).x+10>x&&turn.get(i).x-10<x)&&turn.get(i).y==y)||
                                ((turn.get(i).y+10>y&&turn.get(i).y-10<y)&&turn.get(i).x==x)){
                            canturn=false;
                            break;
                        }
                    }
                    if(canturn){
                        moveto = dir;
                        turn.add(new Vector2(x, y));
                    }
//                    }

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
                }
        }



            for (int i = 0; i < turn.size(); i++) {
                if(!array.contains(turn.get(i))){
                    turn.remove(i);
                }
            }


        if(perv.size()>array.size()+300){
            perv.remove(0);
        }
    }


    private  void  hit(){
        gamePlay.pause();
    }

    private void move(){
        if(!pause){
            checkplayer();
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

    private void checkplayer(){
        //da eating himself function
        for(int i=0; i<array.size(); i++) {
            if (i < array.size() - 20 && x < array.get(i).x + 10 &&
                    x + 10 > array.get(i).x &&
                    y < array.get(i).y + 10 &&
                    y + 10 > array.get(i).y) {

                hit();
            }
        }
            //hit da wall function
            for(int f=0; f<wall.size(); f++){
                if(x < wall.get(f).x + 10 &&
                        x + 10 > wall.get(f).x &&
                        y < wall.get(f).y + 10 &&
                        y + 10 > wall.get(f).y){
                    hit();
                }
            }

    }



    private void touchCotroll(){
        inputMultiplexer.addProcessor(new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {

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
}
