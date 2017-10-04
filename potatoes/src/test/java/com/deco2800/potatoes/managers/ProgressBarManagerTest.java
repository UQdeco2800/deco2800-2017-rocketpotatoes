package com.deco2800.potatoes.managers;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.deco2800.potatoes.BaseTest;

public class ProgressBarManagerTest extends BaseTest {

	ProgressBarManager progressBarManger;

	@Before
	public void setUp() {
		progressBarManger = new ProgressBarManager();
	}

	@Test
	public void initTest() {
		ArrayList<Boolean> progressValues = new ArrayList<Boolean>();
		progressValues.add(true);
		progressValues.add(true);
		progressValues.add(true);
		progressValues.add(true);

		assertEquals("progress values should all be true", progressBarManger.getProgressValues(), progressValues);
		progressBarManger.togglePlayerProgress();
		progressBarManger.togglePlayerProgress();
		progressBarManger.togglePotatoProgress();
		progressBarManger.togglePotatoProgress();
		progressBarManger.toggleAlliesProgress();
		progressBarManger.toggleAlliesProgress();
		progressBarManger.toggleEnemyProgress();
		progressBarManger.toggleEnemyProgress();

	}

	@Test
	public void testPlayerToggle() {
		ArrayList<Boolean> progressValues = new ArrayList<Boolean>();
		progressValues.add(false);
		progressValues.add(true);
		progressValues.add(true);
		progressValues.add(true);
		progressBarManger.togglePlayerProgress();
		assertEquals("progress values should contain 3/4 trues", progressBarManger.getProgressValues(), progressValues);
	}

	@Test
	public void testPotatoToggle() {
		ArrayList<Boolean> progressValues = new ArrayList<Boolean>();
		progressValues.add(true);
		progressValues.add(false);
		progressValues.add(true);
		progressValues.add(true);
		progressBarManger.togglePotatoProgress();
		assertEquals("progress values should contain 3/4 trues", progressBarManger.getProgressValues(), progressValues);

	}

	@Test
	public void testAlliesToggle() {
		ArrayList<Boolean> progressValues = new ArrayList<Boolean>();
		progressValues.add(true);
		progressValues.add(true);
		progressValues.add(false);
		progressValues.add(true);
		progressBarManger.toggleAlliesProgress();
		assertEquals("progress values should contain 3/4 trues", progressBarManger.getProgressValues(), progressValues);

	}

	@Test
	public void testEnemyToggle() {
		ArrayList<Boolean> progressValues = new ArrayList<Boolean>();
		progressValues.add(true);
		progressValues.add(true);
		progressValues.add(true);
		progressValues.add(false);
		progressBarManger.toggleEnemyProgress();
		assertEquals("progress values should contain 3/4 trues", progressBarManger.getProgressValues(), progressValues);

	}
}
