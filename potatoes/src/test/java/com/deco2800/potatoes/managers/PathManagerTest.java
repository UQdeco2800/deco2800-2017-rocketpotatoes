package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.util.Path;
import com.deco2800.potatoes.util.Box3D;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;

import com.deco2800.potatoes.worlds.InitialWorld;

public class PathManagerTest {
    @Before
    public void setUp() throws Exception {
        InitialWorld mockWorld = mock(InitialWorld.class);
        mockWorld.setLength(100);
        mockWorld.setWidth(100);
        GameManager gm = GameManager.get();
        gm.setWorld(mockWorld);
    }

    @Test
    public void reachesGoal() {
        PathManager m = new PathManager();
        Box3D start = new Box3D(0, 0, 0, 1, 1, 1);
        Box3D finish = new Box3D(2, 2, 2, 1, 1, 1);

        Path p = m.generatePath(start, finish);

        assertEquals(start, p.nextPoint());
        assertEquals(finish, p.goal());
    }
}
