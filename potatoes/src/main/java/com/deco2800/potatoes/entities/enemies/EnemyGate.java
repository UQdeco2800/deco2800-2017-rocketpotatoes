package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.collisions.Box2D;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.entities.health.HasProgressBar;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.health.ProgressBar;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.worlds.terrain.Terrain;
import com.deco2800.potatoes.worlds.terrain.TerrainType;

/**
 * A gate where enemies come out.
 *
 */
public class EnemyGate extends MortalEntity implements HasProgressBar {
	//private static final transient String TEXTURE = "enemyCave_SE";

	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity("healthbar", 2);
	private static final String GRASS = "grass_tile_1";

//	private float posX;
//	private float posY;
	
	/**
	 * Empty constructor for serialization
	 */
	public EnemyGate() {
		// empty for serialization
	}

	/**
	 * Create an enemy gate at coordinates on the map
	 *
	 * @param posX x coordinate to place gate
	 * @param posY y coordinate to place gate
	 */
	public EnemyGate(float posX, float posY, String texture) {
        super(new Circle2D(posX, posY, 1.3f), 1.6f, 1.6f, texture, 1000f);
        this.setSolid( false );
        this.setStatic( true );
        clearPath(posX, posY);
	}

	/**
	 * Clear path from enemy gate to middle of map
	 */
	public void clearPath(float posX, float posY) {
//		float posX = this.getPosX();
//		float posY = this.getPosY();
		float mapMiddleX = GameManager.get().getWorld().getLength()/2;
		float mapMiddleY = GameManager.get().getWorld().getWidth()/2;
		if (posX <= mapMiddleX){
			if (posY <= mapMiddleY) {
				for(int x = (int)posX-2; x <= mapMiddleX; x++) {
					for (int y = (int)posY-2;y <= mapMiddleY; y++) {
						GameManager.get().getWorld().setTile(x, y, new Terrain(GRASS, 1, true));
					}
				}
			} else {
				for(int x = (int)posX-2; x <= mapMiddleX; x++) {
					for (int y = (int)mapMiddleY; y <= posY+2; y++) {
						GameManager.get().getWorld().setTile(x, y, new Terrain(GRASS, 1, true));
						}
				}	
			}
		} else {
			if (posY <= mapMiddleY) {
				for(int x = (int)mapMiddleX; x <= posX+2; x++) {
					for (int y = (int)posY-2;y <= mapMiddleY; y++) {
						GameManager.get().getWorld().setTile(x, y, new Terrain(GRASS, 1, true));
					}
				}
			} else {
				for(int x = (int)mapMiddleX; x <= posX+2; x++) {
					for (int y = (int)mapMiddleY; y <= posY+2; y++) {
						GameManager.get().getWorld().setTile(x, y, new Terrain(GRASS, 1, true));
						}
				}	
			}	
		}	
	}
	
	@Override
	public ProgressBar getProgressBar() {
		return PROGRESS_BAR;
	}

	@Override
	public String toString() {
		return "The Enemy Gate";
	}

}
