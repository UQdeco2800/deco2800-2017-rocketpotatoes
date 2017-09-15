package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.util.Path;
import com.deco2800.potatoes.util.Box3D;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;

import com.deco2800.potatoes.worlds.InitialWorld;

public class PathManagerTest {
    @Before
    public void setUp() throws Exception {
        InitialWorld mockWorld = mock(InitialWorld.class);
        when(mockWorld.getLength()).thenReturn(100);
        when(mockWorld.getWidth()).thenReturn(100);
        GameManager gm = GameManager.get();
        gm.setWorld(mockWorld);
    }

    @Test
    public void reachesGoal() {
        PathManager m = new PathManager();
        Box3D start = new Box3D(0, 0, 0, 1, 1, 1);
        Box3D finish = new Box3D(2, 2, 2, 1, 1, 1);

        Path p = m.generatePath(start, finish);

        assertThat("Start is not the first point of path",
                start.equals(p.nextPoint()), is(equalTo(true)));
        assertThat("Finish is not the last point of path",
                finish.equals(p.goal()), is(equalTo(true)));

    }
}
