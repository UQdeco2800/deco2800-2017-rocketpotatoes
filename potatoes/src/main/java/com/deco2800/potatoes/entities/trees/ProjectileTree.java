package com.deco2800.potatoes.entities.trees;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import com.deco2800.potatoes.entities.StatisticsBuilder;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.animation.Animation;
import com.deco2800.potatoes.entities.animation.AnimationFactory;

public class ProjectileTree extends AbstractTree implements Tickable {
	private static final transient String[] GROW_ANIMATION = createGrowList();
	private static final transient List<TreeStatistics> STATS = initStats();

	/**
	 * Default constructor for serialization
	 */
	public ProjectileTree() {
		// default method
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

	public ProjectileTree(float posX, float posY, float posZ, String texture, int reloadTime, float range,
			float maxHealth) {
		super(posX, posY, posZ, 1f, 1f, 1f, texture);
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
