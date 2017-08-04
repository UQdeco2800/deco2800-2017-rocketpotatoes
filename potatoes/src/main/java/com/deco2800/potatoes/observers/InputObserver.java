package com.deco2800.potatoes.observers;

/**
 * Created by woody on 30-Jul-17.
 */
public interface InputObserver {

	void notifyKeyDown(int keycode);

	void notifyKeyUp(int keycode);

	void notifyKeyTyped(char character);

	void notifyTouchDown(int screenX, int screenY, int pointer, int button);

	void notifyTouchUp(int screenX, int screenY, int pointer, int button);

	void notifyTouchDragged(int screenX, int screenY, int pointer);

	void notifyMouseMoved(int screenX, int screenY);

	void notifyScrolled(int amount);

}
