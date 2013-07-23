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
		dayMapper.put(2, new Day(4,1,"One more person is coming today."));
		dayMapper.put(3, new Day(5,2,"Another person is coming today.","Two groups are coming at a time, now."));
	}
	
	public Day(int dayMax, int dayConcurrent, String... dayNotes) {
		this.dayMax = dayMax;
		this.dayConcurrent = dayConcurrent;
		this.dayNotes = new Array<String>(dayNotes);
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
