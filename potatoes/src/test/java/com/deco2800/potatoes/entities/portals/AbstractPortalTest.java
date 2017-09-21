package com.deco2800.potatoes.entities.portals;

import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.AbstractEntity;

import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.enemies.Squirrel;
import com.deco2800.potatoes.entities.portals.AbstractPortal;
import com.deco2800.potatoes.entities.portals.BasePortal;
import com.deco2800.potatoes.exceptions.InvalidResourceException;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.util.WorldUtil;
import com.deco2800.potatoes.worlds.World;
import com.deco2800.potatoes.worlds.WorldType;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import com.badlogic.gdx.Input;

/**
 * Tests for abstract portals.
 * 
 * @author Jordan Holder
 *
 */
public class AbstractPortalTest extends BaseTest{
	AbstractPortal testPortal;
	
	@Before
	public void setup() {
		SoundManager soundManager = new SoundManager();
		PlayerManager playerManager = new PlayerManager();
		
		GameManager.get().setWorld(new TestWorld());
		GameManager.get().addManager(soundManager);
		GameManager.get().addManager(playerManager);
		
		testPortal = new AbstractPortal(0, 0, 0, null);
	}
	
	@Test
	public void collidedFalseTest() {
		boolean collided;
		// player not colliding with portal
		Player addedPlayer = new Player(10, 10, 0);
		GameManager.get().getWorld().addEntity(addedPlayer);
		collided = testPortal.preTick(0);
		assertEquals(false, collided);
	}
	
	@Test
	public void collidedTrueTest() {
		boolean collided;
		//player colliding with portal
		Player addedPlayer = new Player(0, 0, 0);
		GameManager.get().getWorld().addEntity(addedPlayer);
		
		collided = testPortal.preTick(0);
		
		assertEquals(true, collided);
	}
	
	@Test
	public void collidedNotPlayerTest() {
		boolean collided;
		//entity that isn't a player colliding with the portal
		Squirrel entity = new Squirrel(0, 0, 0);
		GameManager.get().getWorld().addEntity(entity);
		
		collided = testPortal.preTick(0);
		
		assertEquals(false, collided);
	}
	
	@Test
	public void getPlayerNullTest() {
		Player player;
		//entity that isn't a player
		Squirrel entity = new Squirrel(0, 0, 0);
		GameManager.get().getWorld().addEntity(entity);
		
		testPortal.preTick(0);
		
		player = (Player) testPortal.getPlayer();
		
		assertEquals(null, player);
	}
	
	@Test
	public void getPlayerTest() {
		Player testPlayer;
		//player added to the world
		Player addedPlayer = new Player(0, 0, 0);
		GameManager.get().getWorld().addEntity(addedPlayer);
		
		testPortal.preTick(0);
		
		testPlayer = (Player) testPortal.getPlayer();
		
		assertEquals(testPlayer, addedPlayer);

	}
	
	@Test
	public void changeWorldTest() {
		Player testPlayer = new Player();
		
		GameManager.get().getManager(PlayerManager.class).setPlayer(testPlayer);
		
		testPortal.changeWorld();
		
		testPlayer = GameManager.get().getManager(PlayerManager.class).getPlayer();
		assert(18 == testPlayer.getPosX());
		assert(16 == testPlayer.getPosY());
	}
	
	@Test
	public void onTickExceptionTest() {
		testPortal.onTick(0);
		PlayerManager playerManager = GameManager.get().getManager(PlayerManager.class);
		Player testPlayer;
		//player added to the world
		Player addedPlayer = new Player(1, 1, 1);
		
		GameManager.get().getManager(PlayerManager.class).setPlayer(addedPlayer);
		GameManager.get().getWorld().addEntity(addedPlayer);

		testPortal.onTick(0);
		
		testPlayer = GameManager.get().getManager(PlayerManager.class).getPlayer();
		
		assertEquals(addedPlayer, testPlayer);
	}
	
	private class TestWorld extends World {
		
	}

}