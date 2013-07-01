package com.kandl.ropgame.view.sandwichView;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.kandl.ropgame.model.Recipe.CookState;
import com.kandl.ropgame.model.Sandwich;

public class CutView extends Group {
	private Sandwich sandwich;
	private Image bread, dragUI;
	private Group cuts, dragImages;
	
	public CutView(Sandwich s) {
		cuts = new Group();
		dragImages = new Group();
		sandwich = s;
		bread = sandwich.getBread().getTopView(CookState.fromTime(sandwich.getCookTime()));
		bread.setScale(2.22f);
		setSize(bread.getWidth() + 200, bread.getHeight() + 200);
		dragUI = new Image();
		dragUI.setSize(bread.getWidth() + 400, bread.getHeight() + 400);
		dragUI.addListener(new DragListener() {
			
			@Override
			public void dragStart(InputEvent e, float x, float y, int pointer) {
				System.out.println("Started");
			}
			
			@Override
			public void drag(InputEvent e, float x, float y, int pointer) {
				System.out.println("Dragging");
			}
			
			@Override
			public void dragStop(InputEvent e, float x, float y, int pointer) {
				System.out.println("Stopped");
			}
		});
		addActor(bread);
		addActor(cuts);
		addActor(dragImages);
		addActor(dragUI);
		bread.setPosition(100, 100);
		
	}
	
	public Sandwich getSandwich() {
		return sandwich;
	}
}
