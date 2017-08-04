package com.deco2800.potatoes;

import com.deco2800.potatoes.entities.Tree;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.worlds.AbstractWorld;
import com.deco2800.potatoes.util.WorldUtil;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WorldUtilTest {
	@Test
	public void TestDistanceFunctions() {
		GameManager.get().setWorld(new TestWorld());
		Tree t1 = new Tree(1, 1, 1);
		Tree t2 = new Tree(2, 2, 1);
		GameManager.get().getWorld().addEntity(t1);
		GameManager.get().getWorld().addEntity(t2);

		WorldUtil.closestEntityToPosition(0f, 0f, 2f);
		assertEquals(t1, WorldUtil.closestEntityToPosition(0f, 0f, 2f).get());
	}
	
	private class TestWorld extends AbstractWorld {
		
	}
}
