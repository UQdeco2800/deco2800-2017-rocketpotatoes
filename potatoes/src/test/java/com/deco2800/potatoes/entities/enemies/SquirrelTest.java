package com.deco2800.potatoes.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.GoalPotate;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.resources.FoodResource;
import com.deco2800.potatoes.entities.resources.ResourceEntity;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.PathManager;
import com.deco2800.potatoes.worlds.WorldType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class to test the Squirrel enemy type
 *
 * @author ryanjphelan
 */
public class SquirrelTest extends BaseTest {

    private Squirrel squirrelEmpty;
    private Squirrel squirrel1;
    private Squirrel squirrel2;
    private ResourceEntity seed;
    private ResourceTree resourceTree;
    private Player playerTest;
    private GoalPotate goalPotatoTest;

    @Before
    public void setup() throws Exception {
        squirrelEmpty = new Squirrel();
        squirrel1 = new Squirrel(0, 0, 0);
        squirrel2 = new Squirrel(8, 8, 0);
        GameManager.get().getManager(WorldManager.class).setWorld(WorldType.FOREST_WORLD);
    }

    @After
    public void cleanUp() {
        GameManager.get().clearManagers();
    }

    /*
     * Test the squirrel when it is initialised using the empty constructor
     */
    @Test
    public void emptyConstructor() {
        assertEquals(null, squirrelEmpty.getDirection());
        assertEquals("squirrel", squirrelEmpty.getEnemyType());
        assertEquals(Color.RED, squirrel1.getProgressBar().getColours().get(0));
        assertEquals(Color.valueOf("fff134"), squirrel1.getProgressBar().getColours().get(1));
        assertEquals(Color.GREEN, squirrel1.getProgressBar().getColours().get(2));
    }

    /*
     * Test the toString method
     */
    @Test
    public void toStringTest() {
        Assert.assertEquals("String mismatch", "squirrel at (0, 0)", squirrel1.toString());
    }

    /*
 * Test the onTick method
 */
    @Test
    public void onTickTest() {
        /*GameManager.get().getWorld().addEntity(squirrel1);
        GameManager.get().getWorld().addEntity(squirrel2);
        playerTest = new Player(3, 3, 0);
        GameManager.get().getWorld().addEntity(playerTest);
        seed = new ResourceEntity(-1, -1, 0, new FoodResource());
        resourceTree = new ResourceTree(1, 1, 0);
        GameManager.get().getWorld().addEntity(seed);
        GameManager.get().getWorld().addEntity(resourceTree);
        squirrel1.onTick(1);*/
    }
}