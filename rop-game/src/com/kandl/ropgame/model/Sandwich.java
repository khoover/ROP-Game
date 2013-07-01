package com.kandl.ropgame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kandl.ropgame.ingredients.*;

public class Sandwich {
	private Array<Ingredient> components;
	private float cookTime;
	private Bread bread;
	private Array<Vector2> cuts, positions;
	
	public Sandwich (Bread bread) {
		components = new Array<Ingredient>(2);
		cookTime = 0;
		this.bread = bread;
		cuts = new Array<Vector2>(0);
		positions = new Array<Vector2>(0);
	}
	
	public Bread getBread() {
		return bread;
	}
	
	public void addIngredient(Ingredient i) {
		components.add(i);
	}
	
	public Array<Ingredient> getIngredients() {
		return components;
	}
	
	public void cook(float delta) {
		cookTime += delta;
	}
	
	public float getCookTime() {
		return cookTime;
	}
	
	public void addCut(Vector2 cut, Vector2 position) {
		cuts.add(cut);
		positions.add(position);
	}
	
	public Array<Vector2> getCuts() {
		return cuts;
	}
	
	public Array<Vector2> getPositions() {
		return positions;
	}
}
