package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WorldManager;
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

    @Before
    public void setup() throws Exception {
        GameManager.get().getManager(WorldManager.class).setWorld(ForestWorld.get());
        enemyGateEmpty = new EnemyGate();
        enemyGate1 = new EnemyGate(5,5, "enemyCave_SE");
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
