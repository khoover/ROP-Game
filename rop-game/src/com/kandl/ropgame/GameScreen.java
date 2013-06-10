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
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.Scaling;
import com.kandl.ropgame.ui.UITable;

public class GameScreen implements Screen{
	public static final int FRONT = 0;
	public static final int INGREDIENTS = 1;
	public static final int GRILL = 2;
	public static final int ASSEMBLY = 3;
	
	private final Stage UILayer;
	private final UITable UItable;
	private final Stage[] Screen = new Stage[4];
	private static Stage ActiveScreen;
	public static GameScreen screen;
	
	public GameScreen() {
		UILayer = new Stage(1024, 600, true);
		InputMultiplexer input = new InputMultiplexer();
		input.addProcessor(UILayer);
		input.addProcessor(new GameProcessor());
		Gdx.input.setInputProcessor(input);
		UItable = new UITable();
		UItable.setFillParent(true);
		UILayer.addActor(UItable);
		for (int i = 0; i < 4; ++i) { Screen[i] = new Stage(1024, 600, true, UILayer.getSpriteBatch()); }
		ActiveScreen = Screen[0];
		Pixmap GameTest = new Pixmap(1024, 1024, Pixmap.Format.RGBA8888);
			GameTest.setColor(Color.YELLOW);
			GameTest.fillRectangle(0, 212, 1024, 600);
		ActiveScreen.addActor(new Image(new SpriteDrawable(new Sprite(new Texture(GameTest), 0, 212, 1024, 600)), Scaling.fill));
		GameTest.dispose();
	}
	
	public static void switchScreen(int screen) {
		assert(screen >= 0 && screen <= 3);
		ActiveScreen = GameScreen.screen.Screen[screen];
	}

	@Override
	public void render(float delta) {
		ActiveScreen.act(delta);
		UILayer.act(delta);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		ActiveScreen.draw();
		UILayer.draw();
		UITable.drawDebug(UILayer);
	}

	@Override
	public void resize(int width, int height) {
		final int UIlength = (float) width / (float) height < 1.7 ? 1280 : 1024;
		final int UIheight = UIlength == 1280 ? 800 : 600;
		if (UIheight != UILayer.getHeight()) {
			UItable.resize(UIlength, UIheight);
		}
		System.out.println("UI: " + UIlength);
		UILayer.setViewport(UIlength, UIheight, true);
		final int Gamelength = UIlength == 1280 ? 960 : 1024;
		for (Stage s: Screen) {
			s.setViewport(Gamelength, 600, true);
		}
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Gets called when this goes to background. So, what?
	}

	@Override
	public void resume() {
		// TODO add call to showPauseOverlay

	}

	@Override
	public void dispose() {
		for (Stage s: Screen) {
			s.dispose();
		}
		ActiveScreen = null;
		UILayer.dispose();
	}
	
	private final class GameProcessor implements InputProcessor {

		@Override
		public boolean keyDown(int keycode) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer,
				int button) {
			// TODO Auto-generated method stub
			return ActiveScreen.touchDown(screenX, screenY, pointer, button);
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			// TODO Auto-generated method stub
			return ActiveScreen.touchUp(screenX, screenY, pointer, button);
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			// TODO Auto-generated method stub
			return ActiveScreen.touchDragged(screenX, screenY, pointer);
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean scrolled(int amount) {
			// TODO Auto-generated method stub
			return false;
		}
	}
}
