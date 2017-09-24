package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.entities.enemies.MeleeAttackEvent;
import com.deco2800.potatoes.entities.enemies.TankEnemy;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.managers.GameManager;

import com.deco2800.potatoes.worlds.World;
import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.After;

public class MeleeAttackEventTest {
    MeleeAttackEvent testEvent = new MeleeAttackEvent(20, Player.class);
    TankEnemy testMeleeEnemy = new TankEnemy(15, 15);

    @After
    public void cleanUp() {
    	GameManager.get().clearManagers();
    }
    
    @Test
    public void stringTest() {
        assertEquals("string mismatch", "Melee attack with 20 attackspeed", testEvent.toString());
    }

    @Test
    public void emptyTest() {
        MeleeAttackEvent emptyEvent = new MeleeAttackEvent(100, Player.class);
    }

    @Test
    public void copyTest() {
        testEvent.copy();
    }

    @Test
    public void actionTest() {
        GameManager.get().setWorld(new MeleeAttackEventTest.TestWorld());
        GameManager.get().setWorld(new TestWorld());
        GameManager.get().getWorld().addEntity(testMeleeEnemy);
        testEvent.action(testMeleeEnemy);
        GameManager.get().getWorld().addEntity(new Player(17, 17));
        testEvent.action(testMeleeEnemy);
    }

    private class TestWorld extends World {

    }
}



