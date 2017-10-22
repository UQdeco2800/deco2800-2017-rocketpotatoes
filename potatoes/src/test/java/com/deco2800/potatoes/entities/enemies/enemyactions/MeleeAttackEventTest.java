package com.deco2800.potatoes.entities.enemies.enemyactions;

import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.enemies.TankEnemy;
import com.deco2800.potatoes.entities.enemies.enemyactions.MeleeAttackEvent;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.ForestWorld;
import com.deco2800.potatoes.worlds.WorldType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class to test the MeleeAttackEvent type
 *
 * @author craig & ryanjphelan
 */
public class MeleeAttackEventTest extends BaseTest {

    private MeleeAttackEvent eventEmpty;
    private MeleeAttackEvent testEvent = new MeleeAttackEvent(20, Player.class);
    private TankEnemy testMeleeEnemy = new TankEnemy(15, 15);

    @Before
    public void setUp() throws Exception {
        eventEmpty = new MeleeAttackEvent();
        testEvent = new MeleeAttackEvent(20, Player.class);
        GameManager.get().getManager(WorldManager.class).setWorld(ForestWorld.get());
    }

    @After
    public void cleanUp() {
    	GameManager.get().clearManagers();
    	eventEmpty = null;
    	testEvent = null;
    	testMeleeEnemy = null;
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
        GameManager.get().getWorld().addEntity(new Player(17, 17));
        testEvent.action(testMeleeEnemy);
    }

    /*
     * Test the melee event when an entity is in range and when the attacker dies.
     */
    @Test
    public void actionTestInRange() {
        GameManager.get().getWorld().addEntity(testMeleeEnemy);
        Player playerTest = new Player(16, 16);
        playerTest.setHealth(2);
        GameManager.get().getWorld().addEntity(playerTest);
        testEvent.action(testMeleeEnemy);
        testMeleeEnemy.setHealth(0);
        testEvent.action(testMeleeEnemy);
    }
}



