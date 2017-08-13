package com.deco2800.potatoes.worlds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.MultiplayerManager;

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
    // First 16 index's are reserved for clients
    // TODO game will likely crash/break badly when this overflows
    private int currentIndex = 17;
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


    /**
     * Add's an entity into the next available id slot.
     *
     * In singleplayer this should work as you expect.
     *
     * For multiplayer, this should only be called by the master client. TODO way to for perform client side prediction.
     * @param entity
     */
    public void addEntity(AbstractEntity entity) {
        MultiplayerManager m = (MultiplayerManager) GameManager.get().getManager(MultiplayerManager.class);
        if (m.isMultiplayer()) {
            if (m.isMaster()) {
                // HashMap because I want entities to have unique ids that aren't necessarily sequential
                // O(n) insertion? Sorry this is pretty hacky :(
                while (true) {
                    if (entities.containsKey(currentIndex)) {
                        currentIndex++;
                    } else {
                        // If we're in multiplayer and the master tell other entities.
                        entities.put(currentIndex++, entity);

                        // Tell other clients about this entity. Note that we should always broadcast master changes AFTER
                        // they have actually been made. Since the server will often read the master state for information.
                        m.broadcastNewEntity(currentIndex - 1);
                        break;
                    }
                }
            }
            else {
                throw new IllegalStateException(
                        "Clients who aren't master shouldn't be adding entities when in multiplayer!");
            }
        }
        else {
            // Singleplayer behaviour
            entities.put(currentIndex++, entity);
        }
    }

    /**
     * Add's an entity at the id (i.e. the index of the ArrayList). This will overwrite any existing entity
     * if it is occupied.
     *
     * This function should probably only be called for multiplayer purposes, where entity id's must sync across
     * clients. As such this will throw an error if you're not in multiplayer
     * @param entity
     * @param id
     */
    public void addEntity(AbstractEntity entity, int id) {
        MultiplayerManager m = (MultiplayerManager) GameManager.get().getManager(MultiplayerManager.class);
        if (m.isMultiplayer()) {
            entities.put(id, entity);
        }
        else {
            throw new IllegalStateException("Not in multiplayer, this function should only be used for multiplayer");
        }

    }

    public void removeEntity(int id) {
        entities.remove(id);

        // Tell the other clients if we're master and in multiplayer.
        MultiplayerManager m = (MultiplayerManager) GameManager.get().getManager(MultiplayerManager.class);
        if (m.isMultiplayer() && m.isMaster()) {
            m.broadcastEntityDestroy(id);
        }
    }

    public void removeEntity(AbstractEntity entity) {
        for (Map.Entry<Integer, AbstractEntity> e : entities.entrySet()) {
            if (e.getValue() == entity) {
                entities.remove(e.getKey());

                // Tell the other clients if we're master and in multiplayer.
                MultiplayerManager m = (MultiplayerManager) GameManager.get().getManager(MultiplayerManager.class);
                if (m.isMultiplayer() && m.isMaster()) {
                    m.broadcastEntityDestroy(e.getKey());
                }
                return;
            }
        }


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
