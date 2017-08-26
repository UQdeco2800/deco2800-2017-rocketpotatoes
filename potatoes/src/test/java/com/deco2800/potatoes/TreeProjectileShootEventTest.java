package com.deco2800.potatoes;

import com.deco2800.potatoes.entities.trees.*;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.worlds.InitialWorld;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TreeProjectileShootEventTest {
    TreeProjectileShootEvent testEvent = new TreeProjectileShootEvent(10);
    @Test
    public void emptyTest() {
        TreeProjectileShootEvent nullEvent = new TreeProjectileShootEvent();
    }
    public void copyTest() {
        testEvent.copy();
    }
}