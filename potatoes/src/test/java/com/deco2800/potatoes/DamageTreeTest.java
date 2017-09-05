package com.deco2800.potatoes;

import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.trees.DamageTree;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.Inventory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DamageTreeTest {


    DamageTree defaultDamageTree;
    DamageTree customDamageTree;
    DamageTree nullTypeDamageTree;


    Damage LightningTree;
    Damage AcornTree;
    Damage IceTree;


    int testAmount = ResourceTree.DEFAULT_GATHER_CAPACITY/2; // Use a value less than the default

    @Before
    public void setup() {

        defaultDamageTree = new DamageTree(0, 0, 0);
        customDamageTree = new DamageTree(1, 0, 0, new IceTree());
        nullTypeDamageTree = new DamageTree(2, 0, 0,null);

    }

    /* Test initialising the damage tree */
    @Test
    public void initTest() {
        assertTrue(customDamageTree.getDamageTreeType() instanceof IceTree);
        assertTrue(defaultDamageTree.getDamageTreeType() instanceof LightningTree);
        assertTrue(nullTypeDamageTree.getDamageTreeType() instanceof LightningTree);

    }

}
