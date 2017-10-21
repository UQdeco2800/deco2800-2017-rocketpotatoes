package com.deco2800.potatoes.entities.enemies.enemyactions;

import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.enemies.Moose;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.WorldType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ChannelEvent class
 */
public class ChannelEventTest extends BaseTest {
    private Moose testMooseEnemy = new Moose(15, 15);

    private ChannelEvent emptyChannel;
    private HealingWaveEvent testHealWave = new HealingWaveEvent(300, 10f, 10f);
    private ChannelEvent testChannelEvent = new ChannelEvent(10, 100, testHealWave);

    @Before
    public void setup() throws Exception {
        emptyChannel = new ChannelEvent();
        testHealWave = new HealingWaveEvent(300, 10f, 10f);
        testChannelEvent = new ChannelEvent(10, 100, testHealWave);
        GameManager.get().getManager(WorldManager.class).setWorld(WorldType.FOREST_WORLD);
    }

    @After
    public void cleanUp() {
        GameManager.get().clearManagers();
        emptyChannel = null;
        testHealWave = null;
        testChannelEvent = null;
    }
}
