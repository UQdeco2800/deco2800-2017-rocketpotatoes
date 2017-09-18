package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.util.Path;
import com.deco2800.potatoes.util.Box3D;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;

import com.deco2800.potatoes.worlds.World;

import java.util.HashMap;

public class PathManagerTest {

    private class TestableBlockingEntity extends AbstractEntity {

        public TestableBlockingEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength) {
            super(posX, posY, posZ, xLength, yLength, zLength, "texture");
        }

    }

    @Before
    public void setUp() throws Exception {
        World mockWorld = mock(World.class);
        when(mockWorld.getLength()).thenReturn(100);
        when(mockWorld.getWidth()).thenReturn(100);
        // Create generic entity to block paths.
        TestableBlockingEntity blockingEntity = new TestableBlockingEntity(
                50f,
                50f,
                0f,
                30f,
                5f,
                1f
                );
        blockingEntity.setStaticCollideable(true);
        HashMap<Integer, AbstractEntity> entityHashMap = new HashMap<>();
        entityHashMap.put(0, blockingEntity);
        when(mockWorld.getEntities()).thenReturn(entityHashMap);
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

    @Test
    public void obstacleCheck() {
        PathManager m = new PathManager();
        Box3D start = new Box3D(50, 10, 0, 1, 1, 1);
        Box3D finish = new Box3D(50, 90, 0, 1, 1, 1);

        Path p = m.generatePath(start, finish);

        assertThat("Start is not the first point of path",
                start.equals(p.nextPoint()), is(equalTo(true)));
        assertThat("Finish is not the last point of path",
                finish.equals(p.goal()), is(equalTo(true)));
    }

    @Test
    public void leakTest() {
        int rounds = 1;
        for (int i = 0; i < rounds; i++) {
          obstacleCheck();
        }
    }
}
