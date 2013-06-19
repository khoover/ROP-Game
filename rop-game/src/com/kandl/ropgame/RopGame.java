package com.kandl.ropgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.kandl.ropgame.ingredients.Ingredient;
import com.kandl.ropgame.view.Person;

public class RopGame extends Game {
	public static GameScreen gameScreen;
	public static double score;
	public static final boolean DEBUG = true;
	public static final AssetManager assets = new AssetManager();
	
	@Override
	public void create() {
		score = 0;
		loadAll();
		gameScreen = new GameScreen();
		setScreen(gameScreen);
	}
	
	private void loadAll() {
		assets.load("img/backgrounds/new front.png", Texture.class);
		assets.load("img/backgrounds/cuttingBoard.png", Texture.class);
		assets.load("img/backgrounds/Grills.png", Texture.class);
		assets.load("img/backgrounds/Making.png", Texture.class);
		assets.load("img/icons/buttons.atlas", TextureAtlas.class);
		assets.finishLoading();
	}

	@Override
	public void dispose() {
		gameScreen.dispose();
		assets.dispose();
	}
}
