package com.deco2800.potatoes.entities.player;
import com.deco2800.potatoes.entities.TimeEvent;

/**
 * An time event that increments the resource count of a resource tree.
 * 
 * @author petercondoleon
 */
public class AttackCooldownEvent extends TimeEvent<Player> {
	
	/**
	 * Default constructor for serialization
	 */
	public AttackCooldownEvent() {
		//Blank comment for Sonar
	}
	
	/**
	 * Initialises with a gather rate and amount.
	 * 
	 * @param cooldownTime
	 *            the time taken to cool down attack
	 */
	public AttackCooldownEvent(int cooldownTime) {
		setDoReset(true);
		setResetAmount(cooldownTime);
		reset();
	}
	
	/**
	 * Action for incrementing resources in the resource tree. 
	 */
	@Override
	public void action(Player player) {
		player.canAttack = true;
		setDoReset(false);
	}
	
	@Override
	public TimeEvent<Player> copy() {
		return new AttackCooldownEvent(getResetAmount());
	}
}