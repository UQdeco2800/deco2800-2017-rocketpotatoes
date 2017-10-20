package com.deco2800.potatoes.entities.tree;

import com.deco2800.potatoes.entities.trees.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DefenseTreeTest {
	
	DefenseTree defenseTree;
	
	@Before
	 public void setup() {       
		defenseTree = new DefenseTree(1,0);
    }
	
	@After
	public void tearDown(){
		defenseTree = null;
	}
	
	@Test
	public void stringTest() {
		defenseTree.getName();
	}
	
	@Test
	public void textureTest() {
		defenseTree.getTexture();
	}
	
}
