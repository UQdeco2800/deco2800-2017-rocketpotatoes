package com.deco2800.potatoes.entities.trees;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import com.deco2800.potatoes.entities.StatisticsBuilder;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.animation.Animation;
import com.deco2800.potatoes.entities.animation.AnimationFactory;

/**
 * A tree that shoots projectiles using the ProjectileShootEvent
 * 
 * @see AbstractTree
 */
public class ProjectileTree extends AbstractTree implements Tickable {
	private static final transient String[] GROW_ANIMATION = createGrowList();
	private static final transient List<TreeStatistics> STATS = initStats();
	private int reloadTime;
	private int range;
	private float maxHealth;

	/**
	 * Default constructor for serialization
	 */
	public ProjectileTree() {
		// default method
		reloadTime = 10;
		range = 10;
		maxHealth = 10;
	}

	private static String[] createGrowList() {
		String[] result = new String[7];
		for (int i = 1; i < 8; i++) {
			result[i - 1] = "basictree_grow" + i;
		}
		return result;
	}

	/**
	 * Constructor for the base
	 *
	 * @param world
	 *            The world of the tower.
	 * @param posX
	 *            The x-coordinate.
	 * @param posY
	 *            The y-coordinate.
	 * @param posZ
	 *            The z-coordinate.
	 * @param reloadTime
	 * @param range
	 * @param maxHealth
	 *            The initial maximum health of the tower
	 */

	public ProjectileTree(float posX, float posY, float posZ, int reloadTime, float range, float maxHealth) {
		super(posX, posY, posZ, 1f, 1f, 1f);
		this.reloadTime = reloadTime;
		this.maxHealth = maxHealth;
	}

	/**
	 * Creates a basic projectile tree at the given position with a 1 second shoot
	 * time, 8 range and 100 health
	 */
	public ProjectileTree(float posX, float posY, float posZ) {
		this(posX, posY, posZ, 1000, 8f, 100f);
		this.setStaticCollideable(true);
	}

	/**
	 * Creates a copy of this tree as it was when it was just created
	 */
	public ProjectileTree clone() {
		return new ProjectileTree(this.getPosX(), this.getPosY(), this.getPosZ(), this.reloadTime, this.range,
				this.maxHealth);
	}

	@Override
	public List<TreeStatistics> getAllUpgradeStats() {
		return STATS;
	}

	private static List<TreeStatistics> initStats() {
		List<TreeStatistics> result = new LinkedList<>();

		// This isn't very nice maybe change how animations are created
		Function<AbstractTree, Animation> growAnimation = x -> AnimationFactory.createSimpleStateAnimation(100, 0,
				GROW_ANIMATION, () -> (float) x.getConstructionLeft());

		result.add(new StatisticsBuilder<AbstractTree>().setHealth(10).setAttackRange(8f).setBuildTime(5000)
				.setBuildCost(1).setAnimation(growAnimation).addEvent(new TreeProjectileShootEvent(3000))
				.createTreeStatistics());
		result.add(new StatisticsBuilder<AbstractTree>().setHealth(20).setAttackRange(8f).setBuildTime(2000)
				.setBuildCost(1).setAnimation(growAnimation).addEvent(new TreeProjectileShootEvent(2500))
				.createTreeStatistics());
		result.add(new StatisticsBuilder<AbstractTree>().setHealth(30).setAttackRange(8f).setBuildTime(2000)
				.setBuildCost(1).setAnimation(growAnimation).addEvent(new TreeProjectileShootEvent(1500))
				.createTreeStatistics());

		return result;
	}
}
