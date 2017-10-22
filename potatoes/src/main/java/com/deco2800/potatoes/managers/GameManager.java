package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.worlds.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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

	private boolean paused = false;
	private long seed = (long)(Math.random() * 10000000);
	private Random random = new Random(seed);

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
		// Stores managers on the world
		getManager(WorldManager.class).setWorldCached(false);
		if (gameWorld != null) {
			for (Manager manager : managers) {
				if (manager instanceof ForWorld) {
					gameWorld.addManager(manager);
				}
			}
			// Remove ForWorld managers from the manager list
			Iterator<Manager> iter = managers.iterator();
			while (iter.hasNext()) {
				Manager manager = iter.next();
				if (manager instanceof ForWorld) {
					iter.remove();
				}
			}
			// Add managers from the new world
			for (Manager manager : world.getManagers()) {
				managers.add(manager);
			}
		}
		gameWorld = world;
		
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
		for (int index = 0; index < managers.size(); index++) {
			Manager m = managers.get(index);
			if (m instanceof TickableManager) {
				((TickableManager) m).onTick(i);
			}
		}
	}

	/**
	 * Deletes all existing managers. Used to reset the game
	 */
	public void clearManagers() {
		managers.clear();
	}

	/**
	 * Resets the entire game state
	 */
	public static void resetState() {
		instance = null;
	}

	/**
	 * Returns if the game is paused or not
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * Sets if the game is paused or not
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public Random getRandom() {
		return random;
	}

	public long	getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
		random = new Random(seed);
	}
}
