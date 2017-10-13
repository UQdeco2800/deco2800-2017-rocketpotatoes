package com.deco2800.potatoes.entities.trees;

import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.effects.Effect.EffectTexture;
import com.deco2800.potatoes.entities.effects.LightningEffect;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.WorldUtil;

/**
 * Class representing the event for when a lightning tree shoots an enemy. The
 * lightning targets all enemies with range of the tree
 */
public class LightningShootEvent extends TimeEvent<AbstractTree> {
	
	private EffectTexture attackTexture;

	/**
	 * Creates a lightning shoot event with the specified damage and delay
	 */
	public LightningShootEvent(int shootDelay, EffectTexture texture) {
		this.attackTexture = texture;
		
		setDoReset(true);
		setResetAmount(shootDelay);
		reset();
	}

	@Override
	public void action(AbstractTree param) {
		List<AbstractEntity> possibleTargets = WorldUtil
				.getEntitiesOfClass(GameManager.get().getWorld().getEntities().values(), EnemyEntity.class);
		int targetCount = 0;
		for (AbstractEntity target : possibleTargets) {
			// If distance to entity is less than the tree range
			if (WorldUtil.distance(param.getPosX(), param.getPosY(), target.getPosX(), target.getPosY()) <= param
					.getUpgradeStats().getAttackRange() && targetCount <= 4) {
				// Create a lightning effect from the tree to the target
				GameManager.get().getWorld()
						.addEntity(new LightningEffect(target.getClass(),
								new Vector3(param.getPosX(), param.getPosY(), param.getPosZ()),
								new Vector3(target.getPosX(), target.getPosY(), target.getPosZ()),
								0.05f, 1, attackTexture));
				targetCount++;
			}
		}
	}

	@Override
	public TimeEvent<AbstractTree> copy() {
		return new LightningShootEvent(getResetAmount(), attackTexture);
	}
}
