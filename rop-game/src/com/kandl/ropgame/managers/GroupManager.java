package com.kandl.ropgame.managers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kandl.ropgame.RopGame;
import com.kandl.ropgame.model.Group;
import com.kandl.ropgame.view.Person;

public class GroupManager extends Actor {
	private static int dayMax = 3;
	private static int dayTotal = 0;
	private static int max = 1;
	private static int current = 0;
	private float spawnGap = 15;
	private static float despawnGap = 10;
	
	public void createGroup() {
		if (dayTotal++ >= dayMax) {--dayTotal; return;}
		current++;
		spawnGap = 0;
		Person p1 = new Person(Math.random() >= 0.5, true);
		Person p2 = null;
		//if (Math.random() >= 0.5) p2 = new Person(Math.random() >= 0.5, false);
		Group g = new Group(p1, p2);
	}
	
	@Override
	public void act(float delta) {
		spawnGap += delta;
		despawnGap += delta;
		if (spawnGap >= (RopGame.DEBUG ? 0 : 20) && despawnGap >= 5 && current < max) createGroup();
	}
	
	public static void despawn() {
		current--;
		despawnGap = 0;
	}
	
	public static void setMax(int n) {
		max = n;
	}
	
	public static void setDayMax(int n) {
		dayMax = n;
	}

	public static int getDayTotal() {
		return dayTotal;
	}

	public static void setDayTotal(int dayTotal) {
		GroupManager.dayTotal = dayTotal;
	}

	public static int getDayMax() {
		return dayMax;
	}
}
