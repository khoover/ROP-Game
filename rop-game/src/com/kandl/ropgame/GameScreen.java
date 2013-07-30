package com.kandl.ropgame;

/*
 * HOW libgdx VIEWPORTS WORK, AND HOW THE SCENE DOES THEM:
 * Ok, this is so I can remember what the hell I was doing in the future.
 * The Viewport is the "size" of any particular part of the stage being rendered, in terms of the stage.
 * 		- ie. if the viewport is 1x1, then the things created in the stage get a percentage of the final screen = size*100.
 * What the camera then does is it scales the whole thing to match the device.
 * So, how does this impact my UI? Welp, glad you asked. See, what I -WANT- to happen is this:
 * 		- If 1024x600 (native), then all's well, no scaling/stretching needed.
 * 		- If 1280x800 (10.1"), then the right column should scale, and the left column should scale THEN stretch.
 * 			- Only one actual IMAGE will have to stretch, and it'll be defined as .9, all good; the other is just in a location in a cell,
 * 			  scale automagically and be reflowed.
 * Where's the problem? Don't want the right column to stretch, but I want the left column to. The problem, then, is this; how do I
 * define the viewport such that it takes up the full screen, BUT only one column fucks up when that happens. In essence, tell the UI "world"
 * to shrink when I upscale. (narrow).
 * 
 * Possible solution: Viewport goes to either 1280x800 or 1024x600, depending on aspect ratio of resize; 1024/600 > 1.7, so, should be safe
 * to use 1.7 as the delimiter for 1.6 or not. What should happen, then, is the UI should tell it's table to reflow itself, using viewport
 * size (ie. table resize, validate(), I think), and all to-do is replace right column width with 280, instead of 210. Left is still fill,
 * so when reflow happens, it'll stretch to take up space. Similarly, bottom row...Oh. Well then...Ah, fuck it, just run with it for now.
 */

