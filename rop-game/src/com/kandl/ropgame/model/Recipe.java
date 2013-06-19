package com.kandl.ropgame.model;

import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kandl.ropgame.ingredients.*;

public class Recipe {
	public enum CookState {
		UNCOOKED, LIGHT, MEDIUM, WELL, BURNT;
		
		public static CookState getRandom() {
			double f = Math.random();
			if (f < 10d/3d) return LIGHT;
			if (f < 20d/3d) return MEDIUM;
			return WELL;
		}
		
		public static CookState fromTime(float t) {
			if (t < 5) return UNCOOKED;
			else if (t < 15) return LIGHT;
			else if (t < 25) return MEDIUM;
			else if (t < 35) return WELL;
			else return BURNT;
		}
	}
	
	private Array<Ingredient> ingredients;
	private Bread bread;
	private CookState cooked;
	private Array<Vector2> cut;
	
	public Recipe () {
		int i = (int) (Math.random() * 3) + 1;
		ingredients = new Array<Ingredient>(i);
		for (int n = 0; n < i; ++n) {
			try {
				ingredients.add(Ingredient.getRandomIngredient().getConstructor().newInstance());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		try {
			bread = Bread.getRandomIngredient().getConstructor().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		cooked = CookState.getRandom();
		cut = null;
	}

	public Array<Ingredient> getIngredients() {
		return ingredients;
	}

	public Bread getBread() {
		return bread;
	}

	public CookState getCooked() {
		return cooked;
	}

	public Array<Vector2> getCut() {
		return cut;
	}
}
