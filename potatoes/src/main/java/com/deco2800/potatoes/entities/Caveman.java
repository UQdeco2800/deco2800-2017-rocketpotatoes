package com.deco2800.potatoes.entities;

public class Caveman extends Player {
	
	/**
     * Creates a new Caveman instance.
     *
     * @param posX The x-coordinate.
     * @param posY The y-coordinate.
     * @param posZ The z-coordinate.
	 * @return 
     */
    public Caveman(float posX, float posY, float posZ) {
    		super(posX, posY, posZ);
    }
    
    
    @Override
    public void updateSprites() {
    		super.updateSprites();
    		switch (this.getState()) {
			case idle:
				
				break;
			case walk:
				
				break;
			case attack:
	
				break;
			case damaged:
	
				break;
			default:
				break;
			}
    }
	
}
