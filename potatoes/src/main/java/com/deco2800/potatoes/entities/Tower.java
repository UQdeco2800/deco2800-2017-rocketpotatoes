package com.deco2800.potatoes.entities;

import java.util.Optional;

import com.badlogic.gdx.utils.TimeUtils;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.WorldUtil;

/**
 * Tower that can do things.
 * @author leggy
 *
 */
public class Tower extends AbstractEntity implements Tickable {
	
	private final static String TEXTURE = "tower";
	
	private int reloadTime;
	private long lastFireTime;
	
	private float range = 8f;
	
	private Optional<AbstractEntity> target = Optional.empty();


	public Tower() { }

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
		super(posX, posY, posZ, 1, 1, 1, TEXTURE);
		
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
		if(!(lastFireTime + reloadTime < time)) {
			return;
		}
		
		lastFireTime = time;
		this.target = WorldUtil.getClosestEntityOfClass(Squirrel.class, getPosX(), getPosY());
		
		if(!target.isPresent()) {
			return;
		}
		System.out.println("FiRiNg Mi LaZoRs " + i);

		GameManager.get().getWorld().addEntity(new BallisticProjectile(getPosX(), getPosY(), getPosZ(), target.get().getPosX(), target.get().getPosY(), getPosZ(), range));
	

	}


	
	@Override
	public String toString() {
		return String.format("Tower at (%d, %d)", (int)getPosX(), (int)getPosY());
	}
}
