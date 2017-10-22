package com.deco2800.potatoes.entities.enemies.enemyactions;

import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.enemies.Moose;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.ForestWorld;
import com.deco2800.potatoes.worlds.WorldType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

/**
 * Test class for ChannelEvent class
 */
public class ChannelEventTest extends BaseTest {
    private Moose moose = new Moose(15, 15);
    private ChannelEvent emptyChannel;
    private HealingWaveEvent healWave = new HealingWaveEvent(300, 10f, 10f);
    float channelDuration = 100;
    int eventRate = 10;
    private ChannelEvent channel = new ChannelEvent(eventRate, channelDuration, healWave);

    @Before
    public void setUp() throws Exception {
        GameManager.get().getManager(WorldManager.class).setWorld(ForestWorld.get());
        emptyChannel = new ChannelEvent();
        healWave = new HealingWaveEvent(300, 10f, 10f);
        channel = new ChannelEvent(10, 100, healWave);
    }

    @After
    public void cleanUp() {
        GameManager.get().clearManagers();
        emptyChannel = null;
        healWave = null;
        channel = null;
    }

    @Test
    public void actionTest() {
        float channellingStart = healWave.getProgress() - channelDuration;

        //Set enemy's channel timer such that channel action should not happen yet
        moose.setChannellingTimer(healWave.getProgress() - (int)(channellingStart-1));
        channel.action(moose);
        Assert.assertTrue("channelling has prematurely stopped enemy movement", moose.getMoving());

        //Set enemy's channel timer such that channel action should be occurring
        moose.setChannellingTimer(healWave.getProgress() - 1);
        channel.action(moose);
        Assert.assertFalse("channelling has not stopped enemy movement when it should have", moose.getMoving());

        int initialChannelTime = moose.getChannelTimer();
        moose.setChannellingTimer(initialChannelTime);
        channel.action(moose);
        Assert.assertEquals("channelling event hasn't properly incremented enemy's channeling timer",
                (initialChannelTime + eventRate), moose.getChannelTimer());
    }

    @Test
    public void toStringTest() {
        Assert.assertEquals("String mismatch", channel.toString(),
                String.format("Channel with %f duration", channelDuration));
    }
}
