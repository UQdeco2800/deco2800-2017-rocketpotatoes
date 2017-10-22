package com.deco2800.potatoes.entities.player;

import java.util.Map;

import com.deco2800.potatoes.entities.Direction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.deco2800.potatoes.entities.animation.TimeAnimation;
import com.deco2800.potatoes.entities.player.Player.PlayerState;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager.PlayerType;
import com.deco2800.potatoes.managers.PlayerManager;

/**
 * JUnit tests for validating the Player class
 */
public class CavemanTest {
	
	Caveman caveman;
	
	@Before
	public void setup() {
		caveman = new Caveman(0, 0);
		PlayerManager m = new PlayerManager();
		m.setPlayer(caveman);
		GameManager.get().addManager(m);
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
		caveman.walk(true);
		caveman.walk(false);
		caveman.setState(Player.PlayerState.WALK);
		caveman.updateSprites();
		caveman.setState(Player.PlayerState.ATTACK);
		caveman.updateSprites();
		caveman.setState(Player.PlayerState.DAMAGED);
		caveman.updateSprites();
		caveman.setState(Player.PlayerState.DEATH);
		caveman.updateSprites();
		caveman.setState(Player.PlayerState.INTERACT);
		caveman.updateSprites();
		caveman.interact();
		caveman.getProgressBar().getLayoutTexture();
	}
}
