package com.kandl.ropgame.managers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kandl.ropgame.model.Group;
import com.kandl.ropgame.view.Person;

public class GroupManager extends Actor {
	private final int MAX = 3;
	private int current = 0;
	private float spawnGap = 0;
	private float despawnGap = 0;
	
	public void createGroup() {
		current++;
		spawnGap = 0;
		Person p1 = new Person(Math.random() >= 0.5, Person.SITTING, false);
		Person p2 = null;
		if (Math.random() >= 0.5) p2 = new Person(Math.random() >= 0.5, Person.SITTING, true);
		Group g = new Group(p1, p2);
	}
	
	@Override
	public void act(float delta) {
		spawnGap += delta;
		despawnGap += delta;
		if (spawnGap >= 30 && despawnGap >= 10 && current < MAX) createGroup();
	}
	
	public void despawn() {
		current--;
		despawnGap = 0;
	}
}
