package com.deco2800.potatoes.entities.health;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class ProgressBarEntityTest {

	private static List<Color> colours = Arrays.asList(Color.RED, Color.ORANGE, Color.GREEN, Color.BLUE, Color.PURPLE);

	ProgressBarEntity progressBarEntity;
	ProgressBarEntity progressBarEntity2;

	@Before
	public void setUp() throws Exception {
		progressBarEntity = new ProgressBarEntity("progress_bar", colours, 50, 1);
		progressBarEntity2 = new ProgressBarEntity("progress_bar", "layout", 1);
	}

	// Common to all initialisation test
	private void initTestCommon() {
		assertEquals("getTexture() bad init", progressBarEntity.getTexture(), "progress_bar");
		assertEquals("getColours() bad init", progressBarEntity.getColours(), colours);
		assertEquals("getHeight() bad init", progressBarEntity.getHeight(), 50);
		assertEquals("getWidthScale() bad init", progressBarEntity.getWidthScale(), 1, 0f);

	}

	@After
	public void tearDown() {
		progressBarEntity = null;
	}

	@Test
	public void initTest() {
		progressBarEntity = new ProgressBarEntity("progress_bar", colours, 50, 1);
		initTestCommon();
	}

	@Test
	public void initTestEmpty() {
		try {
			progressBarEntity = new ProgressBarEntity();
		} catch (Exception E) {
			fail("No AbstractEntity serializable constructor");
		}
	}

	@Test
	public void getColourTest() {
		// test 5 colour
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(1), Color.PURPLE);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(0.8f), Color.BLUE);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(0.6f), Color.GREEN);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(0.4f), Color.ORANGE);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(0.2f), Color.RED);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(0), Color.RED);

		// test 1 colour
		colours = Arrays.asList(Color.RED);
		progressBarEntity = new ProgressBarEntity(colours);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(1), Color.RED);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(0.5f), Color.RED);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(0), Color.RED);

		// test progress bar with image
		progressBarEntity = new ProgressBarEntity("progress_bar", 50);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(1), Color.WHITE);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(0.5f), Color.WHITE);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(0), Color.WHITE);

	}
	
	@Test
	public void getLayout() {
		assertEquals("layout selected are not correct", progressBarEntity2.getLayoutTexture(), "layout");

	}

}
