package com.deco2800.potatoes.managers;


import com.deco2800.potatoes.collisions.Box2D;
import com.deco2800.potatoes.collisions.Point2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.util.Path;
import com.deco2800.potatoes.worlds.World;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;


import java.util.HashMap;

public class PathManagerTest {

    private class TestableBlockingEntity extends AbstractEntity {

        public TestableBlockingEntity(float posX, float posY, float xLength, float yLength) {
            super(new Box2D(posX, posY, xLength, yLength), xLength, yLength, "texture");
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
                30f,
                5f
                );
        blockingEntity.setStatic(true);
        blockingEntity.setSolid(true);
        HashMap<Integer, AbstractEntity> entityHashMap = new HashMap<>();
        entityHashMap.put(0, blockingEntity);
        when(mockWorld.getEntities()).thenReturn(entityHashMap);
        GameManager gm = GameManager.get();
        gm.setWorld(mockWorld);
    }

    @After
    public void cleanUp() {
        GameManager.get().clearManagers();
    }

    @Test
    public void reachesGoal() {
        PathManager m = new PathManager();
        Point2D start = new Point2D(0, 0);
        Point2D finish = new Point2D(2, 2);

        Path p = m.generatePath(start, finish);


        assertThat("Finish is not the last point of path", finish.equals(p.goal()), is(equalTo(true)));

    }

    @Test
    public void obstacleCheck() {
        PathManager m = new PathManager();
        Point2D start = new Point2D(50, 10);
        Point2D finish = new Point2D(50, 90);

        Path p = m.generatePath(start, finish);


        assertThat("Finish is not the last point of path", finish.equals(p.goal()), is(equalTo(true)));
    }
    @Test
    public void pathTest() {
        PathManager m = new PathManager();
        Point2D start = new Point2D(50, 10);
        Point2D finish = new Point2D(50, 90);

        Path p = m.generatePath(start, finish);
        p.getNodes();
        p.getAngle();
        p.setAngle(2);
    }

}
