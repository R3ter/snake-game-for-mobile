package com.mygdx.game.Levels;

import com.badlogic.gdx.assets.AssetManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Screens.GamePlay;
import com.mygdx.game.MyGdxGame;

public class Level2 extends GamePlay {
    public Level2(SpriteBatch batch, AssetManager manager, MyGdxGame game){
        super(batch,manager,game);
    }
    private OrthogonalTiledMapRenderer renderer;
    private TiledMap map;
    @Override
    public void show() {
        super.show();
         drawbackground=true;
    }

    @Override
    protected void loadingfinished() {
        super.loadingfinished();
        renderer=new OrthogonalTiledMapRenderer(map);
        MapLayer layer= map.getLayers().get(1);
        MapObjects objects=layer.getObjects();
        grow=2;
        renderer.setView(cam);

        for(MapObject t: objects){
            RectangleMapObject rect = (RectangleMapObject) t;
            wall.add(new Vector2(rect.getRectangle().x,rect.getRectangle().y));
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
            if(load("background2.png", Texture.class)){
               return;
           }else if(load("maps/level1.tmx",TiledMap.class)){
                return;
            }
        }else{
            movesnake(2);
        }
    }

    @Override
    protected void initimages() {
        super.initimages();
        map=manager.get("maps/level1.tmx",TiledMap.class);
    }
}
