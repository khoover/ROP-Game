package com.kandl.ropgame;

import com.badlogic.gdx.Game;

public class RopGame extends Game {
	private GameScreen gameScreen;
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 600;
	
	@Override
	public void create() {
		gameScreen = new GameScreen();
		setScreen(gameScreen);
	}

	@Override
	public void dispose() {
		gameScreen.dispose();
	}
}
