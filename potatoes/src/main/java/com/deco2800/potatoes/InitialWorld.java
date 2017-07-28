package com.deco2800.potatoes;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.deco2800.moos.renderers.Renderable;
import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.potatoes.entities.Base;
import com.deco2800.potatoes.entities.Peon;
import com.deco2800.potatoes.entities.Selectable;

public class InitialWorld extends AbstractWorld {

	/**
	 * Constructor for InitialWorld
	 */
	public InitialWorld() {
 		/* Load up the map for this world */
		this.map = new TmxMapLoader().load("resources/placeholderassets/placeholder.tmx");

        /* Grab the width and length values from the map file to use as the world size */
		this.setWidth(this.getMap().getProperties().get("width", Integer.class));
		this.setLength(this.getMap().getProperties().get("height", Integer.class));

		this.addEntity(new Peon(this, 0, 0, 0));
		this.addEntity(new Base(this, 8, 8, 0));
	}

	public void deSelectAll() {
		for (Renderable r : this.getEntities()) {
			if (r instanceof Selectable) {
				((Selectable) r).deselect();
			}
		}
	}
}
