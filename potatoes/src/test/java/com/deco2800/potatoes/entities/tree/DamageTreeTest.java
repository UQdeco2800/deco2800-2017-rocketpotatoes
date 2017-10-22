package com.deco2800.potatoes.entities.tree;


import com.deco2800.potatoes.entities.trees.*;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DamageTreeTest {

    DamageTree defaultDamageTree;
    DamageTree iceTreeDamageTree;
    DamageTree acornTreeDamageTree;
    DamageTree nullTypeDamageTree;
    DamageTree fireTreeDamageTree;
    DamageTree cactusTreeDamageTree;
    DamageTree coralTreeDamageTree;

    @Before
    public void setup() {
        defaultDamageTree = new DamageTree();
        defaultDamageTree = new DamageTree(0, 0);
        iceTreeDamageTree = new DamageTree(1, 0, new IceTreeType());
        acornTreeDamageTree = new DamageTree(1, 0, new AcornTreeType());
        fireTreeDamageTree = new DamageTree(1, 0, new FireTreeType());
        cactusTreeDamageTree = new DamageTree(1, 0, new CactusTreeType());
        coralTreeDamageTree = new DamageTree(1, 0, new CoralTreeType());
        nullTypeDamageTree = new DamageTree(2, 0, null);

    }

    @After
    public void tearDown() {
        defaultDamageTree = null;
        iceTreeDamageTree = null;
        acornTreeDamageTree = null;
        fireTreeDamageTree = null;
        cactusTreeDamageTree = null;
        coralTreeDamageTree = null;
        nullTypeDamageTree = null;
    }

    /* Test getDmageTreeType method */
    @Test
    public void getDamageTreeTypeTest() {
        assertTrue(iceTreeDamageTree.getDamageTreeType() instanceof IceTreeType);
        assertTrue(acornTreeDamageTree.getDamageTreeType() instanceof AcornTreeType);
        assertTrue(fireTreeDamageTree.getDamageTreeType() instanceof FireTreeType);
        assertTrue(defaultDamageTree.getDamageTreeType() instanceof LightningTreeType);
        assertTrue(nullTypeDamageTree.getDamageTreeType() instanceof LightningTreeType);
        assertTrue(cactusTreeDamageTree.getDamageTreeType() instanceof CactusTreeType);
        assertTrue(coralTreeDamageTree.getDamageTreeType() instanceof CoralTreeType);
        
        assertEquals(defaultDamageTree.createCopy(), new DamageTree(0, 0));
        assertEquals(defaultDamageTree.getName(), "Lightning Tree");
        assertEquals(iceTreeDamageTree.getName(), "Ice Tree");
        assertEquals(acornTreeDamageTree.getName(), "Acorn Tree");
        assertEquals(fireTreeDamageTree.getName(), "Fire Tree");
        assertEquals(cactusTreeDamageTree.getName(), "Cactus Tree");
        assertEquals(coralTreeDamageTree.getName(), "Coral Tree");
        assertEquals(nullTypeDamageTree.getName(), "Lightning Tree");

    }
    @Test
    public void otherTreesTest() {
        DefenseTree dTree = new DefenseTree(1,2);
        dTree.createCopy();
        dTree.getAllUpgradeStats();
        CactusTreeType cac = new CactusTreeType();
    }

}
