package com.deco2800.potatoes.entities.trees;

import java.util.LinkedList;
import java.util.List;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.TimeEvent;

public abstract class AbstractTree extends AbstractEntity implements Tickable {
	
	List<TimeEvent> timeEvents = new LinkedList<>();
	
	public AbstractTree(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			String texture) {
		super(posX, posY, posZ, xLength, yLength, zLength, texture);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onTick(long time) {
		// TODO scale with FPS, maybe make progress a float to help with this
		for (TimeEvent timeEvent : timeEvents) {
			timeEvent.decreaseProgress(1);
		}
	}
	
	public void registerTimeEvent(TimeEvent event) {
		timeEvents.add(event);
	}
}

