package com.deco2800.potatoes.worlds;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tower;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * AbstractWorld is the Game AbstractWorld
 *
 * It provides storage for the WorldEntities and other universal world level items.
 * @author timhadwen
 */
public class ExampleWorld extends World {

    private List<AbstractEntity> entities = new ArrayList<AbstractEntity>();

    /**
     * Constructor for AbstractWorld.
     *
     * Initialize the map etc here
     */
    public ExampleWorld() {
        /* Load up the map for this world */
        this.map = new TmxMapLoader().load("placeholder.tmx");

        /* Grab the width and length values from the map file to use as the world size */
        this.setWidth(this.map.getProperties().get("width", Integer.class));
        this.setLength(this.map.getProperties().get("height", Integer.class));

        /* Create random trees to test the engine */
        for (int i = 0; i < this.getLength(); i++){
            for (int j = 0; j < this.getWidth(); j++) {

                /* Spawn trees with a 10% chance */
                Random r = new Random();
                if (r.nextInt(100) < 100) {
                    this.entities.add(new Tower(i, j, 0));
                }
            }
        }
    }

    /**
     * Returns the current map for this world
     * @return Map object for this world
     */
    @Override
	public TiledMap getMap() {
        return this.map;
    }

}
