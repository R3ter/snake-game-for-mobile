package com.mygdx.game.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
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
import com.mygdx.game.enemies.Snake;

public class Level3 extends GamePlay {
    public Level3(SpriteBatch batch, AssetManager manager, MyGdxGame game,int level) {
        super(batch, manager, game);
        this.level=level;
    }

    private OrthogonalTiledMapRenderer renderer;
    private StretchViewport viewport;
    private Stage stage;
    private Snake evilsnake;
    private TiledMap map;
    private int level;
    private Texture rock;
    @Override
    public void show() {
        super.show();
        drawbackground=true;
        steps=5;
        viewport=new StretchViewport(1400,400);
        stage=new Stage(viewport);

        viewport.apply();
        inputMultiplexer.addProcessor(stage);

        evilsnake=new Snake(batch,manager,array);

    }

    @Override
    protected void loadingfinished() {
        super.loadingfinished();
        for(int i=0;i<100;i++)
        array.add(new Vector2(0,0));
        renderer=new OrthogonalTiledMapRenderer(map);
        MapLayer layer= map.getLayers().get(2);
        MapObjects objects=layer.getObjects();
        grow=2;
        renderer.setView(cam);

        rock=manager.get("objects/rock.png");

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
            if(load("background2.png", Texture.class)){
                return;
            }else if(load("maps/level3.tmx",TiledMap.class)){
                return;
            }else if(load("objects/rock.png",Texture.class)){
                return;
            }if(load("buttons/dialog/winbuttons.atlas", TextureAtlas.class)){
                return;
            }
            evilsnake.load();
        }else{
            movesnake(5);
            if (evilsnake.array.contains(array.get(array.size()-1))){
                restart();
            }

            if(start)
                evilsnake.render();

            stage.act(delta);
            stage.draw();
            for(Vector2 v: rocks){
                batch.begin();
                batch.draw(rock,v.x,v.y,20,30);
                batch.end();
            }
            if(score>=20){
                game.setScreen(new WinScreen(batch,manager,game,level));
            }
        }
    }

    @Override
    protected void initimages() {
        super.initimages();
        map=manager.get("maps/level3.tmx",TiledMap.class);
        dialog();
        evilsnake.init();
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
        Label label=new Label("eat 20 apples \n" +
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
