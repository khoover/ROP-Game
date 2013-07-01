package com.kandl.ropgame.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.kandl.ropgame.RopGame;
import com.kandl.ropgame.ingredients.Ingredient;
import com.kandl.ropgame.managers.SheetManager;
import com.kandl.ropgame.managers.TableManager;
import com.kandl.ropgame.model.Recipe;
import com.kandl.ropgame.model.RecipeHolder;
import com.kandl.ropgame.model.Sandwich;
import com.kandl.ropgame.ui.UILayer;

//45, 130
public class OrderSheet extends Group implements Disposable {
	private Label name;
	private RecipeHolder order;
	private Array<Sandwich> sandwiches;
	private boolean dragable = false, addable = true, servable = false;
	private Image background, cut;
	private Array<Image> foreground;
	private MiniOrderSheet mini;
	
	public OrderSheet() {
	}
	
	public OrderSheet(RecipeHolder r) {
		sandwiches = new Array<Sandwich>(2);
		background = new Image(new TextureRegionDrawable(RopGame.assets.get("img/icons/buttons.atlas", TextureAtlas.class).findRegion("paper")), Scaling.stretch);
		addActor(background);
		background.setPosition(0, 0);
		background.setSize(350, 480);
		
		order = r;
		name = new Label(r.getName() + ", " + r.getTable(), UILayer.rightPanelSkin, "person");
		name.setAlignment(Align.center);
		addActor(name);
		name.setSize(350, 50);
		name.setPosition(0, 430);
		
		foreground = new Array<Image>(3);
		/*Image bread = new Image(new SpriteDrawable(r.getLeftRecipe().getBread().getIcon()));
		foreground.add(bread);
		addActor(bread);
		bread.setPosition(20, 370);*/
		Image current;
		int n = 0;
		for (Ingredient i: r.getLeftRecipe().getIngredients()) {
			current = new Image(new SpriteDrawable(i.getIcon()));
			foreground.add(current);
			addActor(current);
			current.setPosition(20, 370 - n++*60);
		}
		
		if (r.getRightRecipe() != null) {
			Texture t = new Texture(128, 512, Pixmap.Format.RGB888);
			Pixmap p = new Pixmap(10, 420, Pixmap.Format.RGB888);
			p.setColor(Color.BLACK);
			p.fill();
			t.draw(p, 0, 0);
			p.dispose();
			current = new Image(new SpriteDrawable(new Sprite(t, 0, 0, 10, 410)));
			foreground.add(current);
			addActor(current);
			current.setPosition(170, 10);
			
			/*bread = new Image(new SpriteDrawable(r.getRightRecipe().getBread().getIcon()));
			foreground.add(bread);
			addActor(bread);
			bread.setPosition(200, 370);*/
			n = 0;
			for (Ingredient i: r.getRightRecipe().getIngredients()) {
				current = new Image(new SpriteDrawable(i.getIcon()));
				foreground.add(current);
				addActor(current);
				current.setPosition(200, 370 - n++*60);
			}
		}
		
		background.addListener(new DragListener() {
			private float startX, startY, deltaX, deltaY;
			
			@Override
			public void dragStart(InputEvent event, float x, float y, int pointer) {
				if (!OrderSheet.this.dragable) return;
				startX = OrderSheet.this.getX();
				startY = OrderSheet.this.getY();
				deltaX = event.getStageX() - startX;
				deltaY = event.getStageY() - startY;
			}
			
			@Override
			public void drag(InputEvent event, float x, float y, int pointer) {
				if (!OrderSheet.this.dragable) return;
				OrderSheet.this.setPosition(event.getStageX() - deltaX, event.getStageY() - deltaY);
			}
			
			@Override
			public void dragStop(InputEvent event, float x, float y, int pointer) {
				if (!OrderSheet.this.dragable) return;
				com.kandl.ropgame.model.Group g = TableManager.findGroupFromPosition(event.getStageX(), event.getStageY());
				if (g == null) OrderSheet.this.addAction(Actions.moveTo(startX, startY));
				else {
					if (servable && g.serve(OrderSheet.this)) {
						SheetManager.removeSheet(OrderSheet.this);
						OrderSheet.this.dispose();
					} else {
						OrderSheet.this.addAction(Actions.moveTo(startX, startY));
					}
				}
			}
		});
	}
	
	public void setDragable(boolean v) {
		dragable = v;
	}

	@Override
	public void dispose() {
		((TextureRegionDrawable) background.getDrawable()).getRegion().getTexture().dispose();
		for (Image i: foreground) {
			if (i instanceof Disposable) ((Disposable)  i).dispose();
		}
	}
	
	public boolean addSandwich(Sandwich s) {
		boolean value = true;
		if (addable) sandwiches.add(s);
		else value = false;
		if (sandwiches.size == 2 || order.getRightRecipe() == null) {
			addable = false;
			servable = true;
			background.setColor(Color.GREEN);
			mini.setBackgroundColor(Color.GREEN);
		}
		return value;
	}
	
	public void setMini(MiniOrderSheet m) {
		mini = m;
	}
	
	public MiniOrderSheet getMini() {
		return mini;
	}
	
	public RecipeHolder getOrder() {
		return order;
	}

	public Array<Sandwich> getSandwichs() {
		return sandwiches;
	}

	public Image getCut() {
		return cut;
	}
}
