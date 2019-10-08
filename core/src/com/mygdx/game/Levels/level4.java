package com.mygdx.game.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.GamePlay;
import com.mygdx.game.Screens.WinScreen;

import java.util.Random;


public class level4 extends GamePlay {

    public level4(SpriteBatch batch, AssetManager manager, MyGdxGame game,int level) {
        super(batch, manager, game);
        this.level=level;
    }
    private Random rand;
    private OrthogonalTiledMapRenderer renderer;
    private StretchViewport viewport;
    private Stage stage;
    private TiledMap map;
    private int level;
    private Sprite applepoint;

    @Override
    public void show() {
        super.show();
        rand=new Random();
        drawbackground=true;
        steps=5;
        viewport=new StretchViewport(1400,400);
        stage=new Stage(viewport);
        wall.clear();

        for (int i=0; i<13; i++){
            wall.add(new Vector2(0,i*20));
            wall.add(new Vector2(34*20,i*20));
        }
        for (int i=0; i<35; i++){
            wall.add(new Vector2(i*20,0));
            wall.add(new Vector2(i*20,12*20));
        }


        viewport.apply();
        inputMultiplexer.addProcessor(stage);

    }

    @Override
    protected void loadingfinished() {
        super.loadingfinished();

        renderer=new OrthogonalTiledMapRenderer(map,batch);
        MapLayer layer= map.getLayers().get(2);
        MapObjects objects=layer.getObjects();
        grow=2;


        for(MapObject t: objects){
            RectangleMapObject rect = (RectangleMapObject) t;
            rocks.add(new Vector2(rect.getRectangle().x,rect.getRectangle().y));
        }
    }

    @Override
    protected void drawfirst() {
        super.drawfirst();
        renderer.render();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if(loading){
            if(load("maps/level4.tmx",TiledMap.class)){
                return;
            }else if(load("buttons/dialog/winbuttons.atlas", TextureAtlas.class)){
                return;
            }else if(load("applepoint.png", Texture.class)){
                return;
            }

        }else{



            if(score>=10){
                game.setScreen(new WinScreen(batch,manager,game,level));
            }

            if(start){
                if(x>500/2f&&x<450){
                    cam.position.set(new Vector2(x,cam.position.y),1);
                }else if(x<500/2f){
                    cam.position.set(new Vector2(500/2f,cam.position.y),1);
                }else if(x>=450){
                    cam.position.set(new Vector2(450,cam.position.y),1);
                }
                cam.update();

            }
            if(food.x-(500/2f)>cam.position.x){
                if(applepoint.isFlipX()){
                    applepoint.flip(true,false);
                }
                batch.begin();
                batch.draw(applepoint,450,food.y,20,20);
                batch.end();
            }else if(food.x+(500/2f)<cam.position.x){
                if(!applepoint.isFlipX()){
                    applepoint.flip(true,false);
                }
                batch.begin();
                batch.draw(applepoint,0,food.y,20,20);
                batch.end();
            }
            renderer.setView(cam);
            stage.act(delta);
            stage.draw();

        }
    }

    @Override
    protected void newfood() {
        food = new Vector2((rand.nextInt(34) + 1) * 20, (rand.nextInt(8) + 1) * 20);

    }

    @Override
    protected void initimages() {
        super.initimages();
        map=manager.get("maps/level4.tmx",TiledMap.class);
        applepoint=new Sprite(manager.get("applepoint.png",Texture.class));

        dialog();
    }

    private void dialog(){
        Skin skin=new Skin(Gdx.files.internal("buttons/dialog/skin.json"),
                manager.get("buttons/dialog/winbuttons.atlas", TextureAtlas.class));
        Dialog dialog=new Dialog("title",skin){
            @Override
            protected void result(Object object) {
                super.result(object);
                start=true;
            }
        };
        ImageButton button=new ImageButton(skin);
        dialog.setBounds(0,0,500,250);
        button.setSize(1,1);
        dialog.add().height(20);
        dialog.row();
        Label label=new Label("eat 10 apples \n" +
                " beware of the other snake",skin);

        dialog.text(label).pad(100);
        dialog.button(button);
        dialog.getButtonTable().getCells().first().height(80).width(80);
        dialog.pack();
        dialog.show(stage);

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }
}
