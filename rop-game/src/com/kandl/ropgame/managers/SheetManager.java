package com.kandl.ropgame.managers;

import com.badlogic.gdx.utils.Array;
import com.kandl.ropgame.view.OrderSheet;

public abstract class SheetManager {
	private static float width, height;
	private static OrderSheet currentSheet = null;
	private static Array<OrderSheet> miniSheets = new Array<OrderSheet>(10);
	
	public static void resize(float width, float height) {
		SheetManager.width = width;
		SheetManager.height = height;
	}
}
