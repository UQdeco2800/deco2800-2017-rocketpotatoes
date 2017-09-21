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
import com.deco2800.potatoes.managers.SoundManager;
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

	private static final float moose_size = 1.5f;

	private static float speed = 0.04f;
	private static Class<?> goal = Player.class;
	private Path path = null;
	private Box3D target = null;


	private int ticksSinceRandom = 0;
	private static final int MAX_WAIT = 200;

	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity();

	/**
	 * Empty constructor for serialization
	 */
	public Moose() {
        // empty for serialization
	}

	public Moose(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 0.60f, 0.60f, 0.60f, moose_size, moose_size, TEXTURE_LEFT, HEALTH, speed, goal);
		this.speed = speed;
		this.goal = goal;
		this.path = null;
		this.damageScaling = 0.8f; // 20% Damage reduction for Moose
	}

    /**
     * Change the target for the moose to a random location
     */
    private void randomTarget() {
        float x = (float) Math.random() * GameManager.get().getWorld().getLength();
        float y = (float) Math.random() * GameManager.get().getWorld().getWidth();
        target = new Box3D(x, y, 0, 1f, 1f, 1f);
    }


	@Override
	public String toString() {
		return String.format("Moose at (%d, %d)", (int) getPosX(), (int) getPosY());
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