package com.kandl.ropgame.datamodel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.kandl.ropgame.RopGame;
import com.kandl.ropgame.model.Recipe;
import com.kandl.ropgame.model.Sandwich;

public class ModelPerson {
	private static ExecutorService output = Executors.newSingleThreadScheduledExecutor();
	private static FileHandle outputFile;
	private static int nextID = 1;
	
	public final int ID;
	
	private boolean finalized = false;
	private ModelRecipe recipe;
	private ModelSandwich sandwich;
	private double time,make,cut;
	
	public ModelPerson(Recipe r, Sandwich s) {
		ID = nextID++;
		recipe = r.getModel();
		if (s != null) sandwich = s.getModel();
	}
	
	public void finalize() {
		if (!finalized) {
			finalized = true;
			output.execute(new Runnable() {
				@Override
				public void run() {
					outputFile.writeString("\r\n" + ID + "," + recipe.ID + "," + sandwich.ID + "," + time + "," +
											make + "," + cut, true);
				}
			});
			recipe.finalize();
			sandwich.finalize();
		}
	}
	
	public void setSandwich(Sandwich sandwich) {
		this.sandwich = sandwich.getModel();
	}

	public static void create() {
		FileHandle temp = Gdx.files.external("RopGame/" + RopGame.gameScreen.ID);
		temp.mkdirs();
		resume();
		outputFile = Gdx.files.external("RopGame/" + RopGame.gameScreen.ID + "/people.csv");
		output.execute(new Runnable() {
			@Override
			public void run() {
				outputFile.writeString("PERSON ID,RECIPE ID,SANDWICH ID,TIME SCORE,MAKE SCORE,CUT SCORE", false);
			}
		});
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

	public void setTime(double time) {
		this.time = time;
	}

	public void setMake(double make) {
		this.make = make;
	}

	public void setCut(double cut) {
		this.cut = cut;
	}
}
