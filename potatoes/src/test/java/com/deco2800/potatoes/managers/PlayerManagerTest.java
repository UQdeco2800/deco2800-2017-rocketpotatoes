package com.deco2800.potatoes.managers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.badlogic.gdx.Input;
import com.deco2800.potatoes.entities.player.Player;

import static org.junit.Assert.assertEquals;

public class PlayerManagerTest {

    PlayerManager playerManager;
    Player player;

    @Before
    public void setUp() {

        playerManager = GameManager.get().getManager(PlayerManager.class);
        player = new Player();
    }

    @After
    public void tearDown() {

        GameManager.get().clearManagers();
        player = null;
    }

    @Test
    public void setupPlayer() {


        assertEquals(null, playerManager.getPlayer());
        playerManager.handleKeyDown(Input.Keys.W);
        playerManager.handleKeyUp(Input.Keys.W);
        playerManager.setPlayer(player);
        assertEquals(player, playerManager.getPlayer());
        playerManager.handleKeyDown(Input.Keys.W);
        playerManager.handleKeyUp(Input.Keys.W);
    }
    
	@Test
	public void distanceTest() {

        player = new Player();
		player.setPosition(10, 15);
		playerManager.setPlayer(player);
		float distance = playerManager.distanceFromPlayer(5, 5);
		assertEquals(11.1803, distance, 0.0001);
	}

    @Test
    public void getTest() {

        playerManager.setPlayerType(playerManager.getPlayerType());
        playerManager.getPlayerType().toString();
        playerManager.setPlayer(0,0);
        playerManager.setPlayerType(PlayerManager.PlayerType.ARCHER);
        playerManager.setPlayer(0,0);
        playerManager.setPlayerType(PlayerManager.PlayerType.WIZARD);
        playerManager.setPlayer(0,0);
        PlayerManager.PlayerType.names();
    }

}
