package com.deco2800.potatoes.managers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.deco2800.potatoes.BaseTest;

public class ProgressBarManagerTest extends BaseTest {

	ProgressBarManager progressBarManger;

	@Before
	public void setUp() {
		progressBarManger = GameManager.get().getManager(ProgressBarManager.class);
	}

	@After
	public void tearDown() {

		GameManager.get().clearManagers();
	}

	@Test
	public void initTest() {
		assertEquals("initial values should be true", progressBarManger.showPlayerProgress(), true);
		assertEquals("initial values should be true", progressBarManger.showPotatoProgress(), true);
		assertEquals("initial values should be true", progressBarManger.showAlliesProgress(), true);
		assertEquals("initial values should be true", progressBarManger.showEnemiesProgress(), true);

	}

	@Test
	public void testToggles() {
		progressBarManger.togglePlayerProgress();
		progressBarManger.togglePotatoProgress();
		progressBarManger.toggleAlliesProgress();
		progressBarManger.toggleEnemiesProgress();

		assertEquals("initial values should be true", progressBarManger.showPlayerProgress(), false);
		assertEquals("initial values should be true", progressBarManger.showPotatoProgress(), false);
		assertEquals("initial values should be true", progressBarManger.showAlliesProgress(), false);
		assertEquals("initial values should be true", progressBarManger.showEnemiesProgress(), false);

		progressBarManger.togglePlayerProgress();
		progressBarManger.togglePotatoProgress();
		progressBarManger.toggleAlliesProgress();
		progressBarManger.toggleEnemiesProgress();

		assertEquals("initial values should be true", progressBarManger.showPlayerProgress(), true);
		assertEquals("initial values should be true", progressBarManger.showPotatoProgress(), true);
		assertEquals("initial values should be true", progressBarManger.showAlliesProgress(), true);
		assertEquals("initial values should be true", progressBarManger.showEnemiesProgress(), true);
	}

}
