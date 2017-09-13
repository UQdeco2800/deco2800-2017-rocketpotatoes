package com.deco2800.potatoes.worlds;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Selectable;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.MultiplayerManager;
import com.deco2800.potatoes.managers.TextureManager;
import com.deco2800.potatoes.renderering.Renderable;
import com.deco2800.potatoes.worlds.terrain.Terrain;

/**
 * World is the Game World
 *
 * It provides storage for the WorldEntities and other universal world level
 * items.
 */
public class World {
	private static final int TILE_WIDTH = 55;
	private static final int TILE_HEIGHT = 32;
	private Terrain[][] terrain;
	
	private Map<Integer, AbstractEntity> entities = new HashMap<>();
	// Current index of the hashmap i.e. the last value we inserted into, for
	// significantly more efficient insertion)
	// First 16 index's are reserved for clients
	// TODO game will likely crash/break badly when this overflows
	private int currentIndex = 17;
	protected TiledMap map;
	private int width;
	private int length;
	// Store event manager for this world's events
	private EventManager eventManager;

	/**
	 * Returns a list of entities in this world
	 * 
	 * @return All Entities in the world
	 */
	public Map<Integer, AbstractEntity> getEntities() {
		return new HashMap<>(this.entities);
	}

	/**
	 * Returns the current map for this world
	 * 
	 * @return Map object for this world
	 */
	public TiledMap getMap() {
		return this.map;
	}
	
	/**
	 * Create a new map based on the terrain grid supplied
	 * @param cells
	 */
	public void setCells(Cell[][] cells) {
		width = cells.length;
		length = cells[0].length;
		map = new TiledMap();
		map.getProperties().put("tilewidth", TILE_WIDTH);
		map.getProperties().put("tileheight", TILE_HEIGHT);
		TiledMapTileLayer layer = new TiledMapTileLayer(width, length, TILE_WIDTH, TILE_HEIGHT);
		for (int x = 0; x < cells.length; x++) {
			for (int y = 0; y < cells[x].length; y++) {
				layer.setCell(x, y, cells[x][y]);
			}
		}
		map.getLayers().add(layer);
	}

	/**
	 * Add's an entity into the next available id slot.
	 *
	 * In singleplayer this should work as you expect.
	 *
	 * For multiplayer, this should only be called by the master client. TODO way to
	 * for perform client side prediction.
	 * 
	 * @param entity
	 */
	public void addEntity(AbstractEntity entity) {
		MultiplayerManager m = GameManager.get().getManager(MultiplayerManager.class);
		if (m.isMultiplayer()) {
			if (m.isMaster()) {
				// HashMap because I want entities to have unique ids that aren't necessarily
				// sequential
				// O(n) insertion? Sorry this is pretty hacky :(
				while (true) {
					if (entities.containsKey(currentIndex)) {
						currentIndex++;
					} else {
						// If we're in multiplayer and the master tell other entities.
						entities.put(currentIndex++, entity);

						// Tell other clients about this entity. Note that we should always broadcast
						// master changes AFTER
						// they have actually been made. Since the server will often read the master
						// state for information.
						m.broadcastNewEntity(currentIndex - 1);
						break;
					}
				}
			} else {
				throw new IllegalStateException(
						"Clients who aren't master shouldn't be adding entities when in multiplayer!");
			}
		} else {
			// Singleplayer behaviour
			entities.put(currentIndex++, entity);
		}
	}

	/**
	 * Add's an entity at the id (i.e. the index of the ArrayList). This will
	 * overwrite any existing entity if it is occupied.
	 *
	 * This function should probably only be called for multiplayer purposes, where
	 * entity id's must sync across clients. As such this will throw an error if
	 * you're not in multiplayer
	 * 
	 * @param entity
	 * @param id
	 */
	public void addEntity(AbstractEntity entity, int id) {
		MultiplayerManager m = GameManager.get().getManager(MultiplayerManager.class);
		if (m.isMultiplayer()) {
			entities.put(id, entity);
		} else {
			throw new IllegalStateException("Not in multiplayer, this function should only be used for multiplayer");
		}

	}

	public void removeEntity(int id) {
		entities.remove(id);

		// Tell the other clients if we're master and in multiplayer.
		MultiplayerManager m = GameManager.get().getManager(MultiplayerManager.class);
		if (m.isMultiplayer() && m.isMaster()) {
			m.broadcastEntityDestroy(id);
		}
	}

	public void removeEntity(AbstractEntity entity) {
		for (Map.Entry<Integer, AbstractEntity> e : entities.entrySet()) {
			if (e.getValue() == entity) {
				entities.remove(e.getKey());

				// Tell the other clients if we're master and in multiplayer.
				MultiplayerManager m = GameManager.get().getManager(MultiplayerManager.class);
				if (m.isMultiplayer() && m.isMaster()) {
					m.broadcastEntityDestroy(e.getKey());
				}
				return;
			}
		}

	}

	/**
	 * Sets the tile int the current world at coordinated x and y to the type of the
	 * terrain given. No idea if this currently works
	 * 
	 * @param x
	 * @param y
	 * @param tile
	 */
	public void setTile(int x, int y, Terrain tile) {
		// This needs to be changed, cells should be generated and stored only once
		TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(
				new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture(tile.getTexture()))));
		((TiledMapTileLayer) map.getLayers().get(0)).setCell(x, y, cell);
	}
	
	/**
	 * Deselects all entities.
	 */
	public void deSelectAll() {
		for (Renderable r : this.getEntities().values()) {
			if (r instanceof Selectable) {
				((Selectable) r).deselect();
			}
		}
	}

	/**
	 * Sets the width (x size) of this world. Note this does not change the map.
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Sets the length (y size) of this world. Note this does not change the map.
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * Returns the x size of the world
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the x size of the world
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Sets the terrain grid to the given grid
	 */
	public void setTerrain(Terrain[][] terrain) {
		this.terrain = terrain;
	}

	/**
	 * Returns the terrain of the specified location taken from the height grid
	 */
	public Terrain getTerrain(int x, int y) {
		return terrain[y][x];
	}

	/**
	 * @return the eventManager
	 */
	public EventManager getEventManager() {
		return eventManager;
	}

	/**
	 * @param eventManager the eventManager to set
	 */
	public void setEventManager(EventManager eventManager) {
		this.eventManager = eventManager;
	}
}
