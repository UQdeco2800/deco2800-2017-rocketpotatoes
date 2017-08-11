package com.deco2800.potatoes.entities.trees;

import java.util.LinkedList;
import java.util.List;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.TimeEvent;

public abstract class AbstractTree extends AbstractEntity implements Tickable {

	private List<TimeEvent> timeEvents = new LinkedList<>();
	private List<TimeEvent> constructionEvents = new LinkedList<>();
	private int constructionLeft = 0; // TODO change to 100 once construction is implemented, or add to constructor

	public AbstractTree() {
	}

	public AbstractTree(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			String texture) {
		super(posX, posY, posZ, xLength, yLength, zLength, texture);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onTick(long i) {
		if (getConstructionLeft() <= 0) {
			for (TimeEvent timeEvent : timeEvents) {
				timeEvent.decreaseProgress(i);
			}
		} else {
			for (TimeEvent timeEvent : constructionEvents) {
				timeEvent.decreaseProgress(i);
			}
		}
	}

	/**
	 * Adds an event to this tree that will trigger every tick when the tree is not
	 * being constructed, when getConstructionLeft() <= 0
	 * 
	 * @param event
	 *            the time event that will be triggered
	 */
	public void registerTimeEvent(TimeEvent event) {
		timeEvents.add(event);
	}

	/**
	 * Adds an event to this tree that will trigger every tick when the tree is
	 * being constructed, when getConstructionLeft() > 0
	 * 
	 * @param event
	 *            the time event that will be triggered
	 */
	public void registerConstructionEvent(TimeEvent event) {
		constructionEvents.add(event);
	}

	/**
	 * @return the percentage of construction left, from 0 to 100
	 */
	public int getConstructionLeft() {
		return constructionLeft;
	}

	/**
	 * @param constructionLeft
	 *            the percentage of construction to set, from 0 to 100
	 */
	public void setConstructionLeft(int constructionLeft) {
		this.constructionLeft = constructionLeft;
	}
}
