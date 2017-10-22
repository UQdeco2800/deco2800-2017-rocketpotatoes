package com.deco2800.potatoes.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.GoalPotate;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.portals.BasePortal;
import com.deco2800.potatoes.entities.trees.ProjectileTree;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.ForestWorld;
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
    private Player playerTest;

    @Before
    public void setup() throws Exception {
        GameManager gameManager = GameManager.get();
        squirrelEmpty = new Squirrel();
        squirrel1 = new Squirrel(0, 0);
        squirrel2 = new Squirrel(8, 8);
        gameManager.getManager(WorldManager.class).setWorld(ForestWorld.get());
        gameManager.getWorld().addEntity(new BasePortal(14, 17, 100));
    }

    @After
    public void cleanUp() {
        GameManager.get().clearManagers();
        squirrel1 = null;
        squirrel2 = null;
        squirrelEmpty = null;
        playerTest = null;
    }

    /*
     * Test the squirrel when it is initialised using the empty constructor
     */
    @Test
    public void emptyConstructor() {
        for (String data:squirrelEmpty.getEnemyType()) {
            assertEquals("squirrel", data);

        }
//        assertEquals("squirrel", squirrelEmpty.getEnemyType());

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
        GameManager.get().getWorld().addEntity(new EnemyGate(5,5, "enemyCave_SE"));
        GameManager.get().getWorld().addEntity(new GoalPotate(15, 10));
        GameManager.get().getWorld().addEntity(new ProjectileTree(10, 10));
        GameManager.get().getWorld().addEntity(squirrel1);
        GameManager.get().getWorld().addEntity(squirrel2);
        playerTest = new Player(3, 3);
        GameManager.get().getManager(PlayerManager.class).setPlayer(playerTest);
        GameManager.get().getWorld().addEntity(playerTest);
        squirrel1.onTick(1);
    }

    @Test
    public void getEnemyTypeTest() throws Exception {
    }


    @Test
    public void getProgressBarTest() throws Exception {
    }

    @Test
    public void getBasicStatsTest() throws Exception {
    }

}
