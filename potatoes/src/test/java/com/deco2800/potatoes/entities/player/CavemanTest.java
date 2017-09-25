package com.deco2800.potatoes.entities.player;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.deco2800.potatoes.entities.HasDirection.Direction;
import com.deco2800.potatoes.entities.animation.TimeAnimation;
import com.deco2800.potatoes.entities.player.Player.PlayerState;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager.PlayerType;

/**
 * JUnit tests for validating the Player class
 */
public class CavemanTest {
	
	Caveman caveman;
	
	@Before
	public void setup() {
		caveman = new Caveman(0, 0);
	}
	
	@After
    public void cleanUp() {
    	GameManager.get().clearManagers();
    }
	
	/**
	 * Test creating player animations
	 */
	@Test
	public void animationTest() {
		
		Map<Direction, TimeAnimation> animationMap = Player.makePlayerAnimation(PlayerType.caveman.name(), 
				PlayerState.idle, 1, 1, null);
		
		// Test setting idle animation to north
		caveman.setAnimation(animationMap.get(Direction.N));
		
		// Test setting idle animation to south
		caveman.setAnimation(animationMap.get(Direction.S));
				
		// Test setting idle animation to south west
		caveman.setAnimation(animationMap.get(Direction.SW));
	}

}
