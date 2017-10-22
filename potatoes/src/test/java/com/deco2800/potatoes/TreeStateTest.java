package com.deco2800.potatoes;

import com.deco2800.potatoes.entities.resources.FoodResource;
import com.deco2800.potatoes.entities.resources.SeedResource;
import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.entities.trees.ResourceTree;
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
    AbstractTree testTree;
    Inventory cost;

    @Before
    public void setUp() {
        cost = new Inventory();
        cost.addInventoryResource(new SeedResource());
        cost.addInventoryResource(new FoodResource());
        testTree = new ResourceTree();
    }

    @After
    public void tearDown() {
        treeState = null;
        testTree = null;
        cost = null;
    }

    @Test
    public void testInit(){
        // With empty constructor
        treeState = new TreeState();
        assert(treeState.getCost().equals(new Inventory()));
        assert(treeState.getTree().equals(new ResourceTree(0,0,new SeedResource(), 1)));
        assert(!treeState.isUnlocked());

        // With full constructor
        treeState = new TreeState(testTree, cost, true, "resource");
        assert(treeState.getTree().equals(testTree.createCopy()));
        assert(treeState.getCost().equals(cost));
        assert(treeState.isUnlocked() == true);
        assert(treeState.getTreeType().equals("resource"));

        treeState.unlock();
        treeState.setUnlock(true);
        treeState.hashCode();
        treeState.setTree(testTree);
        treeState.setCost(cost);
        treeState.setTreeType("");
        
        treeState = new TreeState(null, cost, true, "resource");
        treeState = new TreeState(testTree, null, true, "resource");
    }
}
