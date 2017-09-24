package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.WorldType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class to test the Moose enemy type
 *
 * @author craig & ryanjphelan
 */
public class MeleeAttackEventTest extends BaseTest {

    private MeleeAttackEvent eventEmpty;
    private MeleeAttackEvent testEvent = new MeleeAttackEvent(20, Player.class);
    private TankEnemy testMeleeEnemy = new TankEnemy(15, 15, 0);

    @Before
    public void setup() throws Exception {
        eventEmpty = new MeleeAttackEvent();
        testEvent = new MeleeAttackEvent(20, Player.class);
        GameManager.get().getManager(WorldManager.class).setWorld(WorldType.FOREST_WORLD);
    }

    @After
    public void cleanUp() {
    	GameManager.get().clearManagers();
    }

    /*
     * Test the toString method.
     */
    @Test
    public void toStringTest() {
        assertEquals("String Mismatch", "Melee attack with 20 attackspeed", testEvent.toString());
    }

    /*
     * Test the copy method.
     */
    @Test
    public void copyTest() {
        TimeEvent<EnemyEntity> testCopy = testEvent.copy();
    }

    /*
     * Test the melee event in action (out of range)
     */
    @Test
    public void actionTestOutOfRange() {
        GameManager.get().getWorld().addEntity(testMeleeEnemy);
        testEvent.action(testMeleeEnemy);
        GameManager.get().getWorld().addEntity(new Player(17, 17, 0));
        testEvent.action(testMeleeEnemy);
    }

    /*
     * Test the melee event when an entity is in range and when the attacker dies.
     */
    @Test
    public void actionTestInRange() {
        GameManager.get().getWorld().addEntity(testMeleeEnemy);
        Player playerTest = new Player(16, 16, 0);
        playerTest.setHealth(2);
        GameManager.get().getWorld().addEntity(playerTest);
        testEvent.action(testMeleeEnemy);
        testMeleeEnemy.setHealth(0);
        testEvent.action(testMeleeEnemy);
    }
}



