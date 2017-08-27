package com.deco2800.potatoes;

import com.deco2800.potatoes.entities.trees.*;
import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.worlds.AbstractWorld;
import com.deco2800.potatoes.managers.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TreeProjectileShootEventTest {
    TreeProjectileShootEvent testEvent = new TreeProjectileShootEvent(10);
    private static final int RELOAD = 100;
    private static final float HEALTH = 10f;
    private static final float RANGE = 8f;
    ProjectileTree testTree = new ProjectileTree(10, 10, 0, "real_tree", RELOAD, RANGE, HEALTH);

    @Test
    public void emptyTest() {
        TreeProjectileShootEvent nullEvent = new TreeProjectileShootEvent();
    }

    @Test
    public void copyTest() {
        testEvent.copy();
    }

    @Test
    public void actionTest() {
        GameManager.get().setWorld(new TestWorld());
        GameManager.get().getWorld().addEntity(testTree);
        testEvent.action(testTree);
        GameManager.get().getWorld().addEntity(new Squirrel(9, 9, 0));
        testEvent.action(testTree);
    }
    private class TestWorld extends AbstractWorld {

    }

}