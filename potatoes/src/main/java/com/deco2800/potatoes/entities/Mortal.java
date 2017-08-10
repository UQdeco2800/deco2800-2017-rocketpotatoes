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
	 * @param amount - the amount of health to subtract
	 * @return true iff health <= 0
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
