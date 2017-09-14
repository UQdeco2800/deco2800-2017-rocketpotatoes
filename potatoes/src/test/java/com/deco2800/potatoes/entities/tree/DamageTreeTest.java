package com.deco2800.potatoes.entities.tree;


import com.deco2800.potatoes.entities.trees.AcornTree;
import com.deco2800.potatoes.entities.trees.DamageTree;
import com.deco2800.potatoes.entities.trees.IceTree;
import com.deco2800.potatoes.entities.trees.LightningTree;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class DamageTreeTest {


    DamageTree defaultDamageTree;
    DamageTree iceTreeDamageTree;
    DamageTree acornTreeDamageTree;
    DamageTree nullTypeDamageTree;



    @Before
    public void setup() {

        defaultDamageTree = new DamageTree(0, 0, 0);
        iceTreeDamageTree = new DamageTree(1, 0, 0, new IceTree());
        acornTreeDamageTree = new DamageTree(1, 0, 0, new AcornTree());
        nullTypeDamageTree = new DamageTree(2, 0, 0,null);

    }

    /* Test initialising the damage tree */
    @Test
    public void initTest() {
        assertTrue(iceTreeDamageTree.getDamageTreeType() instanceof IceTree);
        assertTrue(acornTreeDamageTree.getDamageTreeType() instanceof AcornTree);
        assertTrue(defaultDamageTree.getDamageTreeType() instanceof LightningTree);
        assertTrue(nullTypeDamageTree.getDamageTreeType() instanceof LightningTree);

    }

}
