package com.kandl.ropgame.view.sandwichView;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kandl.ropgame.model.Recipe.CookState;
import com.kandl.ropgame.model.Sandwich;

public class CutView extends Group {
	private Sandwich sandwich;
	private Image bread;
	
	public CutView(Sandwich s) {
		sandwich = s;
		bread = sandwich.getBread().getTopView(CookState.fromTime(sandwich.getCookTime()));
		bread.setScale(2.22f);
		addActor(bread);
	}
	
	public Sandwich getSandwich() {
		return sandwich;
	}
}
