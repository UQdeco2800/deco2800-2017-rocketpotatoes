package com.deco2800.potatoes.managers;

import java.util.List;
import java.util.HashMap;

import com.deco2800.potatoes.worlds.AbstractWorld;
import com.deco2800.potatoes.worlds.*;

/**
 * Manager for worlds. Stores and generates all the worlds.
 */
public class WorldManager extends Manager {
	private List<AbstractWorld> worlds;
	private HashMap<String, AbstractWorld> worldHash;

	public WorldManager() {

	}

	/**
	 * Gets the given world where 0 is the main world. The world is generated if it
	 * hasn't already been.
	 * 
	 * @param index
	 *            the number of the world to get
	 * @return the world
	 */
	public AbstractWorld getWorld(int index) {
		if (index < worlds.size()) {
			return worlds.get(index);
		} else {
			// Generate the world here
			return null;
		}
	}
	public AbstractWorld getWorldHash(String name) {
		if (worldHash.get(name)!= null) {
			return worldHash.get(name);
		} else {
			worldHash.put(name, new InitialWorld());
			// Generate the world here
			return worldHash.get(name);
		}
	}

	public void deleteWorld(String name){
		worldHash.remove(name);
	}

	public void clearWorlds(){
		worldHash.clear();
	}

	/**
	 * Sets the world to the world with the given index. If it does not exist, it will be created.
	 */
	public void setWorld(int index) {
		// GameManager.setWorld will probably need to be updated. Some managers need to be reloaded, etc.
		GameManager.get().setWorld(getWorld(index));
	}
}
