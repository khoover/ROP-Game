package com.kandl.ropgame.ingredients;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.kandl.ropgame.model.Recipe.CookState;

public abstract class Bread extends Ingredient {
	
	private static Array<Class<? extends Bread>> subclasses = new Array<Class<? extends Bread>>(false, 6);
	
	public static void addSubclass (Class<? extends Ingredient> clazz) {
		if (Bread.class.isAssignableFrom(clazz)) subclasses.add(clazz.asSubclass(Bread.class));
	}
	
	public static Class<? extends Bread> getRandomIngredient() {
		return subclasses.random();
	}
	
	public abstract Image getTopView(CookState state);
	public abstract Image getLargeTopView(CookState state);
}
