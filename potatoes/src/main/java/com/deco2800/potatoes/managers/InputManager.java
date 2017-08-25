package com.deco2800.potatoes.managers;

import java.util.ArrayList;

import com.badlogic.gdx.InputProcessor;
import com.deco2800.potatoes.observers.KeyDownObserver;
import com.deco2800.potatoes.observers.KeyUpObserver;
import com.deco2800.potatoes.observers.MouseMovedObserver;
import com.deco2800.potatoes.observers.ScrollObserver;
import com.deco2800.potatoes.observers.TouchDownObserver;
import com.deco2800.potatoes.observers.TouchDraggedObserver;
import com.deco2800.potatoes.observers.TouchUpObserver;

/**
 * Created by woody on 30-Jul-17.
 */
public class InputManager extends Manager implements InputProcessor {

	private ArrayList<KeyDownObserver> keyDownListeners = new ArrayList<>();

	private ArrayList<KeyUpObserver> keyUpListeners = new ArrayList<>();

	private ArrayList<TouchDownObserver> touchDownListeners = new ArrayList<>();

	private ArrayList<TouchUpObserver> touchUpListeners = new ArrayList<>();

	private ArrayList<TouchDraggedObserver> touchDragegdListeners = new ArrayList<>();

	private ArrayList<MouseMovedObserver> mouseMovedListeners = new ArrayList<>();

	private ArrayList<ScrollObserver> scrollListeners = new ArrayList<>();


	public void addKeyDownListener(KeyDownObserver observer) {
		keyDownListeners.add(observer);
	}

	public void removeKeyDownListener(KeyDownObserver observer) {
		keyDownListeners.remove(observer);
	}

	public void addKeyUpListener(KeyUpObserver observer) {
		keyUpListeners.add(observer);
	}

	public void removeKeyUpListener(KeyUpObserver observer) {
		keyUpListeners.remove(observer);
	}

	public void addTouchDownListener(TouchDownObserver observer) {
		touchDownListeners.add(observer);
	}

	public void removeTouchDownListener(TouchDownObserver observer) {
		touchDownListeners.remove(observer);
	}

	public void addTouchUpListener(TouchUpObserver observer) {
		touchUpListeners.add(observer);
	}

	public void removeTouchUpListener(TouchUpObserver observer) {
		touchUpListeners.remove(observer);
	}

	public void addTouchDraggedListener(TouchDraggedObserver observer) {
		touchDragegdListeners.add(observer);
	}

	public void removeTouchDraggedListener(TouchDraggedObserver observer) {
		touchDragegdListeners.remove(observer);
	}

	public void addMouseMovedListener(MouseMovedObserver observer) {
		mouseMovedListeners.add(observer);
	}

	public void removeMouseMovedListener(MouseMovedObserver observer) {
		mouseMovedListeners.remove(observer);
	}

	public void addScrollListener(ScrollObserver observer) {
		scrollListeners.add(observer);
	}

	public void removeScrollListener(ScrollObserver observer) {
		scrollListeners.remove(observer);
	}

	@Override
	public boolean keyDown(int keycode) {
		for (KeyDownObserver observer : keyDownListeners) {
			observer.notifyKeyDown(keycode);
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		for (KeyUpObserver observer : keyUpListeners) {
			observer.notifyKeyUp(keycode);
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		for (TouchDownObserver observer : touchDownListeners) {
			observer.notifyTouchDown(screenX, screenY, pointer, button);
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		for (TouchUpObserver observer : touchUpListeners) {
			observer.notifyTouchUp(screenX, screenY, pointer, button);
		}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		for (TouchDraggedObserver observer : touchDragegdListeners) {
			observer.notifyTouchDragged(screenX, screenY, pointer);
		}
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		for (MouseMovedObserver observer : mouseMovedListeners) {
			observer.notifyMouseMoved(screenX, screenY);
		}
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		for (ScrollObserver observer : scrollListeners) {
			observer.notifyScrolled(amount);
		}
		return true;
	}
}
