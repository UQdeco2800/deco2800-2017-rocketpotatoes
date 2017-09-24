package com.deco2800.potatoes.entities.tree;


import com.deco2800.potatoes.entities.trees.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class DamageTreeTest {

    DamageTree defaultDamageTree;
    DamageTree iceTreeDamageTree;
    DamageTree acornTreeDamageTree;
    DamageTree nullTypeDamageTree;
    DamageTree fireTreeDamageTree;

    @Before
    public void setup() {

        defaultDamageTree = new DamageTree(0, 0, 0);
        iceTreeDamageTree = new DamageTree(1, 0, 0, new IceTreeType());
        acornTreeDamageTree = new DamageTree(1, 0, 0, new AcornTreeType());
        fireTreeDamageTree = new DamageTree(1, 0, 0, new FireTreeType());
        nullTypeDamageTree = new DamageTree(2, 0, 0,null);

    }

    /* Test getDmageTreeType method */
    @Test
    public void getDamageTreeTypeTest() {
        assertTrue(iceTreeDamageTree.getDamageTreeType() instanceof IceTreeType);
        assertTrue(acornTreeDamageTree.getDamageTreeType() instanceof AcornTreeType);
        assertTrue(fireTreeDamageTree.getDamageTreeType() instanceof FireTreeType);
        assertTrue(defaultDamageTree.getDamageTreeType() instanceof LightningTreeType);
        assertTrue(nullTypeDamageTree.getDamageTreeType() instanceof LightningTreeType);

    }

}
