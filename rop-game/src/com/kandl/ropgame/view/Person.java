package com.kandl.ropgame.view;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.kandl.ropgame.datamodel.ModelPerson;
import com.kandl.ropgame.model.Recipe;

public class Person extends Group {
	public static final int WALKING = 0;
	public static final int SITTING = 1;
	public static final int EATING = 2;
	
	private static Pool<Array<Animation>> maleSpriteStore;
	private static Pool<Array<Animation>> femaleSpriteStore;
	private static Pool<String> maleNames;
	private static Pool<String> femaleNames;
	private static Array<Array<Animation>> maleToFree;
	private static Array<Array<Animation>> femaleToFree;
	private static Array<String> maleNameToFree;
	private static Array<String> femaleNameToFree;
	private static TextureAtlas spriteAtlas;
	
	private Array<Animation> sprites;
	private final boolean male;
	private final String name;
	private final Recipe order;
	private boolean flipped;
	private ModelPerson model;
	
	public Person(boolean male, boolean flipped) {
		this.flipped = flipped;
		if (male) {
			sprites = maleSpriteStore.obtain();
			if (sprites == null) {
				this.male = false;
				sprites = femaleSpriteStore.obtain();
				name = femaleNames.obtain();
			}
			else {
				this.male = male;
				name = maleNames.obtain();
			}
		} else {
			sprites = femaleSpriteStore.obtain();
			if (sprites == null) {
				this.male = true;
				sprites = maleSpriteStore.obtain();
				name = maleNames.obtain();
			}
			else {
				this.male = male;
				name = femaleNames.obtain();
			}
		}
		order = new Recipe();
		model = new ModelPerson(order, null);
		order.getModel().setPerson(this);
	}
	
	public Recipe getOrder() {
		return order;
	}
	
	public String getName() {
		return name;
	}
	
	public void setFlipped(boolean flipped) {
		this.flipped = flipped;
	}
	
	public TextureRegion getFrame(int state, float stateTime) {
		TextureRegion r = sprites.get(state).getKeyFrame(stateTime, true);
		if (r.isFlipX() != flipped) r.flip(true, false);
		return r;
	}

	public static void initialize() {
		maleNames = new Pool<String>(0, 6){

			@Override
			protected String newObject() {
				return null;
			}
			
		};
		maleNameToFree = new Array<String>(6);
		femaleNames = new Pool<String>(0, 6){

			@Override
			protected String newObject() {
				return null;
			}
			
		};
		femaleNameToFree = new Array<String>(6);
		maleSpriteStore = new Pool<Array<Animation>>(0, 3){

			@Override
			protected Array<Animation> newObject() {
				return null;
			}
			
		};
		maleToFree = new Array<Array<Animation>>(3);
		femaleSpriteStore = new Pool<Array<Animation>>(0, 3){

			@Override
			protected Array<Animation> newObject() {
				return null;
			}
			
		};
		femaleToFree = new Array<Array<Animation>>(3);
		
		// add assets here
		maleNames.freeAll(new Array<String>(new String[] {"Bob", "Frank", "Joe", "Harry", "George", "Alf"}));
		femaleNames.freeAll(new Array<String>(new String[] {"Anne", "Mary", "Lucy", "Agnes", "Ridley", "Elly", "Lois"}));
		
		spriteAtlas = new TextureAtlas("img/people/people.atlas");
		for (int i = 1; i <= 3; ++i) {
			Array<? extends TextureRegion> regions = spriteAtlas.findRegions(String.format("person%1$d", i));
			Animation walking = null;
			Animation sitting = new Animation(1, regions.get(0));
			sitting.setPlayMode(Animation.LOOP);
			Animation eating = new Animation (0.4f, regions, Animation.LOOP);
			Array<Animation> sprite = new Array<Animation>(new Animation[] {walking, sitting, eating});
			femaleSpriteStore.free(sprite);
		}
		for (int i = 4; i <= 6; ++i) {
			Array<? extends TextureRegion> regions = spriteAtlas.findRegions(String.format("person%1$d", i));
			Animation walking = null;
			Animation sitting = new Animation(1, regions.get(0));
			sitting.setPlayMode(Animation.LOOP);
			Animation eating = new Animation (0.4f, regions, Animation.LOOP);
			Array<Animation> sprite = new Array<Animation>(new Animation[] {walking, sitting, eating});
			maleSpriteStore.free(sprite);
		}
	}
	
	public void free() {
		model.close();
		if (male) { maleNameToFree.add(name); maleToFree.add(sprites);
			if (maleToFree.size >= 2) { maleToFree.shuffle(); maleSpriteStore.freeAll(maleToFree); maleToFree.clear(); }
			if (maleNameToFree.size >= 2) { maleNameToFree.shuffle(); maleNames.freeAll(maleNameToFree); maleNameToFree.clear();}
		}
		else { femaleNameToFree.add(name); femaleToFree.add(sprites);
			if (femaleToFree.size >= 2) { femaleToFree.shuffle(); femaleSpriteStore.freeAll(femaleToFree); femaleToFree.clear(); }
			if (femaleNameToFree.size >= 2) { femaleNameToFree.shuffle(); femaleNames.freeAll(femaleNameToFree); femaleNameToFree.clear(); }
		}
	}
	
	public static void dispose() {
		spriteAtlas.dispose();
	}

	public ModelPerson getModel() {
		return model;
	}
	
}
