package com.deco2800.potatoes.entities;

import com.deco2800.moos.entities.Tickable;
import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.worlds.WorldEntity;

public class Player extends WorldEntity implements Tickable{
	
	
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
	}

	@Override
	public void onTick(int arg0) {
		if(movingUp){
			this.posX += speed;
			this.posY -= speed;
		} else if(movingDown){
			this.posX -= speed;
			this.posY += speed;
		}
		
		if(movingLeft){
			this.posX -= speed;
			this.posY -= speed;
		} else if(movingRight){
			this.posX += speed;
			this.posY += speed;
		}
		
	}

}
