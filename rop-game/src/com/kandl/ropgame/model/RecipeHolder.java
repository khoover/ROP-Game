package com.kandl.ropgame.model;

import com.badlogic.gdx.utils.Array;
import com.kandl.ropgame.model.Recipe;

public class RecipeHolder {
	private Array<Recipe> orders;
	
	public RecipeHolder (Recipe... orders) {
		this.orders = new Array<Recipe>(orders);
	}
}
