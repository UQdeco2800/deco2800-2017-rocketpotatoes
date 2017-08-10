package com.deco2800.potatoes.entities;

/**
 * An interface to handle heatlh, damage taken, healing, and death
 * @author michaelruigrok
 */
public interface Mortal {

	/**
	 * @return the current health
	 */
	float getHealth();

	/**
	 * @return the max health
	 */
	float getMaxHealth();

	/**
	 * @return true iff health <= 0
	 */
	boolean isDead();

	/**
	 * @param amount - the amount of health to subtract
	 * @return iff the resulting damage killed the entity
	 */
	boolean damage(float amount);

	/**
	 * @param amount - the amount of health to refill
	 * @return if health is now maxed out
	 */
	boolean heal(float amount);

	/**
	 * Perform the required death behaviour
	 */ 
	void deathHandler();
}
