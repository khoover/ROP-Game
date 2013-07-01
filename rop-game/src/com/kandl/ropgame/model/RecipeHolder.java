package com.kandl.ropgame.model;

import com.badlogic.gdx.utils.Array;
import com.kandl.ropgame.model.Recipe;

public class RecipeHolder {
	private Array<Recipe> orders;
	private String name;
	private int table;
	
	public RecipeHolder (String name, int table, Recipe... orders) {
		this.orders = new Array<Recipe>(orders);
		this.name = name;
		this.table = table;
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

	public int getTable() {
		return table;
	}
}
