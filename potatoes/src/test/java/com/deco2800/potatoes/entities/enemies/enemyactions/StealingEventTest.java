package com.deco2800.potatoes.entities.enemies.enemyactions;

import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.Direction;
import com.deco2800.potatoes.entities.enemies.SpeedyEnemy;
import com.deco2800.potatoes.entities.enemies.Squirrel;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.ForestWorld;
import com.deco2800.potatoes.worlds.WorldType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import static com.deco2800.potatoes.entities.Direction.getFromRad;
import static com.deco2800.potatoes.entities.Direction.getRadFromCoords;
import static org.junit.Assert.assertEquals;

/***
 * Test for StealingEvent
 */
public class StealingEventTest extends BaseTest {
    private SpeedyEnemy raccoon = new SpeedyEnemy(20, 20);
    private Squirrel squirrel = new Squirrel(20,20);
    private ResourceTree insideTree;
    private ResourceTree outsideTree;
    private StealingEvent steal;
    private int eventRate = 10;
    private int initialTreeResources = 5;
    private float range = .8f;

    @Before
    public void setUp() throws Exception {
        GameManager.get().getManager(WorldManager.class).setWorld(ForestWorld.get());
        GameManager.get().getWorld().addEntity(raccoon);
        insideTree = new ResourceTree(20, 20);
        outsideTree = new ResourceTree(25,25);

        steal = new StealingEvent(eventRate);
    }

    @After
    public void cleanUp() {
        GameManager.get().clearManagers();
        raccoon = null;
        steal = null;
        insideTree = null;
        outsideTree = null;
    }

    @Test public void actionTest() {
        raccoon.setMoving(true);
        raccoon.setDirectionToCoords(1,1);

        //Check if stealing occurs when only raccoon in world
        steal.action(raccoon);
        Assert.assertEquals("steal action has occurred without any possible targets in world",
                true, raccoon.getMoving());

        //Add entities to world that should not trigger steal event
        GameManager.get().getWorld().addEntity(outsideTree);
        outsideTree.gather(initialTreeResources);
        GameManager.get().getWorld().addEntity(squirrel);
        steal.action(raccoon);
        Assert.assertEquals("steal action has occurred with insuitable targets",
                true, raccoon.getMoving());
        Assert.assertEquals("tree outside of range has been stolen from", initialTreeResources,
                outsideTree.getGatherCount());

        //Test for positive case; that a viable theft target is within range
        GameManager.get().getWorld().addEntity(insideTree);
        insideTree.gather(initialTreeResources);
        steal.action(raccoon);
        Assert.assertEquals("tree within range of steal event owner as not stolen from",
                initialTreeResources-1, insideTree.getGatherCount());
        Assert.assertEquals("stealing enemy didn't stop to steal", false, raccoon.getMoving());

        Direction correctDirection = getFromRad( getRadFromCoords( insideTree.getPosX()-raccoon.getPosX(), insideTree.getPosY()-raccoon.getPosY()));
        raccoon.getFacing();
        Assert.assertEquals("Stealing enemy didn't turn to face victim", correctDirection, raccoon.getFacing());

        //Test for whether stealing enemy acts appropriately when resource tree has no resources
        insideTree.gather(-insideTree.getGatherCount());
        raccoon.setMoving(false);
        steal.action(raccoon);
        Assert.assertEquals("stealing enemy not moving after interacting with a tree at 0 resources",
                true, raccoon.getMoving());
    }

    @Test
    public void toStringTest() {
        Assert.assertEquals("String mismatch", steal.toString(),
                String.format("Steal occurring every %d ticks", steal.getResetAmount()));
    }

}

