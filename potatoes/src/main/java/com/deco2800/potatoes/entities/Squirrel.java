package com.deco2800.potatoes.entities;

import java.util.Map;
import java.util.Random;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PathManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.Path;

/**
 * A generic player instance for the game
 */
public class Squirrel extends EnemyEntity implements Tickable, HasProgress {
	
	private static final transient String TEXTURE = "squirrel";
	private static final transient float HEALTH = 100f;
	private transient Random random = new Random();

	private float speed = 0.1f;
	private Path path = null;
	private Box3D target = null;

	private PlayerManager playerManager;
	private SoundManager soundManager;
	private PathManager pathManager;
	


	public Squirrel(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1f, 1f, 1f, 1f, 1f, TEXTURE, HEALTH);
		//this.setTexture("squirrel");
		//this.random = new Random();
		PlayerManager playerManager = (PlayerManager) GameManager.get().getManager(PlayerManager.class);
		SoundManager soundManager = (SoundManager) GameManager.get().getManager(SoundManager.class);
		PathManager pathManager = (PathManager) GameManager.get().getManager(PathManager.class);
		this.path = pathManager.generatePathPlayer();
	}

	@Override
	public void onTick(long i) {
		//in case squirrel is made before playerManager
		if(playerManager == null) {
			playerManager = (PlayerManager) GameManager.get().getManager(PlayerManager.class);
		}

		//check collision
		boolean collided = false;
		for (AbstractEntity entity : GameManager.get().getWorld().getEntities().values()) {
			if (entity.isStaticCollideable() && this.getBox3D().overlaps(entity.getBox3D())) {
				//collided with wall
				path = pathManager.generatePathPlayer(); //TODO squirel is requesting path here
				if (path.isEmpty()) {return;} //stuck in wall no way to get to target
				target = path.pop();
				break;
			}
		}


		//check if close enough to target, or collide with staticCollideable entity
		if (target != null && target.overlaps(this.getBox3D())) {
			target = null;
		}

		//check if the path has another node
		if (target == null && !path.isEmpty()) {
			target = path.pop();
		}

		float targetX;
		float targetY;


		if (target == null) {
			Player player = playerManager.getPlayer();
			targetX = player.getPosX();
			targetY = player.getPosY();
		} else {
			targetX = target.getX();
			targetY = target.getY();
		}

		float deltaX = getPosX() - targetX;
		float deltaY = getPosY() - targetY;

		float angle = (float)(Math.atan2(deltaY, deltaX)) + (float)(Math.PI);

		float changeX = (float)(speed * Math.cos(angle));
		float changeY = (float)(speed * Math.sin(angle));

		this.setPosX(getPosX() + changeX);
		this.setPosY(getPosY() + changeY);

/*

		float goalX = playerManager.getPlayer().getPosX() + random.nextFloat() * 6 - 3;
		float goalY = playerManager.getPlayer().getPosY() + random.nextFloat() * 6 - 3;

		if(this.distance(playerManager.getPlayer()) < speed) {
			this.setPosX(goalX);
			this.setPosY(goalY);
			return;
		}
/*
		float deltaX = getPosX() - goalX;
		float deltaY = getPosY() - goalY;

		float angle = (float)(Math.atan2(deltaY, deltaX)) + (float)(Math.PI);

		float changeX = (float)(speed * Math.cos(angle));
		float changeY = (float)(speed * Math.sin(angle));

		Box3D newPos = getBox3D();
		newPos.setX(getPosX() + changeX);
		newPos.setY(getPosY() + changeY);

		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
		boolean collided = false;
		for (AbstractEntity entity : entities.values()) {
			if (!this.equals(entity) && !(entity instanceof Projectile) && newPos.overlaps(entity.getBox3D()) ) {
				if(entity instanceof Player) {
					//soundManager.playSound("ree1.wav");
				}
				collided = true;
			}
		}

		if (!collided) {
			setPosX(getPosX() + changeX);
			setPosY(getPosY() + changeY);
		}
*/
	}
	
	@Override
	public String toString() {
		return "Squirrel";
	}

}
