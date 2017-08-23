package com.deco2800.potatoes.entities;

import java.util.Map;

import org.apache.derby.tools.sysinfo;

import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.entities.AbstractEntity;

public class ResourceEntity extends AbstractEntity implements Tickable{
	
	private Resource resourceType;
	private transient final float change = (float)0.2;
	private transient final float[][] positions = {{change, 0}, {change, change},
			{0, change}, {-change, change}, {-change, 0}, {-change, -change},
			{0, -change}, {-change, -change}};
	
	
	public ResourceEntity() {
		super();
	}
	
	public ResourceEntity(float posX, float posY, float posZ, Resource resource) {
		super(posX, posY, posZ, 1f, 1f, 1f, 1f, 1f, resource.getType());
		resourceType = resource;
	}
	
	
	@Override
	public void onTick(long time) {
		float xPos = getPosX();
		float yPos = getPosY();
		boolean collided = false;
		Player player = null;
		
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
						player = (Player) entity;
					}
					
				}
				
			}
				
		}
		
		if(collided == true) {
			GameManager.get().getWorld().removeEntity(this);
			try {
				player.getInventory().updateQuantity(this.resourceType, 1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
			
	}
	
	
	public Resource getType() {
		return resourceType;
	}
	
	public void setResourceType(Resource resource) {
		resourceType = resource;
	}

	
	/**
	 * <p>
	 * Returns the string representation of the resource.
	 * </p>
	 * 
	 * @return string
	 * 		The string representation of the resource.
	 */
	public String toString() {
		return resourceType.toString();
	}
	
	/*@Override
    public boolean equals(Object object) {
        if (!(object instanceof ResourceEntity)) {
            return false;
        }
        ResourceEntity other = (ResourceEntity) object; // the corridor to compare
        return other.getName().equals(name) &&
        		other.getType().equals(resourceType);
    }

    @Override
    public int hashCode() {
        // We create a polynomial hash-code based on the object string and type.
        final int prime = 31; // an odd base prime
        int result = 1; // the hash code under construction
        result = prime * result + name.hashCode();
        result = prime * result + resourceType.hashCode();
        return result;
    }
    
    
    @Override
    public int compareTo(ResourceEntity other) {
        int result = resourceType.compareTo(other.getType());
        if (result == 0) {
        	return name.compareTo(other.getName());
        }
        return result;
    }*/
    

}
