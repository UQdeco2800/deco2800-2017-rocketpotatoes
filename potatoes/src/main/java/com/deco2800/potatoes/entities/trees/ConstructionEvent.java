package com.deco2800.potatoes.entities.trees;

import com.deco2800.potatoes.entities.TimeEvent;

// Maybe move this out
class ConstructionEvent extends TimeEvent<AbstractTree> {
	public ConstructionEvent(int constructionTime) {
		setDoReset(true);
		setResetAmount(constructionTime / 100);
		reset();
	}

	@Override
	public void action(AbstractTree param) {
		param.decrementConstructionLeft();
		if (param.getConstructionLeft() <= 0) {
			// Changes to the normal events since construction is over
			param.setRegisteredEvents(false);
		}
	}

	@Override
	public TimeEvent<AbstractTree> copy() {
		return null;
	}
}