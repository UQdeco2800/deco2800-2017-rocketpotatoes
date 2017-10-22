package com.deco2800.potatoes.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.GoalPotate;
import com.deco2800.potatoes.entities.player.Player;
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
 * Test class to test the Moose enemy type
 *
 * @author ryanjphelan
 */
public class MooseTest extends BaseTest {

    private Moose mooseEmpty;
    private Moose moose1;
    private Player playerTest;

    @Before
    public void setup() throws Exception {
        mooseEmpty = new Moose();
        moose1 = new Moose(0, 0);
        GameManager.get().getManager(WorldManager.class).setWorld(ForestWorld.get());
    }

    @After
    public void cleanUp() {
        GameManager.get().clearManagers();
        moose1 = null;
        mooseEmpty = null;
        playerTest = null;
    }

    /*
     * Test the moose when it is initialised using the empty constructor
     */
    @Test
    public void emptyConstructor() {
        for (String data:mooseEmpty.getEnemyType()) {
            assertEquals("moose", data);

        }

    }

    @Test
    public void getProgressBarTest() throws Exception {
        assertEquals("healthBarRed", moose1.getProgressBar().getTexture());
    }
    
    /*
     * Test the toString method
     */
    @Test
    public void toStringTest() {
        Assert.assertEquals("String mismatch", "moose at (0, 0)", moose1.toString());
    }

    /*
     * Test the onTick method
     */
    @Test
    public void onTickTest() {
        GameManager.get().getWorld().addEntity(new EnemyGate(5,5, "enemyCave_SE"));
        GameManager.get().getWorld().addEntity(new GoalPotate(15, 10));
        GameManager.get().getWorld().addEntity(new ProjectileTree(10, 10));
        GameManager.get().getWorld().addEntity(moose1);
        playerTest = new Player(3, 3);
        GameManager.get().getManager(PlayerManager.class).setPlayer(playerTest);
        GameManager.get().getWorld().addEntity(playerTest);
        moose1.onTick(1);
        moose1.getProgressBar();
        moose1.randomTarget();
    }
}
