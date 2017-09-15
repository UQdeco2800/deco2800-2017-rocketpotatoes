package com.deco2800.potatoes.worlds;

import java.util.Random;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.deco2800.potatoes.entities.Selectable;
import com.deco2800.potatoes.renderering.Renderable;

/**
 * Initial world using preset world file.
 * 
 * @author leggy
 *
 */
public class InitialWorld extends World {

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
}
