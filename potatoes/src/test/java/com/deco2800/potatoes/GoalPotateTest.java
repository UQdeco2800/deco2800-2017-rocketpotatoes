package com.deco2800.potatoes;

import com.deco2800.potatoes.entities.*;
import org.junit.Test;

public class GoalPotateTest {
	@Test
	public void initTests(){
		GoalPotate test = new GoalPotate();
		test = new GoalPotate(1,1,1);
		GoalPotate test2 = new GoalPotate(3,3,0);
		GoalPotate test3 = new GoalPotate(3,3,0);
		test.rotateAngle();
		test.setPosition(2,2,2);
		test.setPosZ(3);
		test.collidesWith(test2);
		test3.collidesWith(test2);
		test.getBox3D();
		test.toString();
	}
}

