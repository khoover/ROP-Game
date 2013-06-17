package com.kandl.ropgame.view;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.kandl.ropgame.model.Recipe;

public class Person extends Image {
	public static final int WALKING = 0;
	public static final int SITTING = 1;
	public static final int EATING = 2;
	
	private static Pool<Array<Animation>> maleSpriteStore;
	private static Pool<Array<Animation>> femaleSpriteStore;
	private static Pool<String> maleNames;
	private static Pool<String> femaleNames;

	private Array<Animation> sprites;
	private TextureRegionDrawable frame;
	private int state;
	private int stateTime;
	private boolean male;
	private String name;
	private Recipe order;
	private boolean flipped;
	private Label nameLabel;
	
	public Person(boolean male, int state, boolean flipped) {
		this.flipped = flipped;
		this.state = state;
		stateTime = 0;
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
		frame = new TextureRegionDrawable(sprites.get(state).getKeyFrame(0));
		this.setDrawable(frame);
		order = new Recipe();
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		stateTime += delta;
		frame.setRegion(sprites.get(state).getKeyFrame(stateTime, true));
		if (frame.getRegion().isFlipX() != flipped) frame.getRegion().flip(flipped, false);
	}
	
	@Override
	public void draw(SpriteBatch batch, float alpha) {
		super.draw(batch, alpha);
	}
	
	public Recipe getOrder() {
		return order;
	}
	
	public void setState(int state, boolean flipped) {
		this.state = state;
		this.flipped = flipped;
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
