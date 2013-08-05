package com.kandl.ropgame.datamodel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kandl.ropgame.RopGame;
import com.kandl.ropgame.ingredients.Ingredient;
import com.kandl.ropgame.view.Person;

public class ModelSandwich {
	private static ExecutorService output = Executors.newSingleThreadScheduledExecutor();
	private static FileHandle outputFile;
	private static int nextID = 1;
	
	public final int ID;
	
	private boolean finalized = false;
	private final Array<Ingredient> ingredients;
	private final Array<Vector2> cuts, positions;
	private ModelPerson person;
	
	public ModelSandwich(Array<Ingredient> i, Array<Vector2> c, Array<Vector2> p) {
		ingredients = i;
		cuts = c;
		positions = p;
		ID = nextID++;
	}
	
	public static void create() {
		FileHandle temp = Gdx.files.external("RopGame/" + RopGame.gameScreen.ID);
		temp.mkdirs();
		resume();
		outputFile = Gdx.files.external("RopGame/" + RopGame.gameScreen.ID + "/sandwiches.csv");
		output.execute(new Runnable() {
			@Override
			public void run() {
				outputFile.writeString("SANDWICH ID,PERSON ID,INGREDIENTS,CUTS,POSITIONS", false);
			}
		});
	}
	
	public void close() {
		if (!finalized) {
			finalized = true;
			output.execute(new Runnable() {
				@Override
				public void run() {
					String toWrite = "\r\n" + ID + "," + person.ID + ",";
					for (int n = 0; n < ingredients.size; ++n) {
						if (n != 0) toWrite = toWrite + " ";
						Ingredient i = ingredients.get(n);
						toWrite = toWrite + i.getClass().getSimpleName();
					}
					toWrite = toWrite + ",";
					for (int i = 0; i < cuts.size; ++i) {
						if (i != 0) toWrite = toWrite + " ";
						Vector2 v = cuts.get(i);
						toWrite = toWrite + v.x + ":" + v.y;
					}
					toWrite = toWrite + ",";
					for (int i = 0; i < positions.size; ++i) {
						if (i != 0) toWrite = toWrite + " ";
						Vector2 v = positions.get(i);
						toWrite = toWrite + v.x + ":" + v.y;
					}
					outputFile.writeString(toWrite, true);
				}
			});
		}
	}
	
	public void setPerson(Person p) {
		person = p.getModel();
	}
	
	public static void pause() {
		output.shutdown();
	}
	
	public static void resume() {
		output = Executors.newSingleThreadExecutor();
	}
	
	public static void dispose() {
		output.shutdownNow();
	}
}
