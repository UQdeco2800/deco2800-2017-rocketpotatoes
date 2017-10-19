package com.deco2800.potatoes;

import com.deco2800.potatoes.entities.*;
import org.junit.Test;

public class GoalPotateTest {
	@Test
	public void initTests(){
		GoalPotate test = new GoalPotate();
		test = new GoalPotate(1,1);
		GoalPotate test2 = new GoalPotate(3,3);
		GoalPotate test3 = new GoalPotate(3,3);
		test.rotationAngle();
		test.setPosition(2,2);
		test.collidesWith(test2);
		test3.collidesWith(test2);
		test.getMask();
        test.toString();
        test.getProgressBar();
	}
}

