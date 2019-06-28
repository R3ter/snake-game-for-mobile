package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Levels.Level1;

import java.util.Timer;
import java.util.TimerTask;

public class MyGdxGame extends Game {
	SpriteBatch batch;
	Texture waitimg;
	AssetManager manager;
	boolean loading=true;

	public MyGdxGame(){

	}
	@Override
	public void create () {

		batch = new SpriteBatch();

		manager = new AssetManager();
		manager.load("loading.png", Texture.class);
		manager.finishLoading();


		this.setScreen(new Level1(batch,manager,this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
