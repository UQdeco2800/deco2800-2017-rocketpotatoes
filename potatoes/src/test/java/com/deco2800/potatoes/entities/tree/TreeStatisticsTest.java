package com.deco2800.potatoes.entities.tree;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.deco2800.potatoes.entities.trees.ResourceTree;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.deco2800.potatoes.entities.PropertiesBuilder;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.resources.SeedResource;
import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.entities.trees.TreeProperties;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;

public class TreeStatisticsTest {
    TreeProperties test;

    @Before
    public void setup() {
        test = new TreeProperties(new PropertiesBuilder<AbstractTree>().setBuildCost(1));
    }

    @After
    public void tearDown() {
        GameManager.get().clearManagers();
        test = null;

    }

    @Test
    public void testRemoveConstructionResources() {
        PlayerManager pm = GameManager.get().getManager(PlayerManager.class);
        pm.setPlayer(new Player(0, 0));
        pm.getPlayer().getInventory().removeInventoryResource(new SeedResource());
        pm.getPlayer().getInventory().addInventoryResource(new SeedResource());
        AbstractTree tree = new ResourceTree(0,0,new SeedResource(),2);
        assertFalse("Not enough resources but construction succeeded", test
                .removeConstructionResources(tree));
        pm.getPlayer().getInventory().updateQuantity(new SeedResource(), (int) test.getBuildCost());
        test.getBuildTime();
        //assertTrue("Construction failed with enough resources", test.removeConstructionResources());
    }

}
