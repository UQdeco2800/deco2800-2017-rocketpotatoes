package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.entities.Player;
import org.junit.Test;
import com.badlogic.gdx.Input;

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

}