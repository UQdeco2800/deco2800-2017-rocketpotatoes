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
import com.deco2800.potatoes.managers.PlayerManager;

/**
 * JUnit tests for validating the Player class
 */
public class WizardTest {
	
	Wizard wizard;
	
	@Before
	public void setup() {
		wizard = new Wizard(0, 0);		
		PlayerManager m = new PlayerManager();
		m.setPlayer(wizard);
		GameManager.get().addManager(m);
	}
	
	@After
    public void cleanUp() {
    	GameManager.get().clearManagers();
    	wizard = null;
    }
	
	/**
	 * Test creating player animations
	 */
	@Test
	public void animationTest() {
		
		Map<Direction, TimeAnimation> animationMap = Player.makePlayerAnimation(PlayerType.CAVEMAN.name(), 
				PlayerState.IDLE, 1, 1, null);
		
		// Test setting idle animation to north
		wizard.setAnimation(animationMap.get(Direction.N));
		
		// Test setting idle animation to south
		wizard.setAnimation(animationMap.get(Direction.S));
				
		// Test setting idle animation to south west
		wizard.setAnimation(animationMap.get(Direction.SW));
	}
	@Test
	public void extraTest() {
		wizard.updateSprites();
		wizard.walk(true);
		wizard.walk(false);
		wizard.setState(Player.PlayerState.WALK);
		wizard.updateSprites();
		wizard.setState(Player.PlayerState.ATTACK);
		wizard.updateSprites();
		wizard.setState(Player.PlayerState.DAMAGED);
		wizard.updateSprites();
		wizard.setState(Player.PlayerState.DEATH);
		wizard.updateSprites();
		wizard.setState(Player.PlayerState.INTERACT);
		wizard.updateSprites();
		wizard.interact();
		wizard.getProgressBar().getLayoutTexture();
	}
}
