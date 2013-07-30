package com.kandl.ropgame.view.sandwichView;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.kandl.ropgame.model.Sandwich;
import com.kandl.ropgame.ingredients.*;

public class MakeView extends Group {
	private Sandwich sandwich;
	private Array<Image> ingredients;
	private int size;
	private float height;
	private final float buffer = 100;
	
	public MakeView(Sandwich s) {
		sandwich = s;
		ingredients = new Array<Image>(0);
		ingredients.add(sandwich.getBread().getSideView());
		addActor(ingredients.get(0));
		ingredients.get(0).setPosition(0, 0);
		ingredients.get(0).setScaling(Scaling.none);
		ingredients.get(0).setAlign(Align.bottom);
		height = ingredients.get(0).getHeight() - 25;
		ingredients.get(0).setSize(ingredients.get(0).getWidth(), ingredients.get(0).getHeight()+buffer);
		setSize(getWidth(), height + buffer);
	}
	
	public void addIngredient(Ingredient i) {
		sandwich.addIngredient(i);
		ingredients.add(i.getSideView());
		addActor(ingredients.peek());
		ingredients.peek().setPosition(0, height);
		ingredients.peek().setScaling(Scaling.none);
		ingredients.peek().setAlign(Align.bottom);
		height += ingredients.peek().getHeight() - 50;
		ingredients.peek().setSize(ingredients.peek().getWidth(), ingredients.peek().getHeight() + buffer);
		setSize(getWidth(), height + buffer);
	}
	
	public boolean isFull() {
		return ingredients.size >= 4;
	}
	
	public Sandwich getSandwich() {
		return sandwich;
	}
}
