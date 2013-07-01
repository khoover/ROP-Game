package com.kandl.ropgame.view.sandwichView;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.kandl.ropgame.RopGame;
import com.kandl.ropgame.managers.GrillManager;
import com.kandl.ropgame.managers.SheetManager;
import com.kandl.ropgame.model.Recipe.CookState;
import com.kandl.ropgame.model.Sandwich;
import com.kandl.ropgame.view.MiniOrderSheet;
import com.kandl.ropgame.view.ProgressBar;

public class GrillView extends Group implements Disposable {
	private ProgressBar bar;
	private Sandwich model;
	private MiniOrderSheet order;
	private Image breadImage;
	
	public GrillView(Sandwich s, final MiniOrderSheet o) {
		model = s;
		breadImage = s.getBread().getTopView(CookState.UNCOOKED);
		breadImage.addListener(new ClickListener() {
			@SuppressWarnings("unused")
			@Override
			public void clicked(InputEvent e, float x, float y) {
				if (!RopGame.DEBUG && (bar.getTime() < 40 || RopGame.gameScreen.getCurrentCutting() != null)) return;
				RopGame.gameScreen.initCutting(new CutView(model));
				SheetManager.switchTo(o);
				GrillManager.removeFromGrill(GrillView.this);
			}
		});
		addActor(breadImage);
		order = o.copy();
		addActor(order);
		bar = new ProgressBar();
		addActor(bar);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		float old = model.getCookTime();
		model.cook(delta);
		if (CookState.fromTime(model.getCookTime()) != CookState.fromTime(old)) {
			breadImage.remove();
			breadImage = model.getBread().getTopView(CookState.fromTime(model.getCookTime()));
			breadImage.addListener(new ClickListener() {
				@SuppressWarnings("unused")
				@Override
				public void clicked(InputEvent e, float x, float y) {
					if (!RopGame.DEBUG && (bar.getTime() < 40 || RopGame.gameScreen.getCurrentCutting() != null)) return;
					RopGame.gameScreen.initCutting(new CutView(model));
					SheetManager.switchTo(order.getSource());
					GrillManager.removeFromGrill(GrillView.this);
				}
			});
			addActor(breadImage);
		}
	}
	
	public void setSpot(int position) {
		assert(position >= 1 && position <= 4);
		switch(position) {
		case 1:
			bar.setPosition(0, 170);
			order.setPosition(-90, 0);
			break;
		case 2:
			bar.setPosition(0, 170);
			order.setPosition(170, 0);
			break;
		case 3:
			bar.setPosition(0, -60);
			order.setPosition(-90, 0);
			break;
		case 4:
			bar.setPosition(0, -60);
			order.setPosition(170, 0);
		}
	}

	@Override
	public void dispose() {
		bar.dispose();
		order.dispose();
	}
}
