package com.kandl.ropgame.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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
	private final Image Tab;
	
	private final Image ExpandedOrder;
	
	private final Image Clock;

	public UITable() {
		// TODO Auto-generated constructor stub
		super();
		if (RopGame.DEBUG) super.debug();
		
		// test nulls
		OrderLine = new Image();
		Score = null;
		Scenes = null;
		Scene = new Button[4];
		Tab = new Image();
		ExpandedOrder = null;
		Pixmap test = new Pixmap(128, 128, Pixmap.Format.RGB888);
		test.setColor(Color.WHITE);
		test.fillRectangle(0, 0, 128, 128);
		Clock = new Image(new Sprite(new Texture(test)));
		Clock.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent e, float x, float y, int a, int b) {
				System.out.println("Clicked");
				return true;
			}
		});
		test.dispose();
		
		//actual layout now
		this.row().height(135);
		this.add(OrderLine).expandX().fill();
		this.add(Tab).width(330).fill();
		this.row().expandY();
		this.add(Clock).expandX().bottom().left().padBottom(10).padLeft(10);
		this.add(ExpandedOrder).width(330).fill();
	}
	
	public void resize(float width, float height) {
		int width1 = (int) (330 * (height / 600f));
		int height1 = (int) (135 * (height / 600f));
		this.getCell(Tab).size(width1, height1).fill();
		this.getCell(ExpandedOrder).width(width1).fill();
		this.getCell(OrderLine).height(height1).fill();
		this.invalidateHierarchy();
	}
}
