package com.deco2800.potatoes.observers;

/**
 * Created by woody on 30-Jul-17.
 */
@FunctionalInterface
public interface MouseMovedObserver {

	void notifyMouseMoved(int screenX, int screenY);

}
