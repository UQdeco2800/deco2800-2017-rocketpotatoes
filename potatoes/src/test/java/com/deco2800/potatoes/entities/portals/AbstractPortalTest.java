package com.deco2800.potatoes.entities.portals;

import com.deco2800.potatoes.entities.enemies.Squirrel;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.managers.GameManager;
//import com.deco2800.potatoes.worlds.InitialWorld;
import com.deco2800.potatoes.worlds.World;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;

import org.junit.After;

/**
 * Tests for abstract portals.
 * 
 * @author Jordan Holder
 *
 */
public class AbstractPortalTest{
	AbstractPortal testPortal;
	
	@Before
	public void setup() {
		testPortal = new AbstractPortal(0, 0, null);
		GameManager.get().setWorld(new TestWorld());
	}
	
	@After
    public void cleanUp() {
    	GameManager.get().clearManagers();
    	testPortal = null;
    }
	
	@Test
	public void collidedFalseTest() {
		boolean collided;
		// player not colliding with portal
		Player addedPlayer = new Player(10, 10);
		GameManager.get().getWorld().addEntity(addedPlayer);
		
		collided = testPortal.preTick(0);
		assertEquals(false, collided);
		
	}
	
	@Test
	public void collidedTrueTest() {
		boolean collided;
		//player colliding with portal
		Player addedPlayer = new Player(0, 0);
		GameManager.get().getWorld().addEntity(addedPlayer);
		
		collided = testPortal.preTick(0);
		
		assertEquals(true, collided);
	}
	
	@Test
	public void collidedNotPlayerTest() {
		boolean collided;
		//entity that isn't a player colliding with the portal
		Squirrel entity = new Squirrel(0, 0);
		GameManager.get().getWorld().addEntity(entity);
		
		collided = testPortal.preTick(0);
		
		assertEquals(false, collided);
	}
	
	@Test
	public void getPlayerNullTest() {
		Player player;
		//entity that isn't a player
		Squirrel entity = new Squirrel(0, 0);
		GameManager.get().getWorld().addEntity(entity);
		
		testPortal.preTick(0);
		
		player = (Player) testPortal.getPlayer();
		
		assertEquals(null, player);
	}
	
	@Test
	public void getPlayerTest() {
		Player testPlayer;
		//player added to the world
		Player addedPlayer = new Player(0, 0);
		GameManager.get().getWorld().addEntity(addedPlayer);
		
		testPortal.preTick(0);
		
		testPlayer = (Player) testPortal.getPlayer();
		
		assertEquals(testPlayer, addedPlayer);

	}
	
	private class TestWorld extends World {
		
	}

	@Test
	public void getWorldCollidedTest() {
		testPortal.onTick(0);
/*		PlayerManager playerManager = GameManager.get().getManager(PlayerManager.class);
		Player testPlayer;
		//player added to the world
		Player addedPlayer = new Player(1, 1, 1);
		GameManager.get().getWorld().addEntity(addedPlayer);

		testPortal.onTick(0);

		System.out.println(GameManager.get().getWorld().toString());

		assertEquals(ForestWorld.get(), GameManager.get().getManager(WorldManager.class).getWorld(ForestWorld.get()));
*/
	}

}
