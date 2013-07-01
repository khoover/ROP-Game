package com.kandl.ropgame.model;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.kandl.ropgame.RopGame;
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
	private int score = -1;
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
		TableManager.putTip(table, score / 20d);
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
		score = 100;
	}
	
	public void takeOrder() {
		SheetManager.addOrder(order);
	}
	
	public boolean serve (OrderSheet sheet) {
		if (sheet.getOrder() != order) return false;
		state = Person.EATING;
		stateTime = 0;
		score(order, sheet.getSandwichs());
		return true;
	}
}
