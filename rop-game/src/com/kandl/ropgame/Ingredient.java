package com.kandl.ropgame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Ingredient {
	public static int HEIGHT = 100;
	
	public void drawSide(SpriteBatch batch, float x, float y, float width, float height);	
	public void drawTop(SpriteBatch batch, float x, float y, float width, float height);
}