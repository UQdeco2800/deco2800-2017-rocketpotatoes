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

    TestableBlockingEntity blockingEntity;
    PathManager pathManager;
    Path path;
    GameManager gameManager;

    @Before
    public void setUp() {

        pathManager = GameManager.get().getManager(PathManager.class);
        World mockWorld = mock(World.class);
        when(mockWorld.getLength()).thenReturn(100);
        when(mockWorld.getWidth()).thenReturn(100);
        // Create generic entity to block paths.
        blockingEntity = new TestableBlockingEntity(
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
        gameManager = GameManager.get();
        gameManager.setWorld(mockWorld);
    }

    @After
    public void cleanUp() {
        GameManager.get().clearManagers();
        path = null;
        gameManager = null;
    }

    @Test
    public void reachesGoal() {

        Point2D start = new Point2D(0, 0);
        Point2D finish = new Point2D(2, 2);

        path = pathManager.generatePath(start, finish);


        assertThat("Finish is not the last point of path",
                finish.equals(path.goal()), is(equalTo(true)));

    }

    @Test
    public void obstacleCheck() {

        Point2D start = new Point2D(50, 10);
        Point2D finish = new Point2D(50, 90);

        path = pathManager.generatePath(start, finish);


        assertThat("Finish is not the last point of path",
                finish.equals(path.goal()), is(equalTo(true)));
    }
    @Test
    public void pathTest() {

        Point2D start = new Point2D(50, 10);
        Point2D finish = new Point2D(50, 90);

        path = pathManager.generatePath(start, finish);
        path.getNodes();
        path.getAngle();
        path.setAngle(2);
    }

}
