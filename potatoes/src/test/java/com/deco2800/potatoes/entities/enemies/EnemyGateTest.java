package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.ForestWorld;
import com.deco2800.potatoes.worlds.WorldType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/*
 * Test class to test the Moose enemy type
 *
 * @author ryanjphelan
 */
public class EnemyGateTest extends BaseTest {

    private EnemyGate enemyGateEmpty;
    private EnemyGate enemyGate1;
    private EnemyGate enemyGate2;
    private EnemyGate enemyGate3;
    private EnemyGate enemyGate4;

    @Before
    public void setup() throws Exception {
        GameManager.get().getManager(WorldManager.class).setWorld(ForestWorld.get());
        enemyGateEmpty = new EnemyGate();
        System.out.println(GameManager.get().getWorld().getLength()/2);
        enemyGate1 = new EnemyGate(4,4, "enemyCave_SE");
        enemyGate2 = new EnemyGate(4,40, "enemyCave_SE");
        enemyGate3 = new EnemyGate(40,4, "enemyCave_SE");
        enemyGate4 = new EnemyGate(40,40, "enemyCave_SE");
    }

    @After
    public void cleanUp() {
        GameManager.get().clearManagers();
        enemyGate1 = null;
        enemyGateEmpty = null;
    }

    /*
     * Test the moose when it is initialised using the empty constructor
     */
    @Test
    public void emptyConstructor() {
        assertEquals(null, enemyGateEmpty.getProgressBar());
    }

    /*
     * Test the toString method
     */
    @Test
    public void toStringTest() {
        Assert.assertEquals("String mismatch", "The Enemy Gate", enemyGate1.toString());
    }

}
