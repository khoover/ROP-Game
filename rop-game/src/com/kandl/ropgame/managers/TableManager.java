package com.kandl.ropgame.managers;

import com.badlogic.gdx.utils.Array;
import com.kandl.ropgame.model.Group;

public abstract class TableManager {
	public static final int TABLEWIDTH = 640;
	public static final int BUTTONHEIGHT = 334;
	
	private static Array<Group> tables = new Array<Group>(10);
	
	public static int assignTable(Group g) {
		while (true) {
			int place = ((int) Math.random() * 10);
			if (tables.get(place) != null) continue;
			tables.set(place, g);
			return place;
		}
	}
	
	public static void freeTable(int i) {
		tables.set(i, null);
	}
	
	public static void putTip(int i, double tip) {
		// TODO actually spawn button at desired location and place tip.
	}
	
	public static void freeTable(Group g) {
		freeTable(tables.indexOf(g, true));
	}
	
	public static int findTableFromGroup(Group g) {
		return tables.indexOf(g, true);
	}
	
	public static Group findGroupFromTable(int i) {
		return tables.get(i);
	}
}
