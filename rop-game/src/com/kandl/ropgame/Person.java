package com.kandl.ropgame;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class Person extends Image {
	public static final int WALKING = 0;
	public static final int SITTING = 1;
	public static final int EATING = 2;
	
	private final TextureRegionDrawable current_frame;
	private int current_state;
	private final Array<Animation> sprites;
	private int stateTime;
	private boolean isFlipped;

	public Person(final Animation walking, final Animation sitting, final Animation eating) {
		setLayoutEnabled(false);
		sprites = new Array<Animation>(new Animation[] {walking, sitting, eating});
		current_state = WALKING;
		stateTime = 0;
		current_frame = new TextureRegionDrawable(sprites.get(current_state).getKeyFrame(stateTime, true));
		setDrawable(current_frame);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		stateTime += delta; 
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		current_frame.setRegion(sprites.get(current_state).getKeyFrame(stateTime, true));
		if (current_frame.getRegion().isFlipX() != isFlipped) current_frame.getRegion().flip(true, false);
		super.draw(batch, parentAlpha);
	}
	
	public void setState(int state, boolean flipped) {
		if (current_state == state) {
			if (isFlipped != flipped) stateTime = 0;
		} else {
			current_state = state;
			stateTime = 0;
		}
		isFlipped = flipped;
	}
}