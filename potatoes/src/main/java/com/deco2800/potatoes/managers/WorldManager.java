package com.deco2800.potatoes.managers;

import java.util.HashMap;

import com.deco2800.potatoes.worlds.AbstractWorld;
import com.deco2800.potatoes.worlds.InitialWorld;

/**
 * Manager for worlds. Stores and generates all the worlds.
 */
public class WorldManager extends Manager {
	private HashMap<Integer, AbstractWorld> worlds;

	public WorldManager() {

	}

	/**
	 * Gets the given world where 0 is the main world. The world is generated if it
	 * hasn't already been.
	 * 
	 * @param key
	 *            the number of the world to get
	 * @return the world
	 */
	public AbstractWorld getWorld(int key) {
		if (worlds.containsKey(key)) {
			return worlds.get(key);
		} else {
			// Generate the world here
			worlds.put(key, new InitialWorld());
			return worlds.get(key);
		}
	}

	public void deleteWorld(int key){
		worlds.remove(key);
	}

	public void clearWorlds(){
		worlds.clear();
	}

	/**
	 * Sets the world to the world with the given index. If it does not exist, it will be created.
	 */
	public void setWorld(int index) {
		// GameManager.setWorld will probably need to be updated. Some managers need to be reloaded, etc.
		GameManager.get().setWorld(getWorld(index));
	}
}
