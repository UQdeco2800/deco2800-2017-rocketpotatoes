package com.deco2800.marsinvasion;

import com.deco2800.moos.entities.Tree;
import com.deco2800.moos.renderers.Renderable;
import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.worlds.ExampleWorld;
import com.deco2800.potatoes.util.WorldUtil;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WorldUtilTest {
	@Test
	public void TestDistanceFunctions() {
		AbstractWorld w = mock(ExampleWorld.class);
		Tree t1 = new Tree(w, 1, 1, 1);
		Tree t2 = new Tree(w, 2, 2, 1);
		List<Renderable> e = new ArrayList<>();
		e.add(t1);
		e.add(t2);

		when(w.getEntities()).thenReturn((ArrayList<Renderable>) e);

		WorldUtil.closestEntityToPosition(w, 0, 0, 2);
		assertEquals(t1, WorldUtil.closestEntityToPosition(w, 0, 0, 2).get());
	}
}
