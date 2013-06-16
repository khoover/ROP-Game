package com.kandl.ropgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.kandl.ropgame.view.Person;

public class RopGame extends Game {
	public static GameScreen gameScreen;
	public static double score;
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 600;
	public static final boolean DEBUG = true;
	public static final AssetManager assets = new AssetManager();
	
	@Override
	public void create() {
		score = 0;
		loadAll();
		Person.initialize();
		gameScreen = new GameScreen();
		setScreen(gameScreen);
	}
	
	private void loadAll() {
		assets.load("img/backgrounds/new front.png", Texture.class);
		assets.load("img/backgrounds/cuttingBoard.png", Texture.class);
		assets.load("img/backgrounds/Grills.png", Texture.class);
		assets.finishLoading();
	}

	@Override
	public void dispose() {
		gameScreen.dispose();
		assets.dispose();
	}
}
