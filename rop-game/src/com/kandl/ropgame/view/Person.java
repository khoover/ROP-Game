package com.kandl.ropgame.view;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.kandl.ropgame.model.Recipe;

public class Person extends Image {
	private static Pool<Array<Animation>> maleSpriteStore;
	private static Pool<Array<Animation>> femaleSpriteStore;
	private static Pool<String> maleNames;
	private static Pool<String> femaleNames;

	private Array<Animation> sprites;
	private boolean male;
	private String name;
	private Recipe order;
	
	public Person(boolean male) {
		this.male = male;
	}

	public static void initialize() {
		maleNames = new Pool<String>(0, 6){

			@Override
			protected String newObject() {
				return null;
			}
			
		};
		femaleNames = new Pool<String>(0, 6){

			@Override
			protected String newObject() {
				return null;
			}
			
		};
		maleSpriteStore = new Pool<Array<Animation>>(0, 6){

			@Override
			protected Array<Animation> newObject() {
				return null;
			}
			
		};
		femaleSpriteStore = new Pool<Array<Animation>>(0, 6){

			@Override
			protected Array<Animation> newObject() {
				return null;
			}
			
		};
		
		// add assets here
		maleNames.freeAll(new Array<String>(new String[] {"Bob", "Frank", "Joe", "Harry", "George", "Alf"}));
		femaleNames.freeAll(new Array<String>(new String[] {"Anne", "Mary", "Lucy", "Agnes", "Ridley", "Elly", "Lois"}));
	}
	
	public void free() {
		if (male) { maleNames.free(name); maleSpriteStore.free(sprites); }
		else { femaleNames.free(name); femaleSpriteStore.free(sprites); }
	}
	
}
