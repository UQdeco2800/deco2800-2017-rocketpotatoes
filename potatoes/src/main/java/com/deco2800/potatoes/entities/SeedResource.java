package com.deco2800.potatoes.entities;

import java.util.Map;

import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;

public class SeedResource extends Resource implements Tickable {
	
	private static final transient String TEXTURE = "seed";
	private final float change = (float)0.2;
	private final float[][] positions = {{change, 0}, {change, change},
			{0, change}, {-change, change}, {-change, 0}, {-change, -change},
			{0, -change}, {-change, -change}};
	
	/**
	 * <p>
	 * Creates a new instance of the class and assigns the name of 
	 * the resource.
	 * </p>
	 * 
	 * <p>
	 * Only to be used when the instance of the resource isn't appearing
	 * on the map.
	 * </p>
	 */
	public SeedResource(String name) {
		super(name);
		this.resourceType = "seed";
	}
	
	public SeedResource(String name, float posX, float posY, float posZ) {
		super(name, posX, posY, posZ, TEXTURE);
		this.resourceType = "seed";
	}

	@Override
	public void onTick(long time) {
		float xPos = getPosX();
		float yPos = getPosY();
		boolean collided = false;
		
		Box3D newPos = getBox3D();
		newPos.setX(xPos);
		newPos.setY(yPos);
		
		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
		for (AbstractEntity entity : entities.values()) {
			if (entity instanceof Player) {
				for (int i = 0; i < 8; i ++) {
					newPos.setX(xPos + positions[i][0]);
					newPos.setY(yPos + positions[i][1]);
					
					if (newPos.overlaps(entity.getBox3D())) {
						collided = true;
					}
				}
				
			}
				
		}
		
		if(collided) {
			GameManager.get().getWorld().removeEntity(this);
			//add to inventory
		}
			
	}
		
}
