package com.kandl.ropgame.model;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.kandl.ropgame.RopGame;
import com.kandl.ropgame.managers.TableManager;
import com.kandl.ropgame.view.Person;

public class Group {
	private static Stage front = RopGame.gameScreen.getScreen(0);

	private int table;
	private Array<Person> members;
	
	public Group (Person p1, Person p2) {
		table = TableManager.assignTable(this);
		if (p2 == null) {
			members = new Array<Person>(1);
			members.add(p1);
			front.addActor(p1);	
		}
		else {
			members = new Array<Person>(2);
			members.add(p1);
			members.add(p2);
			front.addActor(p1);
			front.addActor(p2);
		}
	}
}
