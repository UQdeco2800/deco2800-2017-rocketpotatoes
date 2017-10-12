package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.GoalPotate;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.resources.FoodResource;
import com.deco2800.potatoes.entities.resources.ResourceEntity;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.WorldType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class to test the SpeedyEnemy enemy type
 *
 * @author ryanjphelan & tl
 */
public class SpeedyEnemyTest extends BaseTest {

    private SpeedyEnemy speedyEmpty;
    private SpeedyEnemy speedy1;
    private ResourceEntity seed;
    private ResourceTree resourceTree;
    private Player playerTest;
    private GoalPotate goalPotatoTest;

    @Before
    public void setUp() throws Exception {
        speedyEmpty = new SpeedyEnemy();
        speedy1 = new SpeedyEnemy(0, 0);
        GameManager.get().getManager(WorldManager.class).setWorld(WorldType.FOREST_WORLD);
    }

    @After
    public void cleanUp() {
        GameManager.get().clearManagers();
        speedyEmpty = null;
        speedy1 = null;
        seed = null;
        resourceTree = null;
        playerTest = null;
        goalPotatoTest = null;
    }

    /*
     * Test an emptyConstructor instance of the SpeedyEnemy.
     */
    @Test
    public void emptyConstructor() {
        assertEquals(true, speedyEmpty.getDirection() == null);
        assertEquals("raccoon", speedyEmpty.getEnemyType());
        speedyEmpty.getBasicStats().getGoal();
        speedyEmpty.getBasicStats().getSpeed();
    }

    /*
     * Test the toString method for SpeedyEnemy
     */
    @Test
    public void toStringTest() {
        assertEquals("raccoon at (0, 0)", speedy1.toString());
    }

    /*
     * Test the onTick method TODO does not actually use any assert tests
     */
    /*@Test
    public void onTickTest() {
        GameManager.get().getWorld().addEntity(speedy1);
        seed = new ResourceEntity(-1, -1, new FoodResource());
        resourceTree = new ResourceTree(1, 1);
        GameManager.get().getWorld().addEntity(seed);
        GameManager.get().getWorld().addEntity(resourceTree);
        speedy1.onTick(1);
        GameManager.get().getWorld().removeEntity(resourceTree);
        goalPotatoTest = new GoalPotate(0, 0);
        playerTest = new Player(3, 3);
        GameManager.get().getWorld().addEntity(playerTest);
        //speedy1.onTick(1);
        GameManager.get().getWorld().addEntity(resourceTree);
        GameManager.get().getWorld().addEntity(goalPotatoTest);
        speedy1.onTick(1);
        GameManager.get().getWorld().removeEntity(goalPotatoTest);
        speedy1.onTick(1);
        GameManager.get().getWorld().removeEntity(resourceTree);
        speedy1.onTick(1);

    }*/
}
