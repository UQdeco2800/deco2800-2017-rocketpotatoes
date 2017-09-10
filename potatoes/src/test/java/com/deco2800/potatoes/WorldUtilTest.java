package com.deco2800.potatoes;

import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.worlds.World;
import com.deco2800.potatoes.util.WorldUtil;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WorldUtilTest {
	@Test
	public void TestDistanceFunctions() {
		GameManager.get().setWorld(new TestWorld());
		Player t1 = new Player(1, 1, 1);
		Player t2 = new Player(2, 2, 1);
		GameManager.get().getWorld().addEntity(t1);
		GameManager.get().getWorld().addEntity(t2);

		WorldUtil.closestEntityToPosition(0f, 0f, 2f);
		assertEquals(t1, WorldUtil.closestEntityToPosition(0f, 0f, 2f).get());
	}
	
	private class TestWorld extends World {
		
	}
}
