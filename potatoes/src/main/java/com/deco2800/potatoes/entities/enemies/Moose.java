package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.GoalPotate;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.StatisticsBuilder;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.health.HasProgress;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PathManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.Path;

/**
 * A generic player instance for the game
 */
public class Moose extends EnemyEntity implements Tickable, HasProgress {

	private static final transient String TEXTURE_LEFT = "pronograde"; // TODO: MAKE MOOSE TEXTURE
	private static final transient String TEXTURE_RIGHT = "pronograde";
	private static final transient float HEALTH = 100f;
	private static final transient float ATTACK_RANGE = 0.5f;
	private static final transient int ATTACK_SPEED = 1000;
	private static final EnemyStatistics STATS = initStats();

	private static float speed = 0.04f;
	private static Class<?> goal = Player.class;
	private Path path = null;
	private Box3D target = null;


	private int ticksSinceRandom = 0;
	private static final int MAX_WAIT = 200;

	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity();

	public Moose() {
		super(0, 0, 0, 0.60f, 0.60f, 0.60f, 1f, 1f, TEXTURE_LEFT, HEALTH, speed, goal);
		this.speed = speed;
		this.goal = goal;
		this.path = null;
	}

	public Moose(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 0.60f, 0.60f, 0.60f, 1f, 1f, TEXTURE_LEFT, HEALTH, speed, goal);
		this.speed = speed;
		this.goal = goal;
		this.path = null;
	}

    /**
     * Change the target for the moose to a random location
     */
    private void randomTarget() {
        float x = (float) Math.random() * GameManager.get().getWorld().getLength();
        float y = (float) Math.random() * GameManager.get().getWorld().getWidth();
        target = new Box3D(x, y, 0, 1f, 1f, 1f);
    }

	/**
	 * Moose follows it's path.
	 * Requests a new path whenever it collides with a staticCollideable entity
	 * moves directly towards the player once it reaches the end of it's path
	 * @param i
	 */
	@Override
	public void onTick(long i) {
		PlayerManager playerManager = GameManager.get().getManager(PlayerManager.class);
		PathManager pathManager = GameManager.get().getManager(PathManager.class);
        boolean changeLocation = false;
		if (++ticksSinceRandom == MAX_WAIT || target == null) {
		    ticksSinceRandom = 0;
		    changeLocation = true;
		    randomTarget();
        }
        // check paths

		//check collision
		for (AbstractEntity entity : GameManager.get().getWorld().getEntities().values()) {
			if (entity.isStaticCollideable() && this.getBox3D().overlaps(entity.getBox3D())) {
				//collided with wall
                randomTarget();
				break;
			}
		}

        // check that we actually have a path
        if (path == null || path.isEmpty()) {
            path = pathManager.generatePath(this.getBox3D(), target);
        }


		//check if close enough to target
		if (target != null && playerManager.getPlayer().getBox3D().overlaps(this.getBox3D())) {
			target = null;
		}

		//check if the path has another node
		if (target == null && !path.isEmpty()) {
			target = path.pop();
		}

		float targetX;
		float targetY;


		if (target == null) {
            target = playerManager.getPlayer().getBox3D();
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
		return PROGRESS_BAR;
	}

	private static EnemyStatistics initStats() {
		EnemyStatistics result = new StatisticsBuilder<>().setHealth(HEALTH).setSpeed(speed)
				.setAttackRange(ATTACK_RANGE).setAttackSpeed(ATTACK_SPEED).setTexture(TEXTURE_LEFT)
				.addEvent(new MeleeAttackEvent(ATTACK_SPEED, GoalPotate.class)).createEnemyStatistics();
		return result;
	}

	@Override
	public EnemyStatistics getBasicStats() {
		return STATS;
	}

    }
