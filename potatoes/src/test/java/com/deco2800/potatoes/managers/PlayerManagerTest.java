package com.deco2800.potatoes.managers;

import org.junit.Test;
import com.badlogic.gdx.Input;
import com.deco2800.potatoes.entities.player.Player;

import static org.junit.Assert.assertEquals;

public class PlayerManagerTest {
    @Test
    public void setupPlayer() {
        PlayerManager m = new PlayerManager();
        Player p = new Player();

        assertEquals(null, m.getPlayer());
        m.handleKeyDown(Input.Keys.W);
        m.handleKeyUp(Input.Keys.W);
        m.setPlayer(p);
        assertEquals(p, m.getPlayer());
        m.handleKeyDown(Input.Keys.W);
        m.handleKeyUp(Input.Keys.W);
    }
    
	@Test
	public void distanceTest() {
		PlayerManager manager = new PlayerManager();
		Player player = new Player();
		player.setPosition(10, 15);
		manager.setPlayer(player);
		
		float distance = manager.distanceFromPlayer(5, 5);
		
		assertEquals(11.1803, distance, 0.0001);
	}

}
