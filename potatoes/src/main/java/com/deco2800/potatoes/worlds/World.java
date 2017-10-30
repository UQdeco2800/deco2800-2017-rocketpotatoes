package com.deco2800.potatoes.worlds;

import java.awt.Point;
import java.util.*;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.collisions.Point2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Selectable;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.Manager;
import com.deco2800.potatoes.managers.TextureManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.renderering.Renderable;
import com.deco2800.potatoes.util.RTree;
import com.deco2800.potatoes.worlds.terrain.Terrain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * World is the Game World
 *
 * It provides storage for the WorldEntities and other universal world level
 * items.
 */
public class World {
	private static final Logger LOGGER = LoggerFactory.getLogger(World.class);

	/**
	 * Size of the area entites that are in will be put into the same position in the map
	 */
	private static final int GRID_SIZE = 2;

	private static final int TILE_WIDTH = 128;
	private static final int TILE_HEIGHT = 74;
	private Terrain[][] terrain;

	private Map<Integer, AbstractEntity> entities = new HashMap<>();
	private RTree<Integer> entitiesRtree = new RTree<>();
	// Current index of the hashmap i.e. the last value we inserted into, for
	// significantly more efficient insertion)
	// First 16 index's are reserved for clients
	private int currentIndex = 17;
	protected TiledMap map;
	private int width;
	private int length;
	// Store managers for this world
	private Set<Manager> managers = new HashSet<>();

	private Drawable[] background;
	private Terrain[] backgroundTerrain;

	private WorldType worldType;

	public World() {
		map = new TiledMap();
		map.getProperties().put("tilewidth", TILE_WIDTH);
		map.getProperties().put("tileheight", TILE_HEIGHT);
		terrain = new Terrain[WorldManager.WORLD_SIZE][WorldManager.WORLD_SIZE];
		backgroundTerrain = new Terrain[]{new Terrain("", 0.5f, true)};
		worldType = ForestWorld.get();
	}

	public World(WorldType worldType) {
		this();
		this.worldType = worldType;
	}

	/**
	 * Returns a list of entities in this world
	 * Note that looping through all entities to find a subset of them is rather inefficient in most cases.
	 *
	 * @return All Entities in the world
	 */
	@Deprecated
	public Map<Integer, AbstractEntity> getEntities() {
		return new HashMap<>(this.entities);
	}

