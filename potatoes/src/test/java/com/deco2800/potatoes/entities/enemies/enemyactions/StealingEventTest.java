package com.deco2800.potatoes.entities.enemies.enemyactions;

import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.enemies.SpeedyEnemy;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.WorldType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;

/***
 * Test for StealingEvent
 */
public class StealingEventTest extends BaseTest {
    private SpeedyEnemy raccoon = new SpeedyEnemy(20, 20);
    private ResourceTree tree;
    private StealingEvent steal;
    private int eventRate = 10;

    @Before
    public void setUp() throws Exception {
        GameManager.get().getManager(WorldManager.class).setWorld(WorldType.FOREST_WORLD);
        GameManager.get().getWorld().addEntity(raccoon);
        steal = new StealingEvent(eventRate);
    }

    @After
    public void cleanUp() {
        GameManager.get().clearManagers();
        raccoon = null;
        steal = null;
        tree = null;
    }
}

