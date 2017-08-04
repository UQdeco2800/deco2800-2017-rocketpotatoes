package com.deco2800.potatoes.entities;

import java.util.Optional;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;

/**
 * Tower that can do things.
 * @author leggy
 *
 */
public class Tower extends AbstractEntity implements Tickable {
	
	private int reloadTime;
	private long lastFireTime;
	
	private Optional<AbstractEntity> target = Optional.empty();


	/**
	 * Constructor for the base
	 * 
	 * @param world
	 *            The world of the tower.
	 * @param posX
	 *            The x-coordinate.
	 * @param posY
	 *            The y-coordinate.
	 * @param posZ
	 *            The z-coordinate.
	 */
	public Tower(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1, 1, 1);
		this.setTexture("tower");
		
		this.lastFireTime = 0;
		this.reloadTime = 1000;
	}


	/**
	 * On Tick handler
	 * @param i time since last tick
	 */
	@Override
	public void onTick(long i) {
		long time = TimeUtils.millis();
		if(lastFireTime + reloadTime < time) {
			System.out.println("FiRiNg Mi LaZoRs " + i);
			lastFireTime = time;
		}

	}


	
	@Override
	public String toString() {
		return String.format("Tower at (%d, %d)", (int)getPosX(), (int)getPosY());
	}
}
