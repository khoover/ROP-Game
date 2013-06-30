package com.kandl.ropgame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.kandl.ropgame.*;
import com.kandl.ropgame.managers.GrillManager;
import com.kandl.ropgame.managers.SheetManager;
import com.kandl.ropgame.view.sandwichView.GrillView;

/** A stage responsible for the entire UI layout, and all assets contained by it.
 * 
 * @author Ken Hoover */
public class UILayer extends Stage implements Disposable {
	private final float padX, padY;
	
	private final Image orderLine;
	private final TextButton confirm;
	private final TextButton trash;
	private final Image clock;
	
	// right panel stuff
	private final Label score;
	private final Button[] scene;
	private final ButtonGroup scenes;
	private final Image background;
	
	private ChangeListener confirmListener;
	private ChangeListener trashListener;
	
	public static final Skin rightPanelSkin = new Skin(Gdx.files.internal("img/icons/buttons.json"));

	public UILayer(int width, int height, boolean stretch) {
		super(width, height, stretch);
		padX = 440;
		padY = 180;
		confirmListener = null;
		trashListener = null;
		
		// set skin up
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
		
		clock = new Image();
		orderLine = new Image(new NinePatchDrawable(rightPanelSkin.getAtlas().createPatch("line")), Scaling.stretchX);
		confirm = new TextButton("", rightPanelSkin.get("accept", TextButton.TextButtonStyle.class));
		trash = new TextButton("Trash", rightPanelSkin.get("trash", TextButton.TextButtonStyle.class));
		
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
		
		addActor(confirm);
		confirm.setSize(300, 50);
		confirm.setPosition(width - (padX + (padX - 300f) / 2f), 70);
		confirm.setVisible(false);
		confirm.addListener(confirmListener);
		addActor(trash);
		trash.setSize(200, 50);
		trash.setPosition(width - (padX + (padX - 300f)/2f), 10);
		trash.setVisible(false);
		trash.addListener(trashListener);
		
		//necessary because java hates everyone
		scene[0].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				RopGame.gameScreen.switchScreen(0);
				SheetManager.setDragable(true);
				trash.setVisible(false);
				confirm.setVisible(false);
			}
		});
		scene[1].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				RopGame.gameScreen.switchScreen(1);
				SheetManager.setDragable(false);
				
				trash.setVisible(true);
				trash.removeListener(trashListener);
				trashListener = new ChangeListener() {

					@Override
					public void changed(ChangeEvent event, Actor actor) {
						RopGame.gameScreen.trashMaking();
					}
					
				};
				trash.addListener(trashListener);
				
				confirm.setVisible(true);
				confirm.setText("Grill");
				confirm.removeListener(confirmListener);
				confirmListener = new ChangeListener() {

					@Override
					public void changed(ChangeEvent event, Actor actor) {
						GrillView v = new GrillView(RopGame.gameScreen.getCurrentMaking().getSandwich(),
								SheetManager.getCurrent().getMini());
						if (GrillManager.assignGrill(v)) {
							RopGame.gameScreen.trashMaking();
							RopGame.gameScreen.switchScreen(2);
							scene[2].setChecked(true);
							scene[1].setChecked(false);
						}
					}
					
				};
				confirm.addListener(confirmListener);
			}
		});
		scene[2].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				RopGame.gameScreen.switchScreen(2);
				SheetManager.setDragable(false);
				trash.setVisible(false);
				confirm.setVisible(false);
			}
		});
		scene[3].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				RopGame.gameScreen.switchScreen(3);
				SheetManager.setDragable(false);
				trash.setVisible(true);
				trash.removeListener(trashListener);
				trashListener = new ChangeListener() {

					@Override
					public void changed(ChangeEvent event, Actor actor) {
						RopGame.gameScreen.trashCutting();
					}
					
				};
				trash.addListener(trashListener);
				
				confirm.setVisible(true);
				confirm.setText("Serve");
				confirm.removeListener(confirmListener);
				confirmListener = new ChangeListener() {

					@Override
					public void changed(ChangeEvent event, Actor actor) {
						if (SheetManager.addSandwich(RopGame.gameScreen.getCurrentCutting().getSandwich())) {
							RopGame.gameScreen.trashCutting();
						}
					}
					
				};
				confirm.addListener(confirmListener);
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
		orderLine.setPosition(0, height - padY);
		background.setPosition(width - padX - 5, 0);
		score.setPosition(width - padX + 5, height - padY / 2f + 10);
		for (int i = 0; i < 4; ++i) {
			scene[i].setPosition(width - padX + 4 + i * 110, height - padY);
		}
		confirm.setPosition(width - (padX - (padX - 300f) / 2f), 70);
		trash.setPosition(width - (padX - (padX - 200f)/2f), 10);
		SheetManager.resize(width, height);
	}
	
	public Button[] getButtons() {
		return scene;
	}

	@Override
	public void dispose() {
		score.getStyle().font.dispose();
		rightPanelSkin.dispose();
	}
}
