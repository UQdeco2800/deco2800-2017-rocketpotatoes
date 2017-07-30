package com.deco2800.potatoes;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.deco2800.moos.renderers.Renderable;
import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.potatoes.entities.Tower;
import com.deco2800.potatoes.entities.Peon;
import com.deco2800.potatoes.entities.Selectable;

/**
 * Initial world using preset world file.
 * 
 * @author leggy
 *
 */
public class InitialWorld extends AbstractWorld {

	/**
	 * Constructor for InitialWorld
	 */
	public InitialWorld() {
		/* Load up the map for this world */
		this.map = new TmxMapLoader().load("resources/placeholderassets/placeholder.tmx");

		/*
		 * Grab the width and length values from the map file to use as the world size
		 */
		this.setWidth(this.getMap().getProperties().get("width", Integer.class));
		this.setLength(this.getMap().getProperties().get("height", Integer.class));

		this.addEntity(new Peon(0, 0, 0));
		this.addEntity(new Tower(8, 8, 0));
	}

	/**
	 * Deselects all entities.
	 */
	public void deSelectAll() {
		for (Renderable r : this.getEntities()) {
			if (r instanceof Selectable) {
				((Selectable) r).deselect();
			}
		}
	}
}
