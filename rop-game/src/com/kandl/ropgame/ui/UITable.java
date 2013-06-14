package com.kandl.ropgame.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kandl.ropgame.*;

/** A table responsible for the entire UI layout, and all assets contained by it.
 * 
 * @author Ken Hoover */
public class UITable extends Table {
	private final Image OrderLine;
	
	// top right corner stuff
	private final Label Score;
	private final Button[] Scene;
	private final ButtonGroup Scenes;
	private final Table ButtonTable;
	private final VerticalGroup RightCorner;
	private final Image Tab;
	
	private final Image RightBackground;
	private final Image ExpandedOrder;	
	private final Image Clock;

	public UITable() {
		// TODO Auto-generated constructor stub
		super();
		if (RopGame.DEBUG) super.debug();
		
		// test nulls
		ButtonTable = null;
		RightCorner = null;
		RightBackground = null;
		OrderLine = new Image();
		Score = new Label("$" + String.format("%1$.2f", RopGame.Score), new Label.LabelStyle(RopGame.assets.get("font.fnt", BitmapFont.class), Color.WHITE));
		Scenes = new ButtonGroup();
		Scene = new Button[4];
		for (int i = 0; i < 4; ++i) {
			Scene[i] = new Button((Drawable) null, null, null);
			Scenes.add(Scene[i]);
		}
		
		//necessary because java hates everyone
		Scene[0].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameScreen.switchScreen(0);
			}
		});
		Scene[1].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameScreen.switchScreen(1);
			}
		});
		Scene[2].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameScreen.switchScreen(2);
			}
		});
		Scene[3].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameScreen.switchScreen(3);
			}
		});
		
		Tab = new Image();
		ExpandedOrder = null;
		Clock = new Image();
		
		//actual layout now
		row().height(180);
		add(OrderLine).expandX().fill();
		add(Score).width(440).fill();
		row().expandY();
		add(Clock).expandX().bottom().left().padBottom(10).padLeft(10);
		add(ExpandedOrder).width(440).fill();
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		Score.setText("$" + String.format("%1$.2f", RopGame.Score));
	}
	
	public void resize(float width, float height) {
		int width1 = (int) (330 * (height / 600f));
		int height1 = (int) (135 * (height / 600f));
		getCell(Score).size(width1, height1).fill();
		getCell(ExpandedOrder).width(width1).fill();
		getCell(OrderLine).height(height1).fill();
		invalidateHierarchy();
	}
}
