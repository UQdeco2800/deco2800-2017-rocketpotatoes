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
public class ArcherTest {
	
	Archer archer;
	
	@Before
	public void setup() {
		archer = new Archer(0, 0);
		PlayerManager m = new PlayerManager();
		m.setPlayer(archer);
		GameManager.get().addManager(m);
	}
	
	@After
    public void cleanUp() {
    	GameManager.get().clearManagers();
    	archer = null;
    }
	
	/**
	 * Test creating player animations
	 */
	@Test
	public void animationTest() {
		
		Map<Direction, TimeAnimation> animationMap = Player.makePlayerAnimation(PlayerType.CAVEMAN.name(), 
				PlayerState.IDLE, 1, 1, null);
		
		// Test setting idle animation to north
		archer.setAnimation(animationMap.get(Direction.N));
		
		// Test setting idle animation to south
		archer.setAnimation(animationMap.get(Direction.S));
				
		// Test setting idle animation to south west
		archer.setAnimation(animationMap.get(Direction.SW));
	}
	@Test
	public void extraTest() {
		archer.updateSprites();
		archer.walk(true);
		archer.walk(false);
		archer.setState(Player.PlayerState.WALK);
		archer.updateSprites();
		archer.setState(Player.PlayerState.ATTACK);
		archer.updateSprites();
		archer.setState(Player.PlayerState.DAMAGED);
		archer.updateSprites();
		archer.setState(Player.PlayerState.DEATH);
		archer.updateSprites();
		archer.setState(Player.PlayerState.INTERACT);
		archer.updateSprites();
		archer.interact();
		archer.getProgressBar().getLayoutTexture();
	}
}
