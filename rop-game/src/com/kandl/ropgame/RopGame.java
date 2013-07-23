package com.kandl.ropgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.kandl.ropgame.ui.UILayer;

public class RopGame extends Game {
	public static GameScreen gameScreen;
	public static double score;
	public static final boolean DEBUG = true;
	public static final AssetManager assets = new AssetManager();
	
	@Override
	public void create() {
		Gdx.app.log("life-cycle", "Creating.");
		score = 0;
		gameScreen = new GameScreen();
		setScreen((DEBUG ? gameScreen : new StartScreen(this)));
	}	

	@Override
	public void dispose() {
		gameScreen.dispose();
		assets.dispose();
		UILayer.buttonSkin.dispose();
	}
}
