package com.kandl.ropgame.model;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Array;

public class Day {
	private final int dayMax;
	private final int dayConcurrent;
	private final Array<String> dayNotes;
	
	public static final Map<Integer, Day> dayMapper = new HashMap<Integer, Day>();
	static {
		dayMapper.put(1, new Day(3,1,(String[]) null));
		dayMapper.put(3, new Day(4,1,"One more person is coming today."));
		dayMapper.put(4, new Day(5,2,"Another person is coming today.","Two groups are coming at a time, now."));
		dayMapper.put(5, new Day(6,2,"Six people are going to visit today."));
		dayMapper.put(6, new Day(6,3,"Three groups are coming at a time."));
		dayMapper.put(7, new Day(7,3,"Another person is visiting today."));
		dayMapper.put(8, new Day(8,3,"One more person is coming today."));
		dayMapper.put(9, new Day(9,3,"Nine people are visiting today."));
		dayMapper.put(10, new Day(10,4,"Ten people are visiting today.","Four groups are coming at a time!","Today's the last day!"));
	}
	
	public Day(int dayMax, int dayConcurrent, String... dayNotes) {
		this.dayMax = dayMax;
		this.dayConcurrent = dayConcurrent;
		if (dayNotes != null) { this.dayNotes = new Array<String>(dayNotes); }
		else { this.dayNotes = new Array<String>(0); }
	}
	
	public int getDayMax() {
		return dayMax;
	}
	
	public int getDayConcurrent() {
		return dayConcurrent;
	}
	
	public Array<String> getDayNotes() {
		return dayNotes;
	}
}
