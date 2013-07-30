package com.kandl.ropgame.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;

public class ProgressBar extends Image implements Disposable {
	private static final float fullBar = 40;
	private static final Color YELLOW = new Color(1, 0.96f, 0.24f, 1);
	private static Pixmap background;
	static {
		background = new Pixmap(150, 40, Pixmap.Format.RGB888);
		background.setColor(Color.YELLOW);
		background.fill();
		background.setColor(Color.BLACK);
		background.fillRectangle(5, 5, 140, 30);
	}
	private float time;
	private Texture texture;
	private Pixmap bar;
	private boolean running;

	public ProgressBar() {
		super(new TextureRegionDrawable(new TextureRegion(new Texture(256, 128, Pixmap.Format.RGB888), 0, 0, 150, 40)), Scaling.none);
		running = true;
		texture = ((TextureRegionDrawable) getDrawable()).getRegion().getTexture();
		texture.draw(background, 0, 0);
		bar = new Pixmap(140, 30, Pixmap.Format.RGB888);
		bar.setColor(Color.BLACK);
		bar.fill();
		time = 0;
	}
	
	public ProgressBar(float time) {
		this();
		assert(time <= fullBar);
		this.time = time;
		running = false;
		int barWidth = (int) (140f * time / fullBar);
		if (time <= 5) bar.setColor(Color.WHITE);
		else if (time <= 15) bar.setColor(Color.RED);
		else if (time <= 25) bar.setColor(Color.ORANGE);
		else if (time <= 35) bar.setColor(YELLOW);
		else bar.setColor(Color.GREEN);
		bar.fillRectangle(0, 0, barWidth, 30);
		texture.draw(bar, 5, 5);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if (!running) return;
		float oldTime = time;
		time = Math.min(time + delta, fullBar);
		if (oldTime == time) return;
		int barWidth = (int) (140f * time / fullBar);
		if (time <= 5) bar.setColor(Color.WHITE);
		else if (time <= 15) bar.setColor(Color.RED);
		else if (time <= 25) bar.setColor(Color.ORANGE);
		else if (time <= 35) bar.setColor(YELLOW);
		else bar.setColor(Color.GREEN);
		bar.fillRectangle(0, 0, barWidth, 30);
		texture.draw(bar, 5, 5);
	}
	
	@Override
	public void dispose() {
		texture.dispose();
		bar.dispose();
	}
	
	public float getTime() {
		return time;
	}
	
	public void resume() {
		setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(256, 128, Pixmap.Format.RGB888), 0, 0, 150, 40)));
		texture = ((TextureRegionDrawable) getDrawable()).getRegion().getTexture();
		texture.draw(background, 0, 0);
		bar = new Pixmap(140, 30, Pixmap.Format.RGB888);
		bar.setColor(Color.BLACK);
		bar.fill();
		int barWidth = (int) (140f * time / fullBar);
		if (time <= 5) bar.setColor(Color.WHITE);
		else if (time <= 15) bar.setColor(Color.GREEN);
		else if (time <= 25) bar.setColor(YELLOW);
		else if (time <= 35) bar.setColor(Color.ORANGE);
		else bar.setColor(Color.RED);
		bar.fillRectangle(0, 0, barWidth, 30);
		texture.draw(bar, 5, 5);
	}

	public static void staticDispose() {
		background.dispose();
	}
}
