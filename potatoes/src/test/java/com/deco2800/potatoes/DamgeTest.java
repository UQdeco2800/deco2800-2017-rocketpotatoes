package com.deco2800.potatoes;

import com.deco2800.potatoes.entities.resources.Resource;
import com.deco2800.potatoes.entities.trees.DamageTreeType;
import com.deco2800.potatoes.entities.trees.IceTreeType;
import com.deco2800.potatoes.entities.trees.LightningTreeType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class DamgeTest {
    DamageTreeType IceTree;
    DamageTreeType LightningTree;
    Resource testResource;

    @Before
    public void setUP(){
        IceTree=new IceTreeType();
        LightningTree=new LightningTreeType();
        testResource=new Resource();
    }

    @After
    public void tearDown() {
        IceTree = null;
        LightningTree = null;
        testResource = null;
    }

    @Test
    public void getTypeNameTest(){
        assertEquals(IceTree.getTypeName(),"IceTree");
    }
    @Test
    public void getTextureTest(){
        assertEquals(IceTree.getTexture(),"ice_basic_tree");
    }

    @Test
    public void equalTest(){
        assertTrue(IceTree.equals(IceTree));
        assertFalse(IceTree.equals(testResource));
    }
    @Test
    public void hashCodeTest(){
        assertEquals(IceTree.hashCode(),IceTree.hashCode());


    }
    @Test
    public void compareToTest(){
        assertEquals(IceTree.compareTo(LightningTree),
                -3);


    }

    @Test
    public void toStringTest(){

        assertEquals(IceTree.toString(),"IceTree");

    }

}
