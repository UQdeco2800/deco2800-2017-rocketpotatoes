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
	ProgressBarEntity pb;

	private class TestableProgressBarEntity extends ProgressBarEntity {
		public TestableProgressBarEntity() {};
		
		public TestableProgressBarEntity(String texture, List<Color> colours, int height) {
			
		}
		

	}
	
	@Before
	public void setUp() throws Exception {
		pb = new TestableProgressBarEntity("progress_bar", colours, 50);
	}
	
	
}
