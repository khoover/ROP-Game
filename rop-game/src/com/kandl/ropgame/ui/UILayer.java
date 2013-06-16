package com.kandl.ropgame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.kandl.ropgame.*;

/** A stage responsible for the entire UI layout, and all assets contained by it.
 * 
 * @author Ken Hoover */
public class UILayer extends Stage implements Disposable {
	private float padX, padY;
	
	private final Image orderLine;
	
	// right panel stuff
	private final Label score;
	private final Button[] scene;
	private final ButtonGroup scenes;
	private final Image background;
	private final Image expandedOrder;	
	
	private final Image clock;
	
	private final Skin rightPanelSkin;

	public UILayer(int width, int height, boolean stretch) {
		super(width, height, stretch);
		padX = 440;
		padY = 180;
		
		// set skin up
		rightPanelSkin = new Skin(Gdx.files.internal("img/icons/buttons.json"));
		Pixmap background = new Pixmap(128, 128, Pixmap.Format.RGBA8888);
		background.setColor(Color.BLACK);
		background.fill();
		this.background = new Image(new TiledDrawable(new TextureRegion(new Texture(background))));
		background.dispose();
		
		// create components of corner table
		score = new Label("$" + String.format("%1$.2f", RopGame.score), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/score.fnt"), false), Color.YELLOW));
		score.getStyle().font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		scenes = new ButtonGroup();
		scene = new Button[4];
		for (int i = 0; i < 4; ++i) {
			scene[i] = new Button((Drawable) null);
			scenes.add(scene[i]);
			scene[i].setSize(102, 102);
		}
		scene[0].setStyle(rightPanelSkin.get("front", Button.ButtonStyle.class));
		scene[1].setStyle(rightPanelSkin.get("make", Button.ButtonStyle.class));
		scene[2].setStyle(rightPanelSkin.get("grill", Button.ButtonStyle.class));
		scene[3].setStyle(rightPanelSkin.get("cut", Button.ButtonStyle.class));
		
		expandedOrder = new Image(rightPanelSkin.get("front", Button.ButtonStyle.class).up, Scaling.stretch);
		clock = new Image();
		orderLine = new Image();
		
		// add the components. ORDERING IMPORTANT
		addActor(clock);
		clock.setPosition(5, 5);
		addActor(orderLine);
		orderLine.setPosition(0, height - padY);
		orderLine.setSize(width - padX, padY);
		addActor(this.background);
		this.background.setPosition(width - padX - 5, 0);
		this.background.setSize(padX + 5, height);
		addActor(score);
		score.setPosition(width - padX + 5, height - padY / 2f + 5);
		for (int i = 0; i < 4; ++i) {
			addActor(scene[i]);
			scene[i].setPosition(width - padX + 4 + i * 110, height - padY);
		}
		addActor(expandedOrder);
		
		//necessary because java hates everyone
		scene[0].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				RopGame.gameScreen.switchScreen(0);
			}
		});
		scene[1].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				RopGame.gameScreen.switchScreen(1);
			}
		});
		scene[2].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				RopGame.gameScreen.switchScreen(2);
			}
		});
		scene[3].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				RopGame.gameScreen.switchScreen(3);
			}
		});
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		score.setText("$" + String.format("%1$.2f", RopGame.score));
	}
	
	// guaranteed that height never changes, so all we have to do is fidget with widths/X.
	public void resize(float width, float height) {
		orderLine.setSize(width - padX, padY);
		background.setPosition(width - padX - 5, 0);
		score.setPosition(width - padX + 5, height - padY / 2f + 10);
		for (int i = 0; i < 4; ++i) {
			scene[i].setPosition(width - padX + 4 + i * 110, height - padY);
		}
		expandedOrder.setPosition(width - padX, 5);
	}
	
	public Image getOrderImage () {
		return expandedOrder;
	}

	@Override
	public void dispose() {
		
	}
}
