package com.kandl.ropgame.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kandl.ropgame.RopGame;

/** A table responsible for the entire UI layout, and all assets contained by it.
 * 
 * @author Ken Hoover */
public class UITable extends Table {

	public UITable() {
		// TODO Auto-generated constructor stub
		super();
		if (RopGame.DEBUG) super.debug();
	}
}
