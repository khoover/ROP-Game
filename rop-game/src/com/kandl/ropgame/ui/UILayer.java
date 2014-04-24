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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
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
import com.kandl.ropgame.managers.GroupManager;
import com.kandl.ropgame.managers.SheetManager;
import com.kandl.ropgame.view.sandwichView.GrillView;

/** A stage responsible for the entire UI layout, and all assets contained by it.
 * 
 * @author Ken Hoover */
public class UILayer extends Stage implements Disposable {
	public final float padX, padY;
	
	private Image orderLine;
	private final TextButton confirm;
	private final TextButton trash;
	private final Label count;
	
	// right panel stuff
	private final Label score;
	private final Button[] scene;
	private final ButtonGroup scenes;
	private Image button_background;
	private Image sheet_background;
	
	private ChangeListener confirmListener;
	private ChangeListener trashListener;
	
	public static final Skin buttonSkin = new Skin(Gdx.files.internal("img/icons/buttons.json"));
	static {
		for (BitmapFont f: buttonSkin.getAll(BitmapFont.class).values()) {
			f.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		}
	}

	public UILayer(int width, int height, boolean stretch) {
		super(width, height, stretch);
		padX = 440;
		padY = 180;
		confirmListener = null;
		trashListener = null;
		
		// set skin up
		button_background = new Image(new NinePatchDrawable(buttonSkin.getAtlas().createPatch("panel_bg")), Scaling.stretchY);
		sheet_background = new Image(new NinePatchDrawable(buttonSkin.getAtlas().createPatch("panel_bg")), Scaling.stretchY); 
		
		// create components of corner table
		score = new Label("$" + String.format("%1$.2f", RopGame.score), new Label.LabelStyle(buttonSkin.getFont("score"), Color.YELLOW));
		scenes = new ButtonGroup();
		scene = new Button[4];
		for (int i = 0; i < 4; ++i) {
			scene[i] = new Button((Drawable) null);
			scenes.add(scene[i]);
			scene[i].setSize(102, 102);
		}
		scene[0].setStyle(buttonSkin.get("front", Button.ButtonStyle.class));
		scene[1].setStyle(buttonSkin.get("make", Button.ButtonStyle.class));
		scene[2].setStyle(buttonSkin.get("grill", Button.ButtonStyle.class));
		scene[3].setStyle(buttonSkin.get("cut", Button.ButtonStyle.class));
		
		count = new Label(String.format("%1$d/%2$d", GroupManager.getDayTotal(), GroupManager.getDayMax()), new Label.LabelStyle(score.getStyle()));
		count.getStyle().fontColor = Color.WHITE;
		orderLine = new Image(new NinePatchDrawable(buttonSkin.getAtlas().createPatch("line")), Scaling.stretchX);
		confirm = new TextButton("", buttonSkin.get("accept", TextButton.TextButtonStyle.class));
		trash = new TextButton("Trash", buttonSkin.get("trash", TextButton.TextButtonStyle.class));
		
		// add the components. ORDERING IMPORTANT
		addActor(count);
		count.setPosition(5, 5);
		addActor(orderLine);
		orderLine.setPosition(0, height - padY);
		orderLine.setSize(width - padX, padY);
		addActor(button_background);
		button_background.setPosition(width - padX - 5, height - padY);
		button_background.setSize(padX + 5, padY);
		addActor(sheet_background);
		sheet_background.setPosition(width - padX - 5, 0);
		sheet_background.setSize(padX + 5, height - padY);
		addActor(score);
		score.setPosition(width - padX + 5, height - padY / 2f + 5);
		for (int i = 0; i < 4; ++i) {
			addActor(scene[i]);
			scene[i].setPosition(width - padX + 4 + i * 110, height - padY);
		}
		
		addActor(confirm);
		confirm.setSize((padX - 20f)/2f - 5f, 100);
		confirm.setPosition(width - padX + 10, 10);
		confirm.setVisible(false);
		confirm.addListener(confirmListener);
		addActor(trash);
		trash.setSize(confirm.getWidth(), confirm.getHeight());
		trash.setPosition(confirm.getX() + confirm.getWidth() + 10, 10);
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
						if (SheetManager.getCurrent().getMini() == null) {
							Label l = new Label("No order to serve.", score.getStyle());
							l.setColor(Color.WHITE);
							RopGame.gameScreen.getScreen(1).addActor(l);
							l.setSize(840, 90);
							l.setAlignment(Align.center);
							l.setPosition(0, 400);
							l.addAction(Actions.sequence(Actions.parallel(Actions.moveBy(0, 100, 1), Actions.fadeOut(1)), 
									Actions.removeActor()));
							return;
						}
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
						if (RopGame.gameScreen.getCurrentCutting() == null ) {
							return;
						}
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
						if (RopGame.gameScreen.getCurrentCutting() == null || RopGame.gameScreen.getCurrentCutting().getSandwich() == null) {
							return;
						}
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
		count.setText(String.format("%1$d/%2$d", GroupManager.getDayTotal(), GroupManager.getDayMax()));
	}
	
	// guaranteed that height never changes, so all we have to do is fidget with widths/X.
	public void resize(float width, float height) {
		orderLine.remove();
		orderLine = new Image(new NinePatchDrawable(buttonSkin.getAtlas().createPatch("line")), Scaling.stretchX);
		this.getRoot().addActorAt(1, orderLine);
		orderLine.setSize(width - padX, padY);
		orderLine.setPosition(0, height - padY);
		button_background.setPosition(width - padX - 5, height - padY);
		sheet_background.setPosition(width - padX - 5, 0);
		score.setPosition(width - padX + 5, height - padY / 2f + 10);
		for (int i = 0; i < 4; ++i) {
			scene[i].setPosition(width - padX + 4 + i * 110, height - padY);
		}
		confirm.setSize((padX - 20f)/2f - 5f, 100);
		confirm.setPosition(width - padX + 10, 10);
		trash.setSize(confirm.getWidth(), confirm.getHeight());
		trash.setPosition(confirm.getX() + confirm.getWidth() + 10, 10);
		SheetManager.resize(width, height);
	}
	
	public Button[] getButtons() {
		return scene;
	}
}
