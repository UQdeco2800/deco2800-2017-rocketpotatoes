package com.deco2800.potatoes;


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
        iceTreeDamageTree = new DamageTree(1, 0, 0, new IceTree());
        acornTreeDamageTree = new DamageTree(1, 0, 0, new AcornTree());
        fireTreeDamageTree = new DamageTree(1, 0, 0, new FireTree());
        nullTypeDamageTree = new DamageTree(2, 0, 0,null);

    }

    /* Test getDmageTreeType method */
    @Test
    public void getDamageTreeTypeTest() {
        assertTrue(iceTreeDamageTree.getDamageTreeType() instanceof IceTree);
        assertTrue(acornTreeDamageTree.getDamageTreeType() instanceof AcornTree);
        assertTrue(fireTreeDamageTree.getDamageTreeType() instanceof FireTree);
        assertTrue(defaultDamageTree.getDamageTreeType() instanceof LightningTree);
        assertTrue(nullTypeDamageTree.getDamageTreeType() instanceof LightningTree);

    }

}
