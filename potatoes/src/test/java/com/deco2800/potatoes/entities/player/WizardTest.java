package com.deco2800.potatoes.entities.player;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.deco2800.potatoes.entities.Direction;
import com.deco2800.potatoes.entities.animation.TimeAnimation;
import com.deco2800.potatoes.entities.player.Player.PlayerState;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager.PlayerType;

/**
 * JUnit tests for validating the Player class
 */
public class WizardTest {
	
	Wizard caveman;
	
	@Before
	public void setup() {
		caveman = new Wizard(0, 0);
	}
	
	@After
    public void cleanUp() {
    	GameManager.get().clearManagers();
    	caveman = null;
    }
	
	/**
	 * Test creating player animations
	 */
	@Test
	public void animationTest() {
		
		Map<Direction, TimeAnimation> animationMap = Player.makePlayerAnimation(PlayerType.CAVEMAN.name(), 
				PlayerState.IDLE, 1, 1, null);
		
		// Test setting idle animation to north
		caveman.setAnimation(animationMap.get(Direction.N));
		
		// Test setting idle animation to south
		caveman.setAnimation(animationMap.get(Direction.S));
				
		// Test setting idle animation to south west
		caveman.setAnimation(animationMap.get(Direction.SW));
	}
	@Test
	public void extraTest() {
		caveman.updateSprites();
		//caveman.interact();
		caveman.walk(true);
		caveman.walk(false);
//		caveman.attack();
	}
}
