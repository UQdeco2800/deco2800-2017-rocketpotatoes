package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.entities.Player;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import com.badlogic.gdx.Input;

public class PlayerTest {
	Player player;

	@Before
	public void setup() {
		player = new Player();
	}

	@Test
	public void directionTest(){
		player.getPlayerDirection();
	}

	@Test
	public void keysTest(){
		player.handleKeyDown(Input.Keys.W);
		player.handleKeyDown(Input.Keys.S);
		player.handleKeyDown(Input.Keys.A);
		player.handleKeyDown(Input.Keys.D);
		player.handleKeyDown(Input.Keys.Q);
	}

	@Test
	public void stringTest(){
		player.toString();
		player.getProgressBar();
	}
	@Test
	public void damageTest(){
		player.isDamaged();
		player.setDamaged(true);
	}

}