	/**
	 * Returns the entity associated with the given id
	 */
	public AbstractEntity getEntity(int id) {
		return entities.get(id);
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
	 * Add's an entity into the next available id slot.
	 *
	 * In singleplayer this should work as you expect.
	 *
	 * For multiplayer, this should only be called by the master client.  way to
	 * for perform client side prediction.
	 *
	 * @param entity
	 */
	public void addEntity(AbstractEntity entity) {
		addToMaps(currentIndex++, entity);
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
		addToMaps(id, entity);
	}

	/**
	 * Add the given entity to a random land tile accessible to the default entities (portal, enemy gate)
	 * @param entity the entity to add to the world
	 */
	public void addToLand(AbstractEntity entity) {
		Point p = worldType.getClearSpots().get(GameManager.get().getRandom().nextInt(worldType.clearSpots.size()));
		entity.setPosition(p.x, p.y);
		addEntity(entity);
	}

	/**
	 * Adds the entity to a grass tile, or doesn't add it to the world
	 * @param entity the entity to add to the world
	 */
	public void addToPlantable(AbstractEntity entity) {
		// 10 tries for adding
		for (int i = 0; i < 10; i++) {
			Point p = worldType.getClearSpots().get(GameManager.get().getRandom().nextInt(worldType.clearSpots.size()));
			if (getTerrain(p.x, p.y).isPlantable()) {
				entity.setPosX(p.x);
				entity.setPosY(p.y);
				addEntity(entity);
			}
		}
	}

	/**
	 * Removes the entity with the given id from this world.
	 */
	public void removeEntity(int id) {
		removeFromMaps(id);
	}

	/**
	 * Removes the given entity from this world, if it is in the world. Otherwise,
	 * nothing happens
	 */
	public void removeEntity(AbstractEntity entity) {
		for (Map.Entry<Integer, AbstractEntity> e : entities.entrySet()) {
			if (e.getValue() == entity) {
				removeFromMaps(e.getKey());
				return;
			}
		}
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param maxDistance
	 * @return
	 */
	public Iterator<AbstractEntity> getEntitiesWithinDistance(float x, float y, float maxDistance) {
		return entitiesRtree.findOverlapping(new Circle2D(x, y, maxDistance)).stream().map((id) -> entities.get(id)).iterator();
	}


	/**
	 * Gets the approximate closest entity of the given type to the given position, approximately within the the
	 * given maximum distance.
	 * @param x The x coordinate to measure the distance from
	 * @param y The y coordinate to measure the distance from
	 * @param type The type of entity to check for, {@link AbstractEntity} the closest general entity
	 * @return The closest entity
	 */
	public Optional<AbstractEntity> getClosestEntity(float x, float y, Class<?> type) {
		return Optional.ofNullable(entities.get(entitiesRtree.findClosest(new Point2D(x, y), (id) -> type.isAssignableFrom(entities.get(id).getClass()))));
	}

	/**
	 * Could be updated to be run from {@link AbstractEntity#setPosition(float, float)}
	 */
	public void updatePositions() {
		for (Entry<Integer, AbstractEntity> entry : entities.entrySet()) {
			try {
				if (!entry.getValue().isStatic() && entry.getValue().getMoveSpeed() > 0) {
					entitiesRtree.move(entry.getKey(), entry.getValue().getMask());
				}
			} catch (NoSuchElementException e) {
				// This is fine :fire:
			}
		}
	}

	/**
	 * Adds the entity and associated id to all the maps
	 */
	private void addToMaps(int id, AbstractEntity entity) {
		if (entity.isSolid()) entitiesRtree.insert(id, entity.getMask());
		entities.put(id, entity);
	}

	/**
	 * Removes the entity associated with the given id from all the maps
	 */
	private void removeFromMaps(int id) {
		try {
			entitiesRtree.remove(id);
		} catch (NoSuchElementException e) {
			// This is fine :fire:
		}
		entities.remove(id);
	}

	private Point getGridPoint(AbstractEntity entity) {
		if (entity == null) {
			// Quick fix for tests, should be used normally
			return new Point(0,0);
		}
		return getGridPoint((int)entity.getPosX(), (int)entity.getPosY());
	}

	private Point getGridPoint(float x, float y) {
		// Rounds x and y to a multiple of GRID_SIZE
		return new Point(GRID_SIZE * (int)(0.5 + x / GRID_SIZE), GRID_SIZE * (int)(0.5 + y / GRID_SIZE));
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
		if (!tile.equals(terrain[x][y]) && !backgroundTerrain[0].equals(tile)) {
			GameManager.get().getManager(WorldManager.class).setWorldCached(false);
			terrain[x][y] = tile;
			Cell cell = GameManager.get().getManager(WorldManager.class).getCell(tile);
			((TiledMapTileLayer) map.getLayers().get(0)).setCell(x, y, cell);
		}
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
	public void setTerrain(Terrain[][] newTerrain) {
		if (newTerrain.length != width || newTerrain[0].length != length || map.getLayers().getCount() == 0) {
			width = newTerrain.length;
			length = newTerrain[0].length;
			// remove all map layers
			Iterator<MapLayer> iter = map.getLayers().iterator();
			while(iter.hasNext()) {
				iter.next();
				iter.remove();
			}
			// add our new map layer
			map.getLayers().add(new TiledMapTileLayer(width, length, TILE_WIDTH, TILE_HEIGHT));
		}
		terrain = new Terrain[newTerrain.length][newTerrain[0].length];
		for (int x = 0; x < newTerrain.length; x++) {
			for (int y = 0; y < newTerrain[x].length; y++) {
				setTile(x, y, newTerrain[x][y]);
			}
		}
	}

	/**
	 * Returns the terrain of the specified location taken from the terrain grid
	 */
	public Terrain getTerrain(int x, int y) {
		if (x > 0 && y > 0 && x < width && y < length && terrain[y][x] != null)
			return terrain[y][x];
		else
			// Makes sure terrain finding doesn't crash
			return backgroundTerrain[0];
	}

	/**
	 * Stores the given manager in this world
	 * @param m the manager to add
	 */
	public void addManager(Manager m) {
		managers.add(m);
	}

	/**
	 * @return the managers for this world
	 */
	public Set<Manager> getManagers() {
		return managers;
	}

	/**
	 * Returns the background for beyond the edges of the map
	 */
	public TextureRegionDrawable getBackground() {
		return (TextureRegionDrawable) background[0];
	}

	public TextureRegionDrawable[] getBackgroundArray() {
		return (TextureRegionDrawable[]) background;
	}

	/**
	 * Sets the background for beyond the edges of the map
	 */
	public void setBackground(Terrain terrain) {
		if (terrain.getTexture().equals("water1")) {
			setBackground(Terrain.WATER_ARRAY);
			return;
		}
		try {
			backgroundTerrain = new Terrain[]{terrain};
			TextureRegion textureRegion = new TextureRegion(GameManager.get().getManager(TextureManager.class)
					.getTextureRegion(terrain.getTexture()));
			textureRegion.setRegionHeight(textureRegion.getRegionHeight() * WorldManager.WORLD_SIZE * 2);
			textureRegion.setRegionWidth(textureRegion.getRegionWidth() * WorldManager.WORLD_SIZE * 2);
			textureRegion.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
			background = new TextureRegionDrawable[]{new TextureRegionDrawable(textureRegion)};
		} catch (NullPointerException e) {
			background = new TextureRegionDrawable[]{new TextureRegionDrawable()};
			LOGGER.info(e.getMessage());
		}
	}

	public void setBackground(Terrain[] terrain) {
		try {
			backgroundTerrain = terrain;
			background = new TextureRegionDrawable[terrain.length];
			for (int i = 0; i < terrain.length; i++) {
				TextureRegion textureRegion = new TextureRegion(GameManager.get().getManager(TextureManager.class)
						.getTextureRegion(terrain[i].getTexture()));
				textureRegion.setRegionHeight(textureRegion.getRegionHeight() * WorldManager.WORLD_SIZE * 2);
				textureRegion.setRegionWidth(textureRegion.getRegionWidth() * WorldManager.WORLD_SIZE * 2);
				textureRegion.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
				background[i] = new TextureRegionDrawable(textureRegion);
			}
		} catch (NullPointerException e) {
			background = new TextureRegionDrawable[]{new TextureRegionDrawable()};
			LOGGER.info(e.getMessage());
		}
	}

	public WorldType getWorldType() {
		return worldType;
	}
}