/*
 * Drag and Drop, in scenes.scene2d.util. Will love them, use them for the UI recipes, for making the sandwich...
 * Really nothing I can't do. Incredibly nice. 
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.kandl.ropgame.ingredients.Bacon;
import com.kandl.ropgame.ingredients.Chicken;
import com.kandl.ropgame.ingredients.Cucumber;
import com.kandl.ropgame.ingredients.Fish;
import com.kandl.ropgame.ingredients.Ham;
import com.kandl.ropgame.ingredients.Ingredient;
import com.kandl.ropgame.ingredients.Lettuce;
import com.kandl.ropgame.ingredients.Onion;
import com.kandl.ropgame.ingredients.Pork;
import com.kandl.ropgame.ingredients.Tomato;
import com.kandl.ropgame.ingredients.WhiteBread;
import com.kandl.ropgame.managers.GrillManager;
import com.kandl.ropgame.managers.GroupManager;
import com.kandl.ropgame.managers.SheetManager;
import com.kandl.ropgame.model.Sandwich;
import com.kandl.ropgame.model.Day;
import com.kandl.ropgame.ui.UILayer;
import com.kandl.ropgame.view.Person;
import com.kandl.ropgame.view.ProgressBar;
import com.kandl.ropgame.view.sandwichView.CutView;
import com.kandl.ropgame.view.sandwichView.MakeView;

public class GameScreen implements Screen{
	private MakeView currentMaking;
	private CutView currentCutting = null;
	private Target makeTarget;
	private DragAndDrop makeDrag;
	private final UILayer UILayer;
	private final Stage[] Scene = new Stage[4];
	private Stage ActiveScene, dayStage;
	private Image frontBackground;
	private float offsetX;
	private Array<Ingredient> ingredients = new Array<Ingredient>(5);
	private Music frontMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/front_music.ogg"));
	private Music grillMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/grill_music.ogg"));
	private boolean dayOver = true;
	private InputProcessor input;

	private Array<Label> dayNotes = new Array<Label>(2);
	private Label dayHeader;
	private Label dayScore;
	private int day;
	private double yesterdayScore;
	private TextButton dayContinue;
	
	public float getOffsetX() {
		return offsetX;
	}
	
	public GameScreen() {
		loadAll();
		
		//Create UI, input processors
		UILayer = new UILayer(1280, 800, true);
		SheetManager.initialize(UILayer);
		frontMusic.setLooping(true);
		frontMusic.setVolume(0.5f);
		grillMusic.setLooping(true);
		grillMusic.setVolume(0.75f);
		
		Chicken.initialize(ingredients);
		Lettuce.initialize(ingredients);
		Onion.initialize(ingredients);
		Tomato.initialize(ingredients);
		Bacon.initialize(ingredients);
		Cucumber.initialize(ingredients);
		Fish.initialize(ingredients);
		Ham.initialize(ingredients);
		Pork.initialize(ingredients);
		WhiteBread.initialize();
		Ingredient.loadAll();
		
		//Create scenes
		Scene[0] = createFrontScreen();
		Scene[1] = createMakeScreen();
		Scene[2] = createGrillScreen();
		Scene[3] = createCutScreen();
		
		dayStage = new Stage(1280, 800, true, UILayer.getSpriteBatch());
		Pixmap background = new Pixmap(128,128,Pixmap.Format.RGBA4444);
		background.setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, 0.8f);
		background.fill();
		dayStage.addActor(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(background))), Scaling.stretch));
		dayStage.getActors().get(0).setSize(dayStage.getWidth(), dayStage.getHeight());
		background.dispose();
		yesterdayScore = 0;
		day = 0;
		dayHeader = new Label("", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/big.fnt"), false), Color.WHITE));
		dayScore = new Label("", dayHeader.getStyle());
		dayContinue = new TextButton("Continue", com.kandl.ropgame.ui.UILayer.buttonSkin);
		dayStage.addActor(dayHeader);
		dayStage.addActor(dayScore);
		dayStage.addActor(dayContinue);
		dayHeader.setPosition(0, 640);
		dayHeader.setSize(1280, 160);
		dayHeader.setAlignment(Align.center);
		dayScore.setPosition(0, 300);
		dayScore.setSize(1280, 160);
		dayScore.setAlignment(Align.center);
		dayContinue.setPosition((dayStage.getWidth() - dayContinue.getWidth()) / 2f, 20);
		
		Person.initialize();
		ActiveScene = Scene[0];
	}

	private Stage createCutScreen() {
		Stage scene = new Stage(1280, 800, true, UILayer.getSpriteBatch());
		Texture background = RopGame.assets.get("img/backgrounds/cuttingBoard.png", Texture.class);
		background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		scene.addActor(new Image(new TextureRegionDrawable(new TextureRegion(background, 0, 224, 1280, 800))));
		for (Actor a: scene.getActors()) {
			a.setPosition(-50, 0);
		}
		return scene;
	}
	
	private Stage createGrillScreen() {
		Stage scene = new Stage(1280, 800, true, UILayer.getSpriteBatch());
		Texture background = RopGame.assets.get("img/backgrounds/Grills.png", Texture.class);
		background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		scene.addActor(new Image(new TextureRegionDrawable(new TextureRegion(background, 0, 224, 1280, 800))));
		for (Actor a: scene.getActors()) {
			a.setPosition(0, 0);
		}
		return scene;
	}

	private Stage createMakeScreen() {
		makeDrag = new DragAndDrop();
		makeDrag.setDragActorPosition(-300, 50);
		Stage scene = new Stage(1280, 800, true, UILayer.getSpriteBatch());
		Texture background = RopGame.assets.get("img/backgrounds/Making.png", Texture.class);
		background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		scene.addActor(new Image(new TextureRegionDrawable(new TextureRegion(background, 0, 224, 1280, 800))));
		for (Actor a: scene.getActors()) {
			a.setPosition(-60, 0);
		}
		int n = 0;
		int height = 520;
		for (final Ingredient i: ingredients) {
			Image current = new Image(new SpriteDrawable(i.getIcon()), Scaling.none);
			scene.addActor(current);
			current.setPosition(10 + 170 * n++, height);
			if (n > 5) { n = 0; height = 400; }
			current.setSize(current.getWidth(), 140);
			makeDrag.addSource(new Source(current) {

				@Override
				public Payload dragStart(InputEvent event, float x, float y,
						int pointer) {
					Payload p = new Payload();
					p.setDragActor(i.getSideView());
					p.setObject(i);
					return p;
				}
				
			});
		}
		currentMaking = new MakeView(new Sandwich(new WhiteBread()));
		scene.addActor(currentMaking);
		currentMaking.setPosition(100, 146);
		makeTarget = new Target(currentMaking) {

			@Override
			public boolean drag(Source source, Payload payload, float x,
					float y, int pointer) {
				return !((MakeView) getActor()).isFull();
			}

			@Override
			public void drop(Source source, Payload payload, float x, float y,
					int pointer) {
				((MakeView) getActor()).addIngredient((Ingredient) payload.getObject()); 
			}
			
		};
		makeDrag.addTarget(makeTarget);
		return scene;
	}

	private Stage createFrontScreen() {
		Stage scene = new Stage(1280, 800, true, UILayer.getSpriteBatch());
		
		// Set listener that moves front world
		scene.addListener(new DragListener() {
			@Override
			public void drag (InputEvent event, float x, float y, int pointer) {
				float dx = -getDeltaX();
				if (Scene[0].getRoot().getX() + dx >= 0) { Scene[0].addAction(Actions.moveTo(0, 0)); offsetX = 0;}// if root left edge enters stage
				else if (Scene[0].getRoot().getX() + dx <= Scene[0].getWidth() - 445 - frontBackground.getWidth()) { // if root right edge is left of background + UI coverage
					Scene[0].addAction(Actions.moveTo(Scene[0].getWidth()- 445 - frontBackground.getWidth(), 0));
					offsetX = Scene[0].getWidth() - 445 - frontBackground.getWidth();
				}
				else { Scene[0].addAction(Actions.moveBy(dx, 0, 0)); offsetX += dx; }
			}
		});
		frontBackground = new Image(new TiledDrawable(new TextureRegion(RopGame.assets.get("img/backgrounds/new front.png", Texture.class), 0, 224, 1280, 800)), Scaling.stretch);
		((TiledDrawable) frontBackground.getDrawable()).getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		scene.addActor(frontBackground);
		frontBackground.setSize(1280 * 2.5f, 800);
		scene.addActor(new GroupManager());
		
		int n = 1;
		while (n <= 5) {
			Label l = new Label(Integer.toString(n), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/default.fnt"), false), Color.WHITE));
			l.getStyle().font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			scene.addActor(l);
			l.setSize(61, 70);
			l.setAlignment(Align.center);
			l.setPosition((n-1)/2 * 1280 + 640 + (n % 2 == 0 ? 296 : -357), 209);
			++n;
		}
		return scene;
	}

	public void switchScreen(final int screen) {
		if (RopGame.DEBUG) assert(screen >= 0 && screen <= 3);
		frontMusic.setVolume(0);
		grillMusic.setVolume(0);
		((InputMultiplexer) Gdx.input.getInputProcessor()).removeProcessor(ActiveScene);
		if (screen == 0) frontMusic.setVolume(0.5f);
		else if (screen == 2) grillMusic.setVolume(0.75f);
		ActiveScene = Scene[screen];
		((InputMultiplexer) Gdx.input.getInputProcessor()).addProcessor(ActiveScene);
	}

	@Override
	public void render(float delta) {
		if (dayOver) {
			dayStage.act(delta);
		} else {
			for (Stage s: Scene) {
				s.act(delta);
			}
			UILayer.act(delta);
		}
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		ActiveScene.draw();
		UILayer.draw();
		if (dayOver) dayStage.draw();
	}

	@Override
	public void resize(int width, int height) {
		float length = (float) width * 800f / (float) height;
		UILayer.setViewport(length, 800, true);
		UILayer.resize(length, 800);
		dayStage.setViewport(length, 800, true);
		dayHeader.setWidth(length);
		dayScore.setWidth(length);
		dayContinue.setPosition((length - dayContinue.getWidth()) / 2f, dayContinue.getY());
		for (Stage s: Scene) {
			s.setViewport(length, 800, true);
		}
	}

	@Override
	public void show() {
		InputMultiplexer input = new InputMultiplexer();
		input.addProcessor(UILayer);
		input.addProcessor(ActiveScene);
		this.input = input;
		Gdx.input.setInputProcessor(dayStage);
		prepDay();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
		if (RopGame.DEBUG) Gdx.app.log("life-cycle", "Pausing.");
	}

	@Override
	public void resume() {
		if (RopGame.DEBUG) Gdx.app.log("life-cycle", "Resuming.");
		Pixmap background = new Pixmap(128,128,Pixmap.Format.RGBA4444);
		background.setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, 0.75f);
		background.fill();
		((TextureRegionDrawable) ((Image) dayStage.getActors().get(0)).getDrawable()).setRegion(new TextureRegion(new Texture(background)));
		background.dispose();
		UILayer.resume();
		GrillManager.resume();
	}
	
	public void endDay() {
		dayOver = true;
		UILayer.act(0);
		frontMusic.stop();
		grillMusic.stop();
		Gdx.input.setInputProcessor(dayStage);
		dayScore.setVisible(false);
		dayContinue.setVisible(false);
		dayContinue.setText("Continue");
		dayContinue.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				prepDay();
				GameScreen.this.dayContinue.removeListener(this);
			}
			
		});
		dayHeader.setText(String.format("Day %1$d over!", day));
		dayScore.setText("Today, you earned $" + String.format("%1$.2f", RopGame.score - yesterdayScore) + ".");
		dayScore.addAction(Actions.delay(2, Actions.visible(true)));
		if (day == 10) {
			Label gameOver = new Label("Game Over, thanks for playing!", dayHeader.getStyle());
			dayStage.addActor(gameOver);
			gameOver.setPosition(0, 200);
			gameOver.setSize(dayStage.getWidth(), 160);
			gameOver.setAlignment(Align.center);
			gameOver.setVisible(false);
			gameOver.addAction(Actions.delay(3, Actions.visible(true)));
		}
		else {
			dayContinue.addAction(Actions.delay(3, Actions.visible(true)));
		}
	}
	
	public void startDay() {
		for (Label l: dayNotes) {
			l.remove();
		}
		dayNotes.clear();
		dayOver = false;
		frontMusic.play();
		grillMusic.play();
		Gdx.input.setInputProcessor(input);
	}
	
	public void prepDay() {
		++day;
		Day today = Day.dayMapper.get(day);
		yesterdayScore = RopGame.score;
		GroupManager.setDayTotal(0);
		if (today != null) {
			GroupManager.setDayMax(today.getDayMax());
			GroupManager.setMax(today.getDayConcurrent());
			for (String s: today.getDayNotes()) {
				dayNotes.add(new Label(s, new Label.LabelStyle(com.kandl.ropgame.ui.UILayer.buttonSkin.getFont("score"), Color.WHITE)));
			}
			if (dayNotes.size != 0) {
				for (int i = 0; i < dayNotes.size; ++i) {
					dayStage.addActor(dayNotes.get(i));
					dayNotes.get(i).setPosition(0, 500 - i * 90);
					dayNotes.get(i).setSize(dayStage.getWidth(), 90);
					dayNotes.get(i).setAlignment(Align.center);
				}
			}
		}
		UILayer.act(0);
		dayScore.setVisible(false);
		dayHeader.setText(String.format("Day %1$d", day));
		dayContinue.setText("Start");
		dayContinue.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				startDay();
				GameScreen.this.dayContinue.removeListener(this);
			}
			
		});
	}
	
	@Override
	public void dispose() {
		if (RopGame.DEBUG) Gdx.app.log("life-cycle", "Disposing.");
		for (Stage s: Scene) {
			s.dispose();
		}
		ActiveScene = null;
		UILayer.dispose();
		dayStage.dispose();
		frontMusic.dispose();
		grillMusic.dispose();
		Ingredient.dispose();
		Person.dispose();
		ProgressBar.staticDispose();
	}
	
	public Stage getScreen(int stage) {
		return Scene[stage];
	}
	
	public Stage getUI() {
		return UILayer;
	}
	
	public void trashMaking() {
		currentMaking.remove();
		makeDrag.removeTarget(makeTarget);
		initMaking();
	}

	public void initMaking() {
		currentMaking = new MakeView(new Sandwich(new WhiteBread()));
		Scene[1].addActor(currentMaking);
		currentMaking.setPosition(100, 146);
		makeTarget = new Target(currentMaking) {
			@Override
			public boolean drag(Source source, Payload payload, float x,
					float y, int pointer) {
				return !((MakeView) getActor()).isFull();
			}

			@Override
			public void drop(Source source, Payload payload, float x, float y,
					int pointer) {
				((MakeView) getActor()).addIngredient((Ingredient) payload.getObject()); 
			}
			
		};
		makeDrag.addTarget(makeTarget);
	}
	
	public void initCutting(CutView c) {
		currentCutting = c;
		Scene[3].addActor(currentCutting);
		currentCutting.setPosition(65, 43);
		switchScreen(3);
		UILayer.getButtons()[3].setChecked(true);
		UILayer.getButtons()[2].setChecked(false);
	}

	public void trashCutting() {
		currentCutting.remove();
		currentCutting = null;
	}

	public MakeView getCurrentMaking() {
		return currentMaking;
	}

	public CutView getCurrentCutting() {
		return currentCutting;
	}
	
	private void loadAll() {
		RopGame.assets.load("img/backgrounds/new front.png", Texture.class);
		RopGame.assets.load("img/backgrounds/cuttingBoard.png", Texture.class);
		RopGame.assets.load("img/backgrounds/Grills.png", Texture.class);
		RopGame.assets.load("img/backgrounds/Making.png", Texture.class);
		RopGame.assets.load("img/icons/buttons.atlas", TextureAtlas.class);
		RopGame.assets.finishLoading();
	}
}
