package com.deco2800.potatoes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.SeedResource;
import com.deco2800.potatoes.entities.StatisticsBuilder;
import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.entities.trees.TreeStatistics;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;

public class TreeStatisticsTest {
	TreeStatistics test;

	@Before
	public void setup() {
		test = new TreeStatistics(new StatisticsBuilder<AbstractTree>().setBuildCost(1));
	}

	@Test
	public void testRemoveConstructionResources() {
		PlayerManager pm = GameManager.get().getManager(PlayerManager.class);
		pm.setPlayer(new Player(0, 0, 0));
		pm.getPlayer().getInventory().removeInventoryResource(new SeedResource());
		pm.getPlayer().getInventory().addInventoryResource(new SeedResource());
		assertFalse("Not enough resources but construction succeeded", test.removeConstructionResources());
		pm.getPlayer().getInventory().updateQuantity(new SeedResource(), (int) test.getBuildCost());
		assertTrue("Construction failed with enough resources", test.removeConstructionResources());
	}

}
