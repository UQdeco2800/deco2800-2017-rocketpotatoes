package com.deco2800.potatoes.entities;

import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.util.Box3D;

/**
 * A generic player instance for the game
 */
public class Squirrel extends EnemyEntity implements Tickable, HasProgress, ProgressBar{
	

	private static final transient String TEXTURE_LEFT = "squirrel";
	private static final transient String TEXTURE_RIGHT = "squirrel2";
	private static final transient float HEALTH = 100f;
	private transient Random random = new Random();
	private static Class goal = Player.class;
	private static float speed = 0.1f;

	public Squirrel() {
		super(0, 0, 0, 1f, 1f, 1f, 1f, 1f, TEXTURE_LEFT, HEALTH, speed, goal);
	}

	public Squirrel(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1f, 1f, 1f, 1f, 1f, TEXTURE_LEFT, HEALTH, speed, goal);
		//this.setTexture("squirrel");
		//this.random = new Random();
	}

	

//	Not working **********
//	public void squirrelAttack() {
//		PlayerManager playerManager = (PlayerManager) GameManager.get().getManager(PlayerManager.class);
//		if (this.distance(playerManager.getPlayer())< 5f) {
//			playerManager.getPlayer().damage(10);
//		}
//	}
	
	@Override
	public String toString() {
		return String.format("Squirrel at (%d, %d)", (int) getPosX(), (int) getPosY());
	}

	@Override
	public void setProgressBar(AbstractEntity entity, Texture progressBar, SpriteBatch batch, int xLength, int yLength) {
		if (health > 60) {
			batch.setColor(Color.GREEN);
		} else if (health > 20) {
			batch.setColor(Color.ORANGE);
		} else {
			batch.setColor(Color.RED);
		}

		batch.draw(progressBar, xLength, yLength, health/3, 5);
		batch.setColor(Color.WHITE);
		
	}

}
