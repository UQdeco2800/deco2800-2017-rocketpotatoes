package com.deco2800.potatoes;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.entities.ProgressBarEntity;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class ProgressBarEntityTest {

	private static final List<Color> colours = Arrays.asList(Color.RED, Color.ORANGE, Color.GREEN);
	ProgressBarEntity progressBarEntity;

	private class TestableProgressBarEntity extends ProgressBarEntity {
		public TestableProgressBarEntity() {
		};

		public TestableProgressBarEntity(String texture, List<Color> colours, int height) {
			super(texture, colours, height, 1);
		};
	}
	
	@Before
	public void setUp() throws Exception {
		progressBarEntity = new TestableProgressBarEntity("progress_bar", colours, 50);
	}
	
	// Common to all initialisation test
	private void initTestCommon() {
		assertEquals("getTexture() bad init", progressBarEntity.getTexture(), "progress_bar");
		assertEquals("getColours() bad init", progressBarEntity.getColours(), colours);
		assertEquals("getHeight() bad init", progressBarEntity.getHeight(), 50);
	}
	
	@Test
	public void initTest() {
		progressBarEntity = new TestableProgressBarEntity("progress_bar", colours, 50);
		initTestCommon();
	}
	
	@Test
	public void initTestEmpty() {
		try {
			progressBarEntity = new TestableProgressBarEntity();
		} catch (Exception E) {
			fail("No AbstractEntity serializable constructor");
		}
	}
	
	@Test
	public void getColourTest() {
		// test 3 colours
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(1), Color.GREEN);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(0.5f), Color.ORANGE);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(0), Color.RED);
		
		// test 1 colour
		List<Color> colours = Arrays.asList(Color.RED);
		progressBarEntity = new TestableProgressBarEntity("progress_bar", colours, 50);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(1), Color.RED);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(0.5f), Color.RED);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(0), Color.RED);
		
		// test 5 colour
		colours = Arrays.asList(Color.RED, Color.ORANGE, Color.GREEN, Color.BLUE, Color.PURPLE);
		progressBarEntity = new TestableProgressBarEntity("progress_bar", colours, 50);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(1), Color.PURPLE);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(0.8f), Color.BLUE);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(0.6f), Color.GREEN);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(0.4f), Color.ORANGE);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(0.2f), Color.RED);
		assertEquals("Colour selected is not correct", progressBarEntity.getColour(0), Color.RED);


	}

}
