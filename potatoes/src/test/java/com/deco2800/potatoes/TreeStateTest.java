package com.deco2800.potatoes;

import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.managers.Inventory;
import com.deco2800.potatoes.managers.TreeState;
import org.junit.*;

/**
 * Tests for TreeState class
 *
 * @author Dion Lao
 */
public class TreeStateTest {

    TreeState treeState;

    @Before
    public void setUp() {

    }

    @Test
    public void testInit(){
        // With empty constructor
        treeState = new TreeState();
        assert(treeState.getCost().equals(new Inventory()));
        assert(treeState.getTree().equals(null));
        assert(!treeState.isUnlocked());

        // With full constructor
        treeState = new TreeState()

    }
}
