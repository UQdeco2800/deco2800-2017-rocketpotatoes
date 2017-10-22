package com.deco2800.potatoes.entities.enemies.enemyactions;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.enemies.MeleeAttack;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.WorldUtil;

import java.util.Optional;

/**
 * A melee attack from enemy to a target
 *
 **/
public class MeleeAttackEvent extends TimeEvent<EnemyEntity> {

	private float range = 1.5f;
	private Class target;

	/**
	 * Default constructor for serialization
	 */
	public MeleeAttackEvent() {
		// Empty for serialzation purposes
	}

	/**
	 * Constructor for melee attack event, set up to repeat an attack according to
	 * attackSpeed
	 *
	 * @param attackSpeed
	 *            the delay between attacks
	 *
	 */
	public MeleeAttackEvent(int attackSpeed, Class target) {
		setDoReset(true);
		setResetAmount(attackSpeed);
		this.target = target;
		reset();
	}

	/**
	 * Creates action as per TimeEvent in which the enemy this event belongs to attacks an
	 * entity of the target class provided if within melee range.
	 *
	 * @param enemy
	 *            The enemy that this melee attack belongs to
	 */
	@Override
	public void action(EnemyEntity enemy) {
		Optional<AbstractEntity> foundTarget = WorldUtil.getClosestEntityOfClass(target, enemy.getPosX(), enemy.getPosY());

		// no target exists or target is out of range
		if (!foundTarget.isPresent() || enemy.distanceTo(foundTarget.get()) > range) {
			return;
		}

		GameManager.get().getWorld()
				.addEntity(new MeleeAttack(target,
						new Vector3(enemy.getPosX() + 0.5f, enemy.getPosY() + 0.5f, enemy.getPosZ()),
						new Vector3(foundTarget.get().getPosX(), foundTarget.get().getPosY(), foundTarget.get().getPosZ()), 1, 4));

	}

	/**
	 * @return a copy of this MeleeAttackEvent
	 */
	@Override
	public TimeEvent<EnemyEntity> copy() {
		return new MeleeAttackEvent(getResetAmount(), this.target);
	}

	/**
	 * @return string representation of melee attack
	 */
	@Override
	public String toString() {
		return String.format("Melee attack with %d attackspeed", this.getResetAmount());
	}
}
