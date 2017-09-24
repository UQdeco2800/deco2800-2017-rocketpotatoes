package com.deco2800.potatoes.entities.enemies;

import java.util.LinkedList;
import java.util.List;

import com.deco2800.potatoes.collisions.CollisionMask;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.health.HasProgress;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PathManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.util.Path;

/**
 * A generic player instance for the game
 */
public class Squirrel extends EnemyEntity implements Tickable, HasProgress {

	private static final transient String TEXTURE_LEFT = "squirrel";
	private static final transient String TEXTURE_RIGHT = "squirrel_right";
	private static final transient float HEALTH = 100f;
	private static final BasicStats STATS = initStats();

	private static float speed = 0.04f;
	private static Class<?> goal = Player.class;
	private Path path = null;
	private CollisionMask target = null;

	private static final ProgressBarEntity progressBar = new ProgressBarEntity();
	
	public Squirrel() {
		this(0, 0);
	}

	public Squirrel(float posX, float posY) {
        super(new Circle2D(posX, posY, 0.665f), 0.60f, 0.60f, TEXTURE_LEFT, HEALTH, speed, goal);
		this.speed = speed;
		this.goal = goal;
		this.path = null;
	}
	


	/**
	 * Squirrel follows it's path.
	 * Requests a new path whenever it collides with a staticCollideable entity
	 * moves directly towards the player once it reaches the end of it's path
	 * @param i
	 */
	@Override
	public void onTick(long i) {
		PlayerManager playerManager = GameManager.get().getManager(PlayerManager.class);
		PathManager pathManager = GameManager.get().getManager(PathManager.class);

        // check paths

		//check collision
		for (AbstractEntity entity : GameManager.get().getWorld().getEntities().values()) {
			if (entity.isStaticCollideable() && this.getMask().overlaps(entity.getMask())) {
				//collided with wall
                path = pathManager.generatePath(this.getMask(), playerManager.getPlayer().getMask());
				target = path.pop();
				break;
			}
		}

        // check that we actually have a path
        if (path == null || path.isEmpty()) {
            path = pathManager.generatePath(this.getMask(), playerManager.getPlayer().getMask());
        }


		//check if close enough to target
		if (target != null && target.overlaps(this.getMask())) {
			target = null;
		}

		//check if the path has another node
		if (target == null && !path.isEmpty()) {
			target = path.pop();
		}

		float targetX;
		float targetY;


		if (target == null) {
            target = playerManager.getPlayer().getMask();
		} 

        targetX = target.getX();
        targetY = target.getY();

		float deltaX = getPosX() - targetX;
		float deltaY = getPosY() - targetY;

		float angle = (float)(Math.atan2(deltaY, deltaX)) + (float)(Math.PI);

		//flip sprite
		if (deltaX + deltaY >= 0) {
			this.setTexture(TEXTURE_LEFT);
		} else {
			this.setTexture(TEXTURE_RIGHT);
		}

		float changeX = (float)(speed * Math.cos(angle));
		float changeY = (float)(speed * Math.sin(angle));

		this.setPosX(getPosX() + changeX);
		this.setPosY(getPosY() + changeY);
	}


	@Override
	public String toString() {
		return String.format("Squirrel at (%d, %d)", (int) getPosX(), (int) getPosY());
	}

	@Override
	public ProgressBarEntity getProgressBar() {
		return progressBar;
	}

	private static BasicStats initStats() {
		List<TimeEvent<EnemyEntity>> normalEvents = new LinkedList<>();
		BasicStats result = new BasicStats(HEALTH, speed, 8f, 500, normalEvents, TEXTURE_LEFT);
		result.getNormalEventsReference().add(new MeleeAttackEvent(result.getAttackSpeed(), GoalPotate.class));
		return result;
	}

	@Override
	public BasicStats getBasicStats() {
		return STATS;
	}

    }
