package com.deco2800.potatoes.entities.enemies;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.ForestWorld;
import com.deco2800.potatoes.worlds.WorldType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class to test the Melee attack event class
 *
 * @author craig & ryanjphelan
 */
public class MeleeAttackTest extends BaseTest {

    private TankEnemy testMeleeEnemy = new TankEnemy(15, 15);
    private Player playerTest = new Player(16, 16);
    private MeleeAttack eventEmpty = new MeleeAttack();
    private MeleeAttack testEvent = new MeleeAttack(testMeleeEnemy.getClass(),
            new Vector3(testMeleeEnemy.getPosX() + 0.5f, testMeleeEnemy.getPosY() + 0.5f, testMeleeEnemy.getPosZ()),
            new Vector3(playerTest.getPosX(), playerTest.getPosY(), playerTest.getPosZ()), 1, 4);

    @Before
    public void setup() throws Exception {
        GameManager.get().getManager(WorldManager.class).setWorld(ForestWorld.get());
    }

    @After
    public void cleanUp() {
        GameManager.get().clearManagers();
        testMeleeEnemy = null;
        playerTest = null;
        eventEmpty = null;
        testEvent = null;

    }

    /*
     * Test the melee event construction.
     */
    @Test
    public void testAttackEvent() {
        testEvent.onTick(1);
    }
}
