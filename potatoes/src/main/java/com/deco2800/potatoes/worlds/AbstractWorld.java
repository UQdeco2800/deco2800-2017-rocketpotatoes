package com.deco2800.potatoes.worlds;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.deco2800.potatoes.entities.AbstractEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AbstractWorld is the Game AbstractWorld
 *
 * It provides storage for the WorldEntities and other universal world level items.
 */
public abstract class AbstractWorld {

    private Map<Integer, AbstractEntity> entities = new HashMap<>();
    // Current index of the hashmap i.e. the last value we inserted into, for significantly more efficient insertion)
    private int currentIndex;
    protected TiledMap map;
    private int width;
    private int length;

    /**
     * Returns a list of entities in this world
     * @return All Entities in the world
     */
    public Map<Integer, AbstractEntity> getEntities() {
        return new HashMap<>(this.entities);
    }

    /**
     * Returns the current map for this world
     * @return Map object for this world
     */
    public TiledMap getMap() {
        return this.map;
    }


    public void addEntity(AbstractEntity entity) {
        // HashMap because I want entities to have unique ids that aren't necessarily sequential
        // O(n) insertion? Sorry this is pretty hacky :(
        while (true) {
            if (entities.containsKey(currentIndex)) {
                currentIndex++;
            }
            else {
                System.out.println("Putting " + entity + " in " + currentIndex);
                entities.put(currentIndex, entity);
                currentIndex++;
                break;
            }
        }

    }

    /**
     * Add's an entity at the id (i.e. the index of the ArrayList). This will overwrite any existing entity
     * if it is occupied.
     * @param entity
     * @param id
     */
    public void addEntity(AbstractEntity entity, int id) {
        entities.put(id, entity);
    }

    public void removeEntity(AbstractEntity entity) {
        entities.remove(entity);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }
}
