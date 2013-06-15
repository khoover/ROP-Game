package com.kandl.ropgame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.esotericsoftware.tablelayout.Cell;
import com.kandl.ropgame.*;

/** A table responsible for the entire UI layout, and all assets contained by it.
 * 
 * @author Ken Hoover */
public class UILayer extends Stage implements Disposable {
	private final Image orderLine;
	
	// top right corner stuff
	private final Label score;
	private final Button[] scene;
	private final ButtonGroup scenes;
	
	private Image expandedOrder;	
	private final Image clock;
	
	private final Skin rightPanelSkin;

	public UILayer(int width, int height, boolean stretch) {
		super(width, height, stretch);
		
		// set skin up
		rightPanelSkin = new Skin(Gdx.files.internal("img/icons/buttons.json"));
		Pixmap background = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		background.setColor(Color.BLACK);
		background.fill();
		rightPanelSkin.add("background", new TiledDrawable(new TextureRegion(new Texture(background))), TiledDrawable.class);
		background.dispose();
		
		// create components of corner table
		score = new Label("$" + String.format("%1$.2f", RopGame.score), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/score.fnt"), false), Color.YELLOW));
		scenes = new ButtonGroup();
		scene = new Button[4];
		for (int i = 0; i < 4; ++i) {
			scene[i] = new Button((Drawable) null);
			scenes.add(scene[i]);
		}
		scene[0].setStyle(rightPanelSkin.get("front", Button.ButtonStyle.class));
		scene[1].setStyle(rightPanelSkin.get("make", Button.ButtonStyle.class));
		scene[2].setStyle(rightPanelSkin.get("grill", Button.ButtonStyle.class));
		scene[3].setStyle(rightPanelSkin.get("cut", Button.ButtonStyle.class));
		
		expandedOrder = new Image(rightPanelSkin.get("front", Button.ButtonStyle.class).up, Scaling.fit, Align.center);
		clock = new Image();
		orderLine = new Image();
				
		//necessary because java hates everyone
		scene[0].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameScreen.switchScreen(0);
			}
		});
		scene[1].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameScreen.switchScreen(1);
			}
		});
		scene[2].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameScreen.switchScreen(2);
			}
		});
		scene[3].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameScreen.switchScreen(3);
			}
		});
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		score.setText("$" + String.format("%1$.2f", RopGame.score));
	}
	
	public void resize(float width, float height) {
	}
	
	public Image getOrderImage () {
		return expandedOrder;
	}

	@Override
	public void dispose() {
		
	}
}
