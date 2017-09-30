package com.deco2800.potatoes.entities.player;

import com.deco2800.potatoes.entities.HasDirection.Direction;
import com.deco2800.potatoes.entities.animation.TimeAnimation;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.player.Player.PlayerState;
import com.deco2800.potatoes.managers.CameraManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.managers.PlayerManager.PlayerType;
import com.deco2800.potatoes.worlds.WorldType;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * JUnit tests for validating the Player class
 */
public class PlayerTest {
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
        GameManager.get().getManager(WorldManager.class).setWorld(WorldType.FOREST_WORLD);
        
        cameraManager.setCamera(camera);
        
        m.setPlayer(player);
    }
	
	@After
    public void cleanUp() {
    	GameManager.get().clearManagers();
    }
	
	/**
	 * Test that the player returns its current direction;
	 */
	@Test
	public void directionTest() {
		player.getDirection(); // Test getting the direction
		
		player.setPosition(0, 0); 	// Set the player to the origin
		player.onTick(tick++); 			// Tick the player
        player.setPosition(10, 10); 	// Move the player in East Direction
        player.onTick(tick++);
        assertTrue(player.getDirection().equals(Direction.E));
        
        player.setPosition(15, 5); 	// Move the player in North Direction
        player.onTick(tick++); 
        assertTrue(player.getDirection().equals(Direction.N));
	}
	
	/**
	 * Test the player changing states
	 */
	@Test
	public void stateTest() {
		// Test to see if in idle by default
		assertTrue(player.getState() == PlayerState.IDLE);
		
		// Tick the player changing position to test if state changes to walk
		player.setPosition(0, 0);
		player.onTick(tick++); 		
        player.setPosition(10, 10);
        player.onTick(tick++);
        assertTrue(player.getState() == PlayerState.WALK);
        
        // Tick player to test if it returns to idle after standing still
        player.onTick(tick++);
        assertTrue(player.getState() == PlayerState.IDLE);
	}
	
	/**
	 * Test handling key presses
	 */
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
	
	/**
	 * Test updating the player
	 */
    @Test
    public void updateTest() {
        player.updateSprites();
        player.onTick(2);
    }
}
