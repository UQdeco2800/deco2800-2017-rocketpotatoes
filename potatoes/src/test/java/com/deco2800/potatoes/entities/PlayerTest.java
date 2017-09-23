package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.Player.PlayerState;
import com.deco2800.potatoes.managers.PlayerManager;

import org.junit.Test;
import org.junit.Before;
import com.badlogic.gdx.Input;

public class PlayerTest {
	Player player;

	@Before
	public void setup() {
		player = new Player();
	}
	
	@Test
	public void directionTest() {
		player.getDirection();
	}

	@Test
	public void keysTest(){
		player.handleKeyDown(Input.Keys.W);
		player.handleKeyUp(Input.Keys.W);
		player.handleKeyDown(Input.Keys.S);
		player.handleKeyUp(Input.Keys.S);
		player.handleKeyDown(Input.Keys.A);
		player.handleKeyUp(Input.Keys.A);
		player.handleKeyDown(Input.Keys.D);
		player.handleKeyUp(Input.Keys.D);
	}

	@Test
	public void stringTest(){
		player.toString();
		player.getProgressBar();
	}
	
	@Test
	public void damageTest() {
		player.addState(PlayerState.damaged);
		player.hasState(PlayerState.damaged);
	}
	
	@Test
	public void checkKeyDownTest() {
		player.handleKeyUp(Input.Keys.W);
	}
	
}