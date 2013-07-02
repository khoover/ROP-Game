package com.kandl.ropgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.kandl.ropgame.ui.UILayer;

public class StartScreen implements Screen {
	private Stage stage;
	private Image background;
	private TextButton play, credits;
	
	public StartScreen(final Game g) {
		stage = new Stage(1280, 800, true);
		background = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/backgrounds/coverScreen.png")), 0, 224, 1440, 800)), Scaling.none);
		play = new TextButton("Play", UILayer.buttonSkin);
		play.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				g.setScreen(RopGame.gameScreen);
			}
			
		});
		play.setStyle(new TextButton.TextButtonStyle(play.getStyle()));
		play.getStyle().font.setScale(1.5f);
		stage.addActor(background);
		stage.addActor(play);
		background.setPosition(1280-1440, 0);
		play.setSize(381, 100);
		play.setPosition(820, 382);
		credits = new TextButton("Credits", UILayer.buttonSkin);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		float oldWidth = stage.getWidth();
		stage.setViewport(((float) width/ (float) height) * 800f, 800, true);
		stage.addAction(Actions.moveBy(oldWidth - stage.getWidth(),0));
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		play.getStyle().font.setScale(1);
		dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
