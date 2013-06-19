package com.kandl.ropgame.ingredients;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

// Every subclass must have a section where it declares itself to ingredient.
public abstract class Ingredient{
	
	// Subclasses should load PIXMAP assets through here. Textures will be created in the Atlas.
	protected static AssetManager assets = new AssetManager();
	static {
		assets.load("img/food/food.atlas", TextureAtlas.class);
	}
	
	public static void loadAll() {
		assets.finishLoading();
	}
	
	public static void dispose() {
		assets.dispose();
	}

	private static Array<Class<? extends Ingredient>> subclasses = new Array<Class<? extends Ingredient>>(false, 6);
	
	public static void addSubclass (Class<? extends Ingredient> clazz) {
		subclasses.add(clazz);
	}
	
	public static Class<? extends Ingredient> getRandomIngredient() {
		return subclasses.random();
	}
	
	public abstract Image getSideView();
	public abstract Sprite getIcon();
}
