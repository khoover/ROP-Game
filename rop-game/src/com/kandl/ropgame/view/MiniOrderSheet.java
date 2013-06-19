package com.kandl.ropgame.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.kandl.ropgame.ingredients.Ingredient;
import com.kandl.ropgame.managers.SheetManager;
import com.kandl.ropgame.model.Recipe;

public class MiniOrderSheet extends Group implements Disposable {
	private static final LabelStyle normal = new LabelStyle(new BitmapFont(), Color.WHITE);
	
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
		name = new Label(base.getOrder().getName(), normal);
		addActor(name);
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
		Pixmap p = new Pixmap(350, 480, Pixmap.Format.RGB888);
		p.setColor(Color.WHITE);
		p.fill();
		Texture t = new Texture(512, 512, Pixmap.Format.RGB888);
		t.draw(p, 0, 0);
		p.dispose();
		background = new Image(new TextureRegion(t, 0, 0, 350, 480));
		background.setScale(scale);
		addActor(background);
		
		Sprite currSprite = base.getOrder().getLeftRecipe().getBread().getIcon();
		Image current = new Image(new SpriteDrawable(currSprite));
		current.setScale(scale);
		components.add(current);
		addActor(current);
		current.setPosition(10, height - 50 * scale);
		int n = 1;
		for (Ingredient i: base.getOrder().getLeftRecipe().getIngredients()) {
			currSprite = i.getIcon();
			current = new Image(new SpriteDrawable(currSprite));
			current.setScale(scale);
			components.add(current);
			addActor(current);
			current.setPosition(10, height - scale * (50 * ++n));
		}
		current = new ProgressBar(Recipe.CookState.toTime(base.getOrder().getLeftRecipe().getCooked()));
		current.setScale(scale);
		components.add(current);
		addActor(current);
		current.setPosition(10, height - scale * (50 * ++n));
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

	@Override
	public void dispose() {
		((TextureRegionDrawable) background.getDrawable()).getRegion().getTexture().dispose();
	}

	public MiniOrderSheet getSource() {
		return source;
	}
}
