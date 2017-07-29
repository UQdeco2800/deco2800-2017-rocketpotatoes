package com.deco2800.potatoes.entities;

import com.badlogic.gdx.scenes.scene2d.ui.Button;

/**
 * An interface to make an Entity selectable
 */
public interface Selectable {

	/**
	 * Returns true if the Entity is selected.
	 * 
	 * @return Returns true if the Entity is selected.
	 */
	boolean isSelected();

	/**
	 * Deselects the entity.
	 */
	void deselect();

	/**
	 * Returns the button.
	 * 
	 * @return Returns the button.
	 */
	Button getButton();

	/**
	 * Callback on button press.
	 */
	void buttonWasPressed();
}
