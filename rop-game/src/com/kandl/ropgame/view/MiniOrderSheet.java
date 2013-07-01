package com.kandl.ropgame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.kandl.ropgame.RopGame;
import com.kandl.ropgame.ingredients.Ingredient;
import com.kandl.ropgame.managers.SheetManager;
import com.kandl.ropgame.model.Recipe;

public class MiniOrderSheet extends Group {
	private static final LabelStyle normal = new LabelStyle(new BitmapFont(Gdx.files.internal("fonts/mini.fnt"), false), Color.BLACK);
	static {
		normal.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
	}
	
	private final float targetHeight = 85;
	private float width, height;
	private OrderSheet base;
	private Label name;
	private Image background;
	private Array<Image> components;
	private float scale;
	private MiniOrderSheet source;
	
	public MiniOrderSheet (OrderSheet base, float width, boolean copy, MiniOrderSheet source) {
		components = new Array<Image>(2);
		float ratio = 170f / 420f;
		if (ratio * targetHeight > width) { this.width = width; this.height = (1f / ratio) * width; scale = this.width / 170f; }
		else { this.height = targetHeight; this.width = ratio * height; scale = this.height / 420f; }
		this.base = base;
		name = new Label(base.getOrder().getName() + ", " + base.getOrder().getTable(), normal);
		addActor(name);
		name.setAlignment(Align.center);
		name.setSize(350 * scale, 20);
		name.setPosition(0, -30);
		if (!copy) { 
			this.addListener(new ClickListener() {
				@Override
				public void clicked (InputEvent e, float x, float y) {
					SheetManager.switchTo(MiniOrderSheet.this);
				}
			});
			base.setMini(this);
			source = null;
		} else {
			this.source = source; 
		}
		background = new Image(new TextureRegionDrawable(RopGame.assets.get("img/icons/buttons.atlas", TextureAtlas.class).findRegion("paper")), Scaling.stretch);
		background.setSize(350,480);
		background.setScale(scale);
		addActor(background);
		
		Sprite currSprite;// = base.getOrder().getLeftRecipe().getBread().getIcon();
		Image current;// = new Image(new SpriteDrawable(currSprite));
		/*current.setScale(scale);
		components.add(current);
		addActor(current);
		current.setPosition(10, height - 50 * scale);*/
		int n = 0;
		for (Ingredient i: base.getOrder().getLeftRecipe().getIngredients()) {
			currSprite = i.getIcon();
			current = new Image(new SpriteDrawable(currSprite));
			current.setScale(scale * 2);
			System.out.println(scale * current.getWidth() + ", " + scale * background.getWidth());
			components.add(current);
			addActor(current);
			current.setPosition(10, height - scale * 2 * (50 * ++n));
		}
	}
	
	public MiniOrderSheet copy() {
		return new MiniOrderSheet(base, width, true, this);
	}
	
	public float getLogicWidth() {
		return width;
	}
	
	public void resize(float width) {
		if (this.width == width) return;
		float ratio = 170f / 420f;
		if (ratio * targetHeight > width) {
			this.width = width;
			this.height = (1f / ratio) * width;
			scale = width / 170f;
		} else {
			this.height = targetHeight;
			this.width = ratio * height;
			scale = height / 420f;
		}
		background.setScale(scale);
		
		int n = 0;
		for (Image i: components) {
			i.setScale(scale);
			i.setPosition(10, height - scale * (50 * ++n));
		}
	}
	
	public OrderSheet getOrder() {
		return base;
	}

	public void setBackgroundColor(Color color) {
		background.setColor(color);
	}

	public MiniOrderSheet getSource() {
		return source;
	}
}
