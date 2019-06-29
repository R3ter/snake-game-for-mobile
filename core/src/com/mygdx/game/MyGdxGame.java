package com.mygdx.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.Levels;

public class MyGdxGame extends Game {
	private SpriteBatch batch;
	private AssetManager manager;


	@Override
	public void create () {

		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("loading.png", Texture.class);
		manager.finishLoading();

		this.setScreen(new Levels(batch,manager,this));
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
		manager.dispose();
	}
}
