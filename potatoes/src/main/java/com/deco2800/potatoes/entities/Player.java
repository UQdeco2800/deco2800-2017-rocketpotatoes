package com.deco2800.potatoes.entities;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.moos.DesktopLauncher;
import com.deco2800.moos.entities.Tickable;
import com.deco2800.moos.renderers.Renderable;
import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.worlds.WorldEntity;
import com.deco2800.potatoes.inventory.Inventory;

public class Player extends WorldEntity implements Tickable{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Player.class);

	public boolean movingUp;
	public boolean movingDown;
	public boolean movingRight;
	public boolean movingLeft;
	
	private float speed;
	
	private Inventory inventory;

	public Player(AbstractWorld parent, float posX, float posY, float posZ) {
		super(parent, posX, posY, posZ, 1, 1);
		this.speed = 0.2f;
		
		this.movingUp = false;
		this.movingDown = false;
		this.movingRight = false;
		this.movingLeft = false;
		
		this.inventory = new Inventory();
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
		
		this.position.setX(newPosX);
		this.position.setY(newPosY);

		List<WorldEntity> entities = this.getParent().getEntities();
		
		for(WorldEntity entity : entities) {
			if(this.collidesWith(entity) && !this.equals(entity)) {
				LOGGER.info(this + " colliding with " + entity);
				System.out.println(this + " colliding with " + entity);

			}
		}
		
	}
	
	
	public String toString() {
		return "The player";
	}

}
