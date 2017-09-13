package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.worlds.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Game manager manages all the components of the game.
 * Throughout we call GameManager GM
 * Created by timhadwen on 30/7/17.
 */
public class GameManager implements TickableManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(GameManager.class);

	private static GameManager instance = null;

	private List<Manager> managers = new ArrayList<>();

	private World gameWorld;
	
	private World mainWorld;

	/**
	 * Returns an instance of the GM
	 * @return GameManager
	 */
	public static GameManager get() {
		if (instance == null) {
			instance = new GameManager();
		}
		return instance;
	}

	/**
	 * Private constructor to inforce use of get()
	 */
	private GameManager() {
		
	}

	/**
	 * Adds a manager component to the GM
	 * @param manager
	 */
	public void addManager(Manager manager) {
		managers.add(manager);
	}

	/**
	 * Retrives a manager from the list.
	 * If the manager does not exist one will be created, added to the list and returned
	 * @param type The class type (ie SoundManager.class)
	 * @return A Manager component of the requested type
	 */
	@SuppressWarnings("unchecked")
	public <M extends Manager> M getManager(Class<M> type) {
		/* Check if the manager exists */
		for (Manager m : managers) {
			if (m.getClass() == type) {
				return (M) m;
			}
		}

		/* Otherwise create one */
		try {
			Constructor<?> ctor = type.getConstructor();
			this.addManager((Manager) ctor.newInstance());
		} catch (Exception e) {
			LOGGER.error("Failed to create new manager.", e);
		}

		/* And then return it */
		for (Manager m : managers) {
			if (m.getClass() == type) {
				return (M) m;
			}
		}
		LOGGER.warn("GameManager.get returned null! It shouldn't have!");
		return null;
	}

	/**
	 * Sets the current game world
	 * @param world
	 */
	public void setWorld(World world) {
		// Hopefully stores the events, not sure though
		if (gameWorld != null) {
			gameWorld.setEventManager(getManager(EventManager.class));
		}
		gameWorld = world;
		managers.remove(getManager(EventManager.class));
		if (world.getEventManager() != null) {
			managers.add(world.getEventManager());
		}
	}

	/**
	 * Gets the current game world
	 * @return
	 */
	public World getWorld() {
		return gameWorld;
	}
	
	/**
	 * Sets the main/home game world
	 * @param world
	 * 				The world to set to the main/home world
	 */
	public void setMainWorld(World world) {
		this.mainWorld = world;
	}

	/**
	 * Gets the main/home game world
	 * @return mainWorld
	 * 				The main/home world
	 */
	public World getMainWorld() {
		return mainWorld;
	}

	/**
	 * On tick method for ticking managers with the TickableManager interface
	 * @param i
	 */
	@Override
	public void onTick(long i) {
		for (Manager m : managers) {
			if (m instanceof TickableManager) {
				((TickableManager) m).onTick(0);
			}
		}
	}

	/**
	 * Deletes all existing managers. Used to reset the game
	 */
	public void clearManagers() {
		managers.clear();
	}
}
