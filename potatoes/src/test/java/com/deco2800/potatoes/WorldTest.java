package com.deco2800.potatoes;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.worlds.World;
import com.deco2800.potatoes.util.WorldUtil;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.Before;
import com.badlogic.gdx.Input;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class WorldTest {
    World world;
    TestEntity test = new TestEntity();
    @Rule
    public ExpectedException execption = ExpectedException.none();

    @Before
    public void setup() {
        world = new World();
    }

    @Test
    public void setTest() {
        world.setWidth(1);
        world.setLength(1);

    }

    @Test
    public void entityTest() {
        world.addEntity(test);
        world.removeEntity(0);
        execption.expect(IllegalStateException.class);
        world.addEntity(test, 0);
    }
    @Test
    public void entityTest2() {
        world.addEntity(test);
        world.addEntity(test);
        world.addEntity(test);
        world.addEntity(test);
        world.deSelectAll();
    }

    private class TestEntity extends AbstractEntity {

    }
}
