package com.kandl.ropgame.view.sandwichView;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.kandl.ropgame.model.Sandwich;
import com.kandl.ropgame.ingredients.*;

public class MakeView extends Group {
	private Sandwich sandwich;
	private Array<Image> ingredients;
	private int size;
	private float height;
	
	public MakeView(Sandwich s) {
		sandwich = s;
		ingredients = new Array<Image>(0);
		ingredients.add(sandwich.getBread().getSideView());
		addActor(ingredients.get(0));
		ingredients.get(0).setPosition(0, 0);
		height = ingredients.get(0).getHeight() - 5;
	}
	
	public void addIngredient(Ingredient i) {
		sandwich.addIngredient(i);
		ingredients.add(i.getSideView());
		addActor(ingredients.peek());
		ingredients.peek().setPosition(0, height);
		height += ingredients.peek().getHeight() - 50;
		setSize(getWidth(), height);
	}
	
	public boolean isFull() {
		return ingredients.size >= 4;
	}
	
	public Sandwich getSandwich() {
		return sandwich;
	}
}
