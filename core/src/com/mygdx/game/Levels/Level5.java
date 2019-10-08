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

public class Level5 extends GamePlay {
    public Level5(SpriteBatch batch, AssetManager manager, MyGdxGame game,int level) {
        super(batch, manager, game);
        this.level=level;
    }
    private OrthogonalTiledMapRenderer renderer;
    private StretchViewport viewport;
    private Stage stage;
    private TiledMap map;
    private Snake evilsnake;
    private int level;
    private Texture rock;
    @Override
    public void show() {
        super.show();

        drawbackground=true;
        steps=5;
        viewport=new StretchViewport(1400,400);
        stage=new Stage(viewport);

        evilsnake=new Snake(batch,manager,array);

        viewport.apply();
        inputMultiplexer.addProcessor(stage);


    }

    @Override
    protected void loadingfinished() {
        super.loadingfinished();

        dialog();
        grow=2;


    }


    @Override
    protected void drawfirst() {
        super.drawfirst();
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
            if (evilsnake.array.contains(array.get(array.size()-1))){
                restart();
            }
            if(start){
                evilsnake.render();
            }
            if(score>=10){
                game.setScreen(new WinScreen(batch,manager,game,level));
            }

            stage.act(delta);
            stage.draw();
        }
    }

    @Override
    protected void initimages() {
        super.initimages();
        map=manager.get("maps/level3.tmx",TiledMap.class);
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
