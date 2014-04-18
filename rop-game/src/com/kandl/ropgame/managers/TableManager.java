package com.kandl.ropgame.managers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kandl.ropgame.RopGame;
import com.kandl.ropgame.model.Group;
import com.kandl.ropgame.ui.UILayer;

public abstract class TableManager {
	public static final int TABLEWIDTH = 640;
	public static final int TABLEHEIGHT = 400;
	public static final int BUTTONHEIGHT = 334;
	
	private static Array<Group> tables = new Array<Group>(5);
	static {
		tables.addAll(new Group[] {null, null, null, null, null});
	}
	
	public static int assignTable(Group g) {
		while (true) {
			int place = (int) (Math.random() * 5);
			if (tables.get(place) != null) continue;
			tables.set(place, g);
			return place;
		}
	}
	
	public static void freeTable(int i) {
		tables.set(i, null);
		GroupManager.freeSeat();
		if (GroupManager.getDayMax() == GroupManager.getDayTotal()) {
			for (int n = 0; n < 5; ++n) {
				if (tables.get(n) != null) return;
			}
			RopGame.gameScreen.endDay();
		}
	}
	
	public static void freeTable(Group g) {
		freeTable(tables.indexOf(g, true));
	}
	
	public static void putTip(final int i, final double tip) {
		final Stage front = RopGame.gameScreen.getScreen(0);
		TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(UILayer.buttonSkin.get("default", TextButton.TextButtonStyle.class));
		final TextButton tipButton = new TextButton("Get tip", style);
		front.addActor(tipButton);
		tipButton.setSize(200, 60);
		tipButton.setPosition(i * TABLEWIDTH + ((float) TABLEWIDTH - 200f) / 2f, TABLEHEIGHT);
		tipButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				RopGame.score += tip;
				final Label tipLabel = new Label("$" + String.format("%1$.2f", tip), UILayer.buttonSkin);
				front.addActor(tipLabel);
				tipLabel.setPosition(tipButton.getX(), tipButton.getY());
				tipButton.remove();
				tipLabel.addAction(Actions.sequence(Actions.parallel(Actions.moveBy(0, 100, 2), Actions.fadeOut(2)),
						Actions.run(new Runnable() {

							@Override
							public void run() {
								tipLabel.remove();
							}
							
						})));
				TableManager.freeTable(i);
			}
			
		});
	}
	
	public static int findTableFromGroup(Group g) {
		return tables.indexOf(g, true);
	}
	
	public static Group findGroupFromPosition(float x, float y) {
		if (y <= 600) return tables.get((int) ((x - RopGame.gameScreen.getOffsetX()) / (float) TABLEWIDTH));
		else return null;
	}
}
