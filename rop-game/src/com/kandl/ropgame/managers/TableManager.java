package com.kandl.ropgame.managers;

import java.util.Random;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kandl.ropgame.GameScreen;

public abstract class TableManager {
	private static final int TABLES = 10;
	private static boolean[] tableOccupied = new boolean[TABLES];
	private static float[] tableTip = new float[TABLES];
	private static final Random rng = new Random();
	public static final int TABLEWIDTH = 100;
	public static final int TABLEPAD = 0;
	
	public static int getTable() {
		int table;
		do { table = rng.nextInt(10); } while (tableOccupied[table]);
		return table;
	}
	
	public static void freeTable(int number) {
		tableOccupied[number] = false;
	}
	
	public static void freeAll() {
		tableOccupied = new boolean[TABLES];
		tableTip = new float[TABLES];
	}
	
	public static void putTip(int table, float tip) {
		tableTip[table] = tip;
		Stage main = GameScreen.screen.getScreen(0);
	}
}
