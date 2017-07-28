package com.deco2800.potatoes.entities;

import com.badlogic.gdx.scenes.scene2d.ui.Button;

/**
 * An interface to make an Entity selectable
 */
public interface Selectable {
	boolean isSelected();
	void deselect();
	Button getButton();
	void buttonWasPressed();
}
