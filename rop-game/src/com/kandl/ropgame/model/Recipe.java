package com.kandl.ropgame.model;

import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kandl.ropgame.datamodel.ModelRecipe;
import com.kandl.ropgame.ingredients.*;

public class Recipe {
	public enum CookState {
		UNCOOKED, LIGHT, MEDIUM, WELL;
		
		public static CookState getRandom() {
			double f = Math.random();
			if (f < 1d/3d) return LIGHT;
			if (f < 2d/3d) return MEDIUM;
			return WELL;
		}
		
		public static CookState fromTime(float t) {
			if (t < 10) return UNCOOKED;
			else if (t < 20) return LIGHT;
			else if (t < 30) return MEDIUM;
			else return WELL;
		}
		
		public static float toTime(CookState s) {
			switch(s) {
			case UNCOOKED:
				return 0;
			case LIGHT:
				return 10;
			case MEDIUM:
				return 20;
			case WELL:
				return 30;
			default:
				return 40;
			}
		}
	}
	
	private Array<Ingredient> ingredients;
	private Bread bread;
	private Array<Vector2> cut, pos;
	private final ModelRecipe model;
	
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
		cut = new Array<Vector2>(2);
		pos = new Array<Vector2>(2);
		int n = (int) (Math.random() * 16);
		if ((n & 0x0001) != 0) {
			cut.add(new Vector2(1, 0));
			pos.add(new Vector2(0.5f, 0.5f));
		}
		if ((n & 0x0002) != 0) {
			cut.add(new Vector2(0, 1));
			pos.add(new Vector2(0.5f, 0.5f));
		}
		if ((n & 0x0004) != 0) {
			cut.add(new Vector2(1, 1).nor());
			pos.add(new Vector2(0.5f,0.5f));
		}
		if ((n & 0x0008) != 0) {
			cut.add(new Vector2(-1,1).nor());
			pos.add(new Vector2(0.5f,0.5f));
		}
		
		model = new ModelRecipe(ingredients, cut);
	}

	public Array<Ingredient> getIngredients() {
		return ingredients;
	}

	public Bread getBread() {
		return bread;
	}

	public Array<Vector2> getCut() {
		return cut;
	}
	
	public Array<Vector2> getPos() {
		return pos;
	}

	public ModelRecipe getModel() {
		return model;
	}
}
