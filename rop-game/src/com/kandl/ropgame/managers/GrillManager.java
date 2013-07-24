package com.kandl.ropgame.managers;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.kandl.ropgame.RopGame;
import com.kandl.ropgame.view.sandwichView.GrillView;

public abstract class GrillManager {
	private static Array<Array<GrillView>> spots = new Array<Array<GrillView>> (2);
	static {
		spots.add(new Array<GrillView>(2));
		spots.get(0).add(null);
		spots.get(0).add(null);
		spots.add(new Array<GrillView>(2));
		spots.get(1).add(null);
		spots.get(1).add(null);
	}
	
	public static boolean assignGrill(GrillView v) {
		for (int x = 0; x < 2; ++x) {
			for (int y = 0; y < 2; ++y) {
				if (spots.get(x).get(y) == null) {
					spots.get(x).set(y, v);
					RopGame.gameScreen.getScreen(2).addActor(v);
					v.setPosition(x == 0 ? 211 : 439, y == 0 ? 373 : 198);
					v.setSpot((x+1)+(y*2));
					return true;
				}
			}
		}
		return false;
	}
	
	public static void resume() {
		for (int x = 0; x < 2; ++x) {
			for (int y = 0; y < 2; ++y) {
				if (spots.get(x).get(y) != null) {
					spots.get(x).get(y).resume();
				}
			}
		}
	}
	
	public static void removeFromGrill(GrillView v) {
		v.remove();
		for (Array<GrillView> arr: spots) {
			if (arr.indexOf(v, true) != -1) arr.set(arr.indexOf(v, true), null);
		}
		v.dispose();
	}
}
