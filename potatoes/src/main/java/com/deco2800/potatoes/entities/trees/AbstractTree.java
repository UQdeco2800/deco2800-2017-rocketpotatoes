package com.deco2800.potatoes.entities.trees;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.TimeEvent;

public abstract class AbstractTree extends AbstractEntity implements Tickable {
	
	
	
	public AbstractTree(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			String texture) {
		super(posX, posY, posZ, xLength, yLength, zLength, texture);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onTick(long time) {
		// TODO Auto-generated method stub
	}
	
	public void registerTimeEvent(TimeEvent event) {
		// TODO stub
	}
}

public class projectileTree extends AbstractTree{
	public int level;
	public int hp;
	public int speed;
	//public whateveraprojectileis projectile;
	pubic void init{
		level=1;
		hp=1;
		speed=1;
	}
	public void upgrade {
		// add upgrade numbers later
		switch (level) {
			case 1:
				hp = 7;
				break;
			case 2:
				hp = 10;
				break;
		}
		level++;
	}
}
