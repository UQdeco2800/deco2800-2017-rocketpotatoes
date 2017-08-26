package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.renderering.Renderable;

public class Effect extends AbstractEntity implements Tickable, Renderable {

	public Effect() {
		super(0, 0, 0, 0.4f, 0.4f, 0.4f, 0.4f, 0.4f, true,"");
		System.out.println("sdasdas ");
	}

	@Override
	public void onTick(long time) {

	}

}
