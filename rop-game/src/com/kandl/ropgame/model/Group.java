package com.kandl.ropgame.model;

import java.util.Arrays;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.kandl.ropgame.RopGame;
import com.kandl.ropgame.ingredients.Ingredient;
import com.kandl.ropgame.managers.GroupManager;
import com.kandl.ropgame.managers.SheetManager;
import com.kandl.ropgame.managers.TableManager;
import com.kandl.ropgame.ui.UILayer;
import com.kandl.ropgame.view.OrderSheet;
import com.kandl.ropgame.view.Person;

public class Group extends Actor {
	private Stage front;
	private static final int SITTINGHEIGHT = 97;
	private static final int SITTINGLEFT = 108;
	private static final int SITTINGRIGHT = 352;

	private int table;
	private Array<Person> members;
	private RecipeHolder order;
	private double score = -1;
	private float waitTime;
	private float eatTime;
	
	private int state;
	private float stateTime;
	private Image p1;
	private Image p2;
	private TextureRegionDrawable p1Drawable, p2Drawable;
	private Label p1Label;
	private Label p2Label;
	
	public Group (Person p1, Person p2) {
		front = RopGame.gameScreen.getScreen(0);
		front.addActor(this);
		this.setSize(640, 600);
		state = Person.SITTING;
		stateTime = 0;
		waitTime = 0;
		eatTime = 0;
		table = TableManager.assignTable(this);
		super.setPosition(table * TableManager.TABLEWIDTH, 0);
		if (p2 == null) {
			this.p2 = null;
			p2Label = null;
			p2Drawable = null;
			members = new Array<Person>(1);
			members.add(p1);
			p1Label = new Label(p1.getName(), UILayer.rightPanelSkin, "person");
			this.p1 = new Image(p1.getFrame(state, stateTime));
			p1Drawable = (TextureRegionDrawable) this.p1.getDrawable();
			front.addActor(this.p1);
			front.addActor(p1Label);
			this.p1.setPosition(this.getX() + SITTINGLEFT, (state != Person.WALKING ? SITTINGHEIGHT : 0));
			p1Label.setPosition(this.p1.getX() + (this.p1.getWidth() - p1Label.getWidth())/2f, this.p1.getY() + this.p1.getHeight() + 10);
			order = new RecipeHolder(p1.getName(), table + 1, p1.getOrder());
		}
		else {
			members = new Array<Person>(2);
			members.add(p1);
			members.add(p2);
			p1Label = new Label(p1.getName(), UILayer.rightPanelSkin, "person");
			p2Label = new Label(p2.getName(), UILayer.rightPanelSkin, "person");
			this.p2 = new Image(p2.getFrame(state, stateTime));
			this.p1 = new Image(p1.getFrame(state, stateTime));
			p2Drawable = (TextureRegionDrawable) this.p2.getDrawable();
			p1Drawable = (TextureRegionDrawable) this.p1.getDrawable();
			front.addActor(this.p1);
			front.addActor(this.p2);
			front.addActor(p1Label);
			front.addActor(p2Label);
			this.p1.setPosition(this.getX() + SITTINGLEFT, (state != Person.WALKING ? SITTINGHEIGHT : 0));
			this.p2.setPosition(this.getX() + SITTINGRIGHT, (state != Person.WALKING ? SITTINGHEIGHT : 0));
			p1Label.setPosition(this.p1.getX() + (this.p1.getWidth() - p1Label.getWidth())/2f, this.p1.getY() + this.p1.getHeight() + 10);
			p2Label.setPosition(this.p1.getX() + (this.p2.getWidth() - p2Label.getWidth())/2f, this.p2.getY() + this.p2.getHeight() + 10);
			order = new RecipeHolder(p1.getName(), table + 1, p1.getOrder(), p2.getOrder());
		}
		final TextButton getOrder = new TextButton("Take Order", UILayer.rightPanelSkin);
		front.addActor(getOrder);
		getOrder.setSize(200, 60);
		getOrder.setPosition(table * TableManager.TABLEWIDTH + ((float) TableManager.TABLEWIDTH - 200f) / 2f, TableManager.BUTTONHEIGHT);
		getOrder.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				getOrder.remove();
				Group.this.takeOrder();
			}
			
		});
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		stateTime += delta;
		p1Drawable.setRegion(members.get(0).getFrame(state, stateTime));
		p1.setPosition(this.getX() + SITTINGLEFT, (state != Person.WALKING ? SITTINGHEIGHT : 0));
		p1Label.setPosition(p1.getX() + (p1.getWidth() - p1Label.getWidth())/2f, p1.getY() + p1.getHeight() + 10);
		if (p2 != null) {
			p2.setPosition(getX() + SITTINGRIGHT, (state != Person.WALKING ? SITTINGHEIGHT : 0));
			p2Label.setPosition(p2.getX() + (p2.getWidth() - p2Label.getWidth())/2f, p2.getY() + p2.getHeight() + 10);
			p2Drawable.setRegion(members.get(1).getFrame(state, stateTime));
		}
		if (state == Person.SITTING) waitTime += delta;
		if (state == Person.EATING) eatTime += delta;
		if (score != -1 && eatTime >= 10) leave();
	}
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		p1.setPosition(this.getX() + SITTINGLEFT, (state != Person.WALKING ? SITTINGHEIGHT : 0));
		p1Label.setPosition(p1.getX() + (p1.getWidth() - p1Label.getWidth())/2f, p1.getY() + p1.getHeight() + 10);
		if (p2 != null) {
			p2.setPosition(getX() + SITTINGRIGHT, (state != Person.WALKING ? SITTINGHEIGHT : 0));
			p2Label.setPosition(p1.getX() + (p2.getWidth() - p2Label.getWidth())/2f, p2.getY() + p2.getHeight() + 10);
		}
	}
	
	public void leave() {
		GroupManager.despawn();
		TableManager.putTip(table, (double) Math.round((2.5d + score / 40d) * 100d) / 100d);
		members.get(0).free();
		p1.remove();
		p1Label.remove();
		if (p2 != null) {
			p2.remove();
			p2Label.remove();
			members.get(1).free();
		}
		this.remove();
	}
	
	public void score(RecipeHolder r, Array<Sandwich> sandwichs) {
		double score = 0;
		final float targetTime = 10*2 + 10 + 50 + 10;
		
		// score time
		score = Math.max(0, Math.min(50, 50 - (waitTime - targetTime)));
		
		// score sandwiches
		if (r.getRightRecipe() == null) {
			score += scoreSandwich(r.getLeftRecipe(), sandwichs.get(0)) / 2d;
		} else {
			double[][] scores = new double[2][2];
			scores[0][0] = scoreSandwich(r.getLeftRecipe(), sandwichs.get(0));
			scores[1][0] = scoreSandwich(r.getRightRecipe(), sandwichs.get(0));
			scores[0][1] = scoreSandwich(r.getLeftRecipe(), sandwichs.get(1));
			scores[1][1] = scoreSandwich(r.getRightRecipe(), sandwichs.get(1));
			score += Math.max(scores[0][0] + scores[1][1], scores[1][0] + scores[0][1]) / 4d;
		}
		
		this.score = score;
	}
	
	// returns 0 - 100
	public double scoreSandwich(Recipe r, Sandwich s) {
		double score = 0;
		score += scoreMake(r, s) / 2d;
		score += scoreCuts(r, s) / 2d;
		return score;
	}
	
	// returns 0 - 100
	public double scoreMake(Recipe r, Sandwich s) {
		double score = 100;
		Array<Ingredient> recipeIngredients = r.getIngredients();
		Array<Ingredient> sandwichIngredients = s.getIngredients();
		if (recipeIngredients.size != sandwichIngredients.size) {
			score = Math.max(0, score - 50 * Math.abs(recipeIngredients.size - sandwichIngredients.size));
			if (score == 0) return score;
		}
		Array<Class<? extends Ingredient>> recipeClasses = new Array<Class<? extends Ingredient>>(recipeIngredients.size);
		Array<Class<? extends Ingredient>> sandwichClasses = new Array<Class<? extends Ingredient>>(sandwichIngredients.size);
		for (Ingredient i: recipeIngredients) {
			recipeClasses.add(i.getClass());
		}
		recipeClasses.reverse();
		for (Ingredient i: sandwichIngredients) {
			sandwichClasses.add(i.getClass());
		}
		
		Array<Integer> used = new Array<Integer>(sandwichClasses.size);
		for (int i = 0; i < recipeClasses.size; ++i) {
			boolean contains = false;
			int delta = 20;
			int pos = -1;
			for (int j = 0; j < sandwichClasses.size; ++j) {
				if (used.contains(Integer.valueOf(j), false)) continue;
				if (sandwichClasses.get(j).equals(recipeClasses.get(i))) {
					contains = true;
					if (i == j) {
						delta = 0;
						pos = j;
						break;
					} else if (Math.abs(i - j) < delta) {
						delta = Math.abs(i - j);
						pos = j;
					}
				}
			}
			used.add(pos);
			score = Math.max(0, (contains ? score - delta*5 : score - 30));
		}
		
		return score;
	}
	
	// returns 0 - 100
	public double scoreCuts(Recipe r, Sandwich s) {
		double score = 0;
		Array<Vector2> recipeCuts = r.getCut();
		Array<Vector2> recipePositions = r.getPos();
		Array<Vector2> sandwichCuts = s.getCuts();
		Array<Vector2> sandwichPositions = s.getPositions();
		assert(recipeCuts.size == recipePositions.size && sandwichCuts.size == sandwichPositions.size);
		if (recipeCuts.size != sandwichCuts.size) {
			score = Math.max(0, score - 50 * Math.abs(recipeCuts.size - sandwichCuts.size));
			if (score == 0) return score;
		}
		double[][] scores = new double[4][sandwichCuts.size];
		for (double[] a: scores) {
			Arrays.fill(a, 0);
		}
		for (int i = 0; i < recipeCuts.size; ++i) {
			for (int j = 0; j < sandwichCuts.size; ++j) {
				scores[i][j] = compareCuts(new Vector2(recipeCuts.get(i)), new Vector2(recipePositions.get(i)), 
						new Vector2(sandwichCuts.get(j)), new Vector2(sandwichPositions.get(j)));
			}
		}
		
		for (int i = 0; i < sandwichCuts.size; ++i) {
			double currentScore = scores[0][i];
			int used = 1;
			for (int j = 0; j < sandwichCuts.size; ++j) {
				if (j != i) { currentScore += scores[1][j]; ++used; }
				for (int k = 0; k < sandwichCuts.size; ++k) {
					if (k != j || k != i) { currentScore += scores[2][k]; ++used; }
					for (int l = 0; l < sandwichCuts.size; ++l) {
						if (l != k || l != j || l != i) { currentScore += scores[3][l]; ++used; }
						if(used == recipeCuts.size && currentScore > score) score = currentScore / (double) used;
						if (l != k || l != j || l != i) { currentScore -= scores[3][l]; --used; }
					}
					if (k != j || k != i) { currentScore -= scores[2][k]; ++ used; }
				}
				if (j != i) { currentScore -= scores[1][j]; --used; }
			}
		}
		return score;
	}
	
	public double compareCuts(Vector2 recipeDir, Vector2 recipePos, Vector2 sandwichDir, Vector2 sandwichPos) {
		double score = 100;
		float posDelta = recipePos.sub(sandwichPos).len();
		float angleDelta = Math.abs(recipeDir.angle() - (sandwichDir.angle() == 360 ? 0 : sandwichDir.angle()));
		score = Math.max(0, score - Math.max(0, posDelta - 0.05) * 100);
		score = Math.max(0, score - Math.max(0, angleDelta - 10) / 1.7d);
		return score;
	}
	
	public void takeOrder() {
		SheetManager.addOrder(order);
	}
	
	public boolean serve (final OrderSheet sheet) {
		if (sheet.getOrder() != order) return false;
		state = Person.EATING;
		stateTime = 0;
		new Thread(new Runnable() {

			@Override
			public void run() {
				score(order, sheet.getSandwichs());
			}
			
		}).start();
		return true;
	}
}
