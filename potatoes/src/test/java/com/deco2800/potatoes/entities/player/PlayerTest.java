package com.deco2800.potatoes.entities.player;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.Direction;
import com.deco2800.potatoes.entities.player.Player.PlayerState;
import com.deco2800.potatoes.managers.CameraManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.worlds.ForestWorld;
import com.deco2800.potatoes.worlds.WorldType;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


import org.junit.After;
import org.junit.Before;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * JUnit tests for validating the Player class
 */
public class PlayerTest extends BaseTest {
	Player player;
	Player movingPlayer;
	long tick = 1;

	/**
	 * Test setup
	 */
	@Before
	public void setup() {
		player = new Player();
		player = new Player(1, 1);
		GameManager.get().clearManagers();
		PlayerManager m = new PlayerManager();
		CameraManager cameraManager = new CameraManager();
		WorldManager worldManager = new WorldManager();
		OrthographicCamera camera = new OrthographicCamera();
		GameManager.get().addManager(m);
		GameManager.get().addManager(worldManager);
		GameManager.get().getManager(WorldManager.class).setWorld(ForestWorld.get());
		GameManager.get().getManager(CameraManager.class).setCamera(new OrthographicCamera());
		cameraManager.setCamera(camera);

		m.setPlayer(player);
	}

	@After
	public void cleanUp() {
		GameManager.get().clearManagers();
		player = null;
		movingPlayer = null;
	}

	/**
	 * Test that the player returns its current direction;
	 */
	@Test
	public void directionTest() {
		player.getFacing(); // Test getting the direction

		player.setPosition(0, 0); 				// Set the player to the origin

		player.handleKeyDown(Input.Keys.D);					// Tell the player the D key is pressed
		assertTrue(player.getFacing().equals(Direction.E)); // The player should be moving screen East

		player.handleKeyDown(Input.Keys.W);
		assertTrue(player.getFacing().equals(Direction.NE));

		player.handleKeyUp(Input.Keys.D);
		assertTrue(player.getFacing().equals(Direction.N));

		player.handleKeyDown(Input.Keys.A);
		assertTrue(player.getFacing().equals(Direction.NW));

		player.handleKeyUp(Input.Keys.W);
		assertTrue(player.getFacing().equals(Direction.W));

		player.handleKeyDown(Input.Keys.S);
		assertTrue(player.getFacing().equals(Direction.SW));

		player.handleKeyUp(Input.Keys.A);
		assertTrue(player.getFacing().equals(Direction.S));

		player.handleKeyDown(Input.Keys.D);
		assertTrue(player.getFacing().equals(Direction.SE));
	}

	/**
	 * Test the player changing states
	 */
	@Test
	public void stateTest() {
		// Test to see if in idle by default
		assertTrue(player.getState() == PlayerState.IDLE);

		// Tick the player changing position to test if state changes to walk
		player.handleKeyDown(Input.Keys.D);					// Tell the player the D key is pressed
		assertTrue(player.getState() == PlayerState.WALK);

		// Tick player to test if it returns to idle after standing still
		player.handleKeyUp(Input.Keys.D);					// Tell the player the D key is released
		assertTrue(player.getState() == PlayerState.IDLE);
	}

	/**
	 * Test handling key presses TODO doesn't actually assert any checks
	 */
	@Test
	public void keysTest(){
		player.handleKeyUp(Input.Keys.W);
		player.handleKeyDown(Input.Keys.W);

		player.onTick(2);
		player.updateSprites();
		player.handleKeyUp(Input.Keys.P);
		player.handleKeyUp(Input.Keys.W);
		player.handleKeyDown(Input.Keys.S);
		player.onTick(2);
		player.updateSprites();
		player.handleKeyUp(Input.Keys.S);
		player.handleKeyDown(Input.Keys.A);
		player.onTick(2);
		player.updateSprites();
		player.handleKeyUp(Input.Keys.A);
		player.handleKeyDown(Input.Keys.D);
		player.onTick(2);
		player.updateSprites();
		player.handleKeyUp(Input.Keys.D);

		player.handleKeyDown(Input.Keys.A);
		player.handleKeyDown(Input.Keys.W);
		player.onTick(2);
		player.updateSprites();
		player.handleKeyUp(Input.Keys.W);
		player.handleKeyUp(Input.Keys.A);

		player.handleKeyDown(Input.Keys.A);
		player.handleKeyDown(Input.Keys.S);
		player.onTick(2);
		player.updateSprites();
		player.handleKeyUp(Input.Keys.A);
		player.handleKeyUp(Input.Keys.S);

		player.handleKeyDown(Input.Keys.W);
		player.handleKeyDown(Input.Keys.D);
		player.onTick(2);
		player.updateSprites();
		player.handleKeyUp(Input.Keys.D);
		player.handleKeyUp(Input.Keys.W);

		player.handleKeyDown(Input.Keys.S);
		player.handleKeyDown(Input.Keys.D);
		player.onTick(2);
		player.updateSprites();
		player.handleKeyUp(Input.Keys.D);
		player.handleKeyUp(Input.Keys.S);

		player.handleKeyDown(Input.Keys.SPACE);
		player.handleKeyDown(Input.Keys.SPACE);
		player.handleKeyDown(Input.Keys.SPACE);
		player.handleKeyDown(Input.Keys.E);
		player.handleKeyDown(Input.Keys.F);
		player.handleKeyDown(Input.Keys.T);
		player.handleKeyDown(Input.Keys.R);
		player.handleKeyDown(Input.Keys.SHIFT_LEFT);
		player.handleKeyUp(Input.Keys.SHIFT_LEFT);
	}

	/**
	 * Test returning string representations of objects
	 */
	@Test
	public void stringTest() {
		player.toString();
		player.getProgressBar();
		player.getTexture();
	}
	@Test
	public void directionTest2() {
		Direction.SE.getAngleDeg();
		Direction.getFromCoords(1.2f,1.3f);
		Direction.getFromDeg(1.2f);
		Direction.getFromRad(1.178097f);
		Direction.getFromRad(1.963495f);
		Direction.getFromRad(2.75f);
		Direction.getFromRad(3.534292f);
		Direction.getFromRad(4.32f);
		Direction.getFromRad(5.105088f);
		Direction.getFromRad(5.9f);
	}
}