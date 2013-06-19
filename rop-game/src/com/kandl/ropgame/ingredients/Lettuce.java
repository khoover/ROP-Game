package com.kandl.ropgame.ingredients;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;

public class Lettuce extends Ingredient {
	public static void initialize(Array<Ingredient> ingredients) {
		Ingredient.addSubclass(Lettuce.class);
		ingredients.add(new Lettuce());
	}

	@Override
	public Image getSideView() {
		// TODO Auto-generated method stub
		return new Image(new SpriteDrawable(assets.get("img/food/food.atlas", TextureAtlas.class).createSprite("icons/lettuce")));
	}

	@Override
	public Sprite getIcon() {
		return assets.get("img/food/food.atlas", TextureAtlas.class).createSprite("icons/lettuce");
	}
}
