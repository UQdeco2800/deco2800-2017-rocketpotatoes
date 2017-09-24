package com.deco2800.potatoes;

import com.deco2800.potatoes.entities.trees.*;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;

import org.junit.Before;
import org.junit.Test;

import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.SeedResource;
import com.deco2800.potatoes.entities.TimeEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.Assert.*;

public class UpgradeStatsTest {
	List<TimeEvent<AbstractTree>> normalEvents;
	List<TimeEvent<AbstractTree>> constructionEvents;
	UpgradeStats test;

	@Before
	public void setup() {
		normalEvents = new LinkedList<>();
		constructionEvents = new LinkedList<>();
		test = new UpgradeStats(10, 1000, 8f, 5000, 1, normalEvents, constructionEvents, "real_tree");
	}

	@Test
	public void getEventsTest() {
		assertFalse("Normal events copy returned the same object",
				test.getNormalEventsCopy() == test.getNormalEventsCopy());
		assertFalse("Constuction events copy returned the same object",
				test.getConstructionEventsCopy() == test.getConstructionEventsCopy());
		assertTrue("Normal events copy didn't return the same object",
				test.getNormalEventsReference() == test.getNormalEventsReference());
		assertTrue("Construction events copy didn't return the same object",
				test.getConstructionEventsReference() == test.getConstructionEventsReference());
	}

	@Test
	public void emptyTest() {
		new UpgradeStats();
	}

	@Test(expected = NoSuchElementException.class)
	public void getFailTest() {
		test.get("non-element");
	}

	@Test
	public void getPropertiesTest() {
		Set<String> properties = test.getProperties();
		assertTrue("hp not in properties", properties.contains("hp"));
		assertTrue("speed not in properties", properties.contains("speed"));
		assertTrue("range not in properties", properties.contains("range"));
		assertTrue("constructionTime not in properties", properties.contains("constructionTime"));
		assertTrue("normalEvents not in properties", properties.contains("normalEvents"));
		assertTrue("constructionEvents not in properties", properties.contains("constructionEvents"));
		assertTrue("texture not in properties", properties.contains("texture"));
	}

	@Test
	public void removeConstructionResourcesTest() {
		PlayerManager pm = GameManager.get().getManager(PlayerManager.class);
		pm.setPlayer(new Player(0, 0));
		pm.getPlayer().getInventory().removeInventoryResource(new SeedResource());
		pm.getPlayer().getInventory().addInventoryResource(new SeedResource());
		assertFalse("Not enough resources but construction succeeded", test.removeConstructionResources());
		pm.getPlayer().getInventory().updateQuantity(new SeedResource(), (int) test.get("resourceCost"));
		assertTrue("Construction failed with enough resources", test.removeConstructionResources());
	}

	@Test
	public void getHpTest() {
		assertEquals("incorrectreturn value hp", test.getHp(), 10);
	}

	@Test
	public void getRangeTest() {
		assertEquals("incorrectreturn value range", test.getRange(), 8, 0.01);
	}

	@Test
	public void getSpeedTest() {
		assertEquals("incorrectreturn value speed", test.getSpeed(), 1000);
	}

	@Test
	public void getTextureTest() {
		assertEquals("incorrectreturn value texture", test.getTexture(), "real_tree");
	}

	@Test
	public void getTimeTest() {
		assertEquals("incorrectreturn value speed", test.getConstructionTime(), 5000);
	}
}