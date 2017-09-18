package com.deco2800.potatoes.entities.portals;

import com.deco2800.potatoes.entities.portals.AbstractPortal;
import com.deco2800.potatoes.entities.portals.BasePortal;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import com.badlogic.gdx.Input;

public class BasePortalTest{
	BasePortal test;
	@Before
	public void setup() {
		test = new BasePortal(  1, 1, 0, 2);
	}

}