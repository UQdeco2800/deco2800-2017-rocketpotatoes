package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.entities.Player;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayerManagerTest {
    @Test
    public void setupPlayer() {
        PlayerManager m = new PlayerManager();
        Player p = new Player();

        assertEquals(null, m.getPlayer());
        m.setPlayer(p);
        assertEquals(p, m.getPlayer());
    }

}