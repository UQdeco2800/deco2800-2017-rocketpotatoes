package com.deco2800.potatoes.entities;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.moos.entities.Tickable;
import com.deco2800.moos.util.Box3D;
import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.worlds.WorldEntity;

public class Player extends WorldEntity implements Tickable{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Player.class);

	public boolean movingUp;
	public boolean movingDown;
	public boolean movingRight;
	public boolean movingLeft;
	
	private float speed;
	
	public Player(AbstractWorld parent, float posX, float posY, float posZ) {
		super(parent, posX, posY, posZ, 1, 1);
		this.speed = 0.2f;
		
		this.movingUp = false;
		this.movingDown = false;
		this.movingRight = false;
		this.movingLeft = false;
		
		this.setTexture("selected_black");
	}

	@Override
	public void onTick(int arg0) {
		float newPosX = this.position.getX();
		float newPosY = this.position.getY();
		
		if(movingUp){
			newPosX += speed;
			newPosY -= speed;
		} else if(movingDown){
			newPosX -= speed;
			newPosY += speed;
		}
		
		if(movingLeft){
			newPosX -= speed;
			newPosY -= speed;
		} else if(movingRight){
			newPosX += speed;
			newPosY += speed;
		}
		
		/*this.position.setX(newPosX);
		this.position.setY(newPosY);*/
		
		Box3D newPos = getBox3D();
		newPos.setX(newPosX);
		newPos.setY(newPosY);
		
		

		List<WorldEntity> entities = this.getParent().getEntities();
		boolean collided = false;
		for(WorldEntity entity : entities) {
			if(!this.equals(entity) && newPos.overlaps(entity.getBox3D())) {
				LOGGER.info(this + " colliding with " + entity);
				System.out.println(this + " colliding with " + entity);
				collided = true;
				
				/*movingUp = false;
				movingDown = false;
				movingRight = false;
				movingLeft = false;*/

			}
		}
		
		if(!collided) {
			this.position.setX(newPosX);
			this.position.setY(newPosY);			
		}
		
		
	}
	
	
	public String toString() {
		return "The player";
	}

}
