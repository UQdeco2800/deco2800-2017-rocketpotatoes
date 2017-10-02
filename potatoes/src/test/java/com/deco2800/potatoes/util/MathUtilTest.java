package com.deco2800.potatoes.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MathUtilTest {
	@Test
	public void testCompareFloat() {
		float value = 4;
		float diffValue = value + 0.0000005f;
		System.out.println(value == diffValue);
		assertTrue(MathUtil.compareFloat(value, diffValue));
		assertFalse(MathUtil.compareFloat(value, 0));
	}
}
