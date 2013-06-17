package com.kandl.ropgame.model;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.kandl.ropgame.RopGame;
import com.kandl.ropgame.managers.TableManager;
import com.kandl.ropgame.view.Person;

public class Group extends Actor {
	private static Stage front = RopGame.gameScreen.getScreen(0);

	private int table;
	private Array<Person> members;
	private RecipeHolder order;
	private int score = -1;
	private float waitTime;
	private float eatTime;
	
	public Group (Person p1, Person p2) {
		waitTime = 0;
		eatTime = 0;
		table = TableManager.assignTable(this);
		if (p2 == null) {
			members = new Array<Person>(1);
			members.add(p1);
			front.addActor(p1);	
			order = new RecipeHolder(p1.getOrder());
		}
		else {
			members = new Array<Person>(2);
			members.add(p1);
			members.add(p2);
			front.addActor(p1);
			front.addActor(p2);
			order = new RecipeHolder(p1.getOrder(), p2.getOrder());
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if (score != -1 && eatTime >= 10) leave();
	}
	
	public void leave() {
		TableManager.putTip(table, score / 20d);
	}
	
	public void wait(float delta) {
		waitTime += delta;
	}
	
	public void eat(float delta) {
		eatTime += delta;
	}
	
	public void score(RecipeHolder r, Sandwich... sandwichs) {
		score = 100;
	}
	
	public void takeOrder() {
		
	}
	
	public void serve (RecipeHolder r) {
		
	}
}
