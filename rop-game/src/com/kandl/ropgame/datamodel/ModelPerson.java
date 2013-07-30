package com.kandl.ropgame.datamodel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.kandl.ropgame.RopGame;

public class ModelPerson {
	private static ExecutorService output = Executors.newSingleThreadScheduledExecutor();
	private static FileHandle outputFile;
	public ModelPerson() {
		output.execute(new Runnable() {
			@Override
			public void run() {
				outputFile.writeString("Test", true);
			}
		});
	}
	
	public static void create() {
		FileHandle temp = Gdx.files.external("RopGame/" + RopGame.gameScreen.ID);
		temp.mkdirs();
		resume();
		outputFile = Gdx.files.external("RopGame/" + RopGame.gameScreen.ID + "/people.csv");
		output.execute(new Runnable() {
			@Override
			public void run() {
				outputFile.writeString("", false);
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
}
