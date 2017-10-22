package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.GoalPotate;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.resources.ResourceEntity;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.entities.resources.FoodResource;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.ForestWorld;
import com.deco2800.potatoes.worlds.WorldType;
import org.junit.After;
import org.junit.Assert;
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
        GameManager.get().getManager(WorldManager.class).setWorld(ForestWorld.get());
        speedyEmpty = new SpeedyEnemy();
        speedy1 = new SpeedyEnemy(0, 0);
        speedy1.getProgressBar();
        ResourceTree tree1 = new ResourceTree(1, 1);
        speedy1.addTreeToVisited(tree1);
        speedy1.mostRelevantTarget(speedy1.getSpeedyTargets());
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
    @Test
    public void getProgressBarTest() throws Exception {
        assertEquals("healthBarRed", speedy1.getProgressBar().getTexture());
    }

    @Test
    public void addTreeToVisitedTest() throws Exception {
        //not yet used
    }

    @Test
    public void mostRelevantTargetTest() throws Exception {
        ////not yet used

    }

    @Test
    public void getEnemyTypeTest() throws Exception {
        Assert.assertEquals(1,speedy1.getEnemyType().length);

    }
    /*
     * Test an emptyConstructor instance of the SpeedyEnemy.
     */
    @Test
    public void emptyConstructor() {
        for (String data:speedyEmpty.getEnemyType()) {
            assertEquals("raccoon", data);

        }

//        assertEquals("raccoon", speedyEmpty.getEnemyType());
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
     * Test the onTick method
     */
    @Test
    public void onTickTest() {
        GameManager.get().getWorld().addEntity(speedy1);
        seed = new ResourceEntity(-6, -6, new FoodResource());
        resourceTree = new ResourceTree(6, 6);
        GameManager.get().getWorld().addEntity(seed);
        GameManager.get().getWorld().addEntity(resourceTree);
        speedy1.onTick(1);
        GameManager.get().getWorld().removeEntity(resourceTree);
        goalPotatoTest = new GoalPotate(0, 0);
        playerTest = new Player(6, 9);
        GameManager.get().getWorld().addEntity(playerTest);
        GameManager.get().getWorld().addEntity(resourceTree);
        GameManager.get().getWorld().addEntity(goalPotatoTest);
        speedy1.onTick(1);
        GameManager.get().getWorld().removeEntity(goalPotatoTest);
        speedy1.onTick(1);
        GameManager.get().getWorld().removeEntity(resourceTree);
        for (int i = 0; i < 40; ++i) {
            speedy1.onTick(1);
        }
    }
}
