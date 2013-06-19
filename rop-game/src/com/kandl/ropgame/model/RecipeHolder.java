package com.kandl.ropgame.model;

import com.badlogic.gdx.utils.Array;
import com.kandl.ropgame.model.Recipe;

public class RecipeHolder {
	private Array<Recipe> orders;
	private String name;
	
	public RecipeHolder (String name, Recipe... orders) {
		this.orders = new Array<Recipe>(orders);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Recipe getLeftRecipe() {
		return orders.get(0);
	}
	
	public Recipe getRightRecipe() {
		if (orders.size < 2) return null;
		else return orders.get(1);
	}
}
