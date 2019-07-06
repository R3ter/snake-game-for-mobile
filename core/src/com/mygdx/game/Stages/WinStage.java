package com.mygdx.game.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;


public class WinStage extends Stage {
    AssetManager manager;
    public WinStage(AssetManager manager, OrthographicCamera cam){
        StretchViewport viewport=new StretchViewport(500,260,cam);
        this.manager=manager;
        this.setViewport(viewport);
        Skin skin= new Skin(Gdx.files.internal("buttons/newbuttons/skin.json"),
               manager.get("buttons/newbuttons/buttons.atlas", TextureAtlas.class));
        ImageButton imageButton=new ImageButton(skin);
        Dialog dialog=new Dialog("",skin);
        dialog.show(this);
        dialog.button(imageButton);
        dialog.setDebug(true);
        dialog.setBounds(0,0,500,260);
    }
    public void show(){

    }
}
