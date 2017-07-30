package com.deco2800.potatoes.handlers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.deco2800.moos.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;

/**
 * Keyboard input handler.
 * @author leggy
 *
 */
public class InputListener implements InputProcessor {
	
	private PlayerManager playerManager;
	
	public InputListener() {
		playerManager = (PlayerManager)GameManager.get().getManager(PlayerManager.class);
	}
	
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.W) {
			playerManager.getPlayer().setMovingUp(true);
			playerManager.getPlayer().setMovingDown(false);

		} else if (keycode == Input.Keys.S) {
			playerManager.getPlayer().setMovingUp(false);
			playerManager.getPlayer().setMovingDown(true);

		} else if (keycode == Input.Keys.A) {
			playerManager.getPlayer().setMovingRight(false);
			playerManager.getPlayer().setMovingLeft(true);

		} else if (keycode == Input.Keys.D) {
			playerManager.getPlayer().setMovingRight(true);
			playerManager.getPlayer().setMovingLeft(false);

		} else if (keycode == Input.Keys.EQUALS) {
			if (GameManager.get().getCamera().zoom > 0.1) {
				GameManager.get().getCamera().zoom -= 0.1;
			}
		} else if (keycode == Input.Keys.MINUS) {
			GameManager.get().getCamera().zoom += 0.1;
		} else {
			return false;
		}
		return true;
	}
	

	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.W) {
			playerManager.getPlayer().setMovingUp(false);

		} else if (keycode == Input.Keys.S) {
			playerManager.getPlayer().setMovingDown(false);

		} else if (keycode == Input.Keys.A) {
			playerManager.getPlayer().setMovingLeft(false);

		} else if (keycode == Input.Keys.D) {
			playerManager.getPlayer().setMovingRight(false);

		} else {
			return false;
		}
		return true;
	}

	public boolean keyTyped(char character) {
		return false;
	}

	public boolean touchDown(int x, int y, int pointer, int button) {
		return false;
	}

	public boolean touchUp(int x, int y, int pointer, int button) {
		return false;
	}

	public boolean touchDragged(int x, int y, int pointer) {
		return false;
	}

	public boolean mouseMoved(int x, int y) {
		return false;
	}

	public boolean scrolled(int amount) {
		return false;
	}
}

