package com.kandl.ropgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public class RopGame extends Game {
	public static GameScreen gameScreen;
	public static float Score;
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 600;
	public static final boolean DEBUG = true;
	public static final AssetManager assets = new AssetManager();
	
	@Override
	public void create() {
		Score = 0;
		gameScreen = new GameScreen();
		setScreen(gameScreen);
		loadAll();
	}
	
	private void loadAll() {
		assets.finishLoading();
	}

	@Override
	public void dispose() {
		gameScreen.dispose();
		assets.dispose();
	}
}
