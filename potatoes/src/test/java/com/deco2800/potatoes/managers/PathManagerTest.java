package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.collisions.Point2D;
import com.deco2800.potatoes.util.Path;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;

import com.deco2800.potatoes.worlds.InitialWorld;

public class PathManagerTest {
    @Before
    public void setUp() throws Exception {
        InitialWorld mockWorld = mock(InitialWorld.class);
        GameManager gm = GameManager.get();
        gm.setWorld(mockWorld);
    }

    @Test
    public void reachesGoal() {
        PathManager m = new PathManager();
        Point2D start = new Point2D(0, 0);
        Point2D finish = new Point2D(2, 2);

        Path p = m.generatePath(start, finish);

        assertEquals(start, p.nextPoint());
        assertEquals(finish, p.goal());
    }
}
