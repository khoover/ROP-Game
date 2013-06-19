package com.kandl.ropgame.ingredients;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Pool;
import com.kandl.ropgame.model.Recipe.CookState;

public class WhiteBread extends Bread {
	
	public static void initialize() {
		Bread.addSubclass(WhiteBread.class);
	}

	@Override
	public Image getTopView(CookState state) {
		switch(state) {
		case UNCOOKED: case LIGHT:
			return new Image(new SpriteDrawable(assets.get("img/food/food.atlas", TextureAtlas.class).createSprite("bread/bread_normal")));
		case MEDIUM:
			return new Image(new SpriteDrawable(assets.get("img/food/food.atlas", TextureAtlas.class).createSprite("bread/bread_brown")));
		case WELL:
			return new Image(new SpriteDrawable(assets.get("img/food/food.atlas", TextureAtlas.class).createSprite("bread/bread_grilled")));
		default:
			return new Image(new SpriteDrawable(assets.get("img/food/food.atlas", TextureAtlas.class).createSprite("bread/bread_burnt")));
		}
	}

	@Override
	public Image getSideView() {
		return new Image(new SpriteDrawable(assets.get("img/food/food.atlas", TextureAtlas.class).createSprite("bigger food/bread_side")));
	}

	@Override
	public Sprite getIcon() {
		return assets.get("img/food/food.atlas", TextureAtlas.class).createSprite("icons/bread");
	}
}
