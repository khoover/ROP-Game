package com.kandl.ropgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public class RopGame extends Game {
	public static GameScreen gameScreen;
	public static double score;
	public static final boolean DEBUG = true;
	public static final AssetManager assets = new AssetManager();
	
	@Override
	public void create() {
		score = 0;
		gameScreen = new GameScreen();
		setScreen(gameScreen);
	}	

	@Override
	public void dispose() {
		gameScreen.dispose();
		assets.dispose();
	}
}
