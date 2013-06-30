package com.kandl.ropgame.managers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.kandl.ropgame.RopGame;
import com.kandl.ropgame.model.RecipeHolder;
import com.kandl.ropgame.model.Sandwich;
import com.kandl.ropgame.view.MiniOrderSheet;
import com.kandl.ropgame.view.OrderSheet;

public abstract class SheetManager {
	private static float width = 840, height = 800, miniWidth = 80;
	private static final float padX = 440, padY = 180;
	private static int current = 0, number = -1;
	private static boolean dragable = true;
	private static Stage UI;
	private static OrderSheet currentSheet = new OrderSheet();
	private static Array<MiniOrderSheet> miniSheets = new Array<MiniOrderSheet>(10);
	private static Image downArrow = new Image(new TextureRegionDrawable(RopGame.assets.get("img/icons/buttons.atlas", TextureAtlas.class).findRegion("triangle_down")));
	
	public static void resize(float width, float height) {
		SheetManager.width = width - padX;
		SheetManager.height = height;
		miniWidth = (SheetManager.width - 40f) / 10f;
		layout();
	}
	
	public static void addOrder(RecipeHolder r) {
		number++;
		MiniOrderSheet m = new MiniOrderSheet(new OrderSheet(r), miniWidth - 10, false, null);
		miniSheets.add(m);
		UI.addActor(m);
		m.addAction(Actions.moveTo(10 + miniWidth * (number) + (miniWidth - m.getLogicWidth()) / 2f, height - padY + 35));
		switchTo(number);
		current = number;
	}
	
	public static void shiftRight() {
		if (current >= number) return;
		switchTo(++current);
	}
	
	public static void shiftLeft() {
		if (current <= 0) return;
		switchTo(--current);
	}
	
	public static void removeSheet(OrderSheet o) {
		number--;
		o.getMini().remove();
		miniSheets.removeValue(o.getMini(), true);
		if (current > number) current = number;
		if (current < 0) { current = 0; currentSheet.remove(); currentSheet = new OrderSheet(); }
		else {
			switchTo(current);
		}
		layout();
	}
	
	public static void switchTo(int i) {
		currentSheet.remove();
		currentSheet.setDragable(false);
		downArrow.setPosition(miniWidth * ((float) i + 0.5f), height - 30);
		currentSheet = miniSheets.get(i).getOrder();
		currentSheet.setDragable(dragable);
		UI.addActor(currentSheet);
		currentSheet.addAction(Actions.moveTo(width + 45, 130));
	}
	
	public static void switchTo(MiniOrderSheet o) {
		if (miniSheets.indexOf(o, true) != -1) switchTo(miniSheets.indexOf(o, true));
	}
	
	public static void layout() {
		int i = 0;
		for (MiniOrderSheet m: miniSheets) {
			m.resize(miniWidth - 10);
			m.addAction(Actions.moveTo(10 + miniWidth * i++ + (miniWidth - m.getLogicWidth()) / 2f, height - padY + 35));
		}
		currentSheet.addAction(Actions.moveTo(width + 45, 130));
	}
	
	public static void setDragable(boolean b) {
		dragable = b;
		currentSheet.setDragable(b);
	}
	
	public static boolean addSandwich(Sandwich s) {
		return currentSheet.addSandwich(s);
	}
	
	public static void initialize(Stage ui) {
		UI = ui;
		UI.addActor(downArrow);
		downArrow.setPosition(10 + miniWidth - downArrow.getWidth() - (miniWidth - downArrow.getWidth()) / 2f, height - 30);
	}
	
	public static OrderSheet getCurrent() {
		return currentSheet;
	}
}
