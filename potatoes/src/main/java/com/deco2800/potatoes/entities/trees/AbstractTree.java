package com.deco2800.potatoes.entities.trees;

import java.util.List;
import java.util.Arrays;

import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.animation.Animated;
import com.deco2800.potatoes.entities.animation.Animation;
import com.deco2800.potatoes.entities.animation.SingleFrameAnimation;
import com.deco2800.potatoes.entities.health.HasProgress;
import com.deco2800.potatoes.entities.health.HasProgressBar;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.health.ProgressBar;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;
import com.badlogic.gdx.graphics.Color;

/**
 * AbstractTree represents an upgradable tree entity. AbstractTree can have
 * registered normal events which are triggered when the tree is not under
 * construction and construction events which are triggered when the tree is
 * being constructed
 */
public abstract class AbstractTree extends MortalEntity implements Tickable, HasProgress, HasProgressBar, Animated {

	// Maybe move this out
	private static class ConstructionEvent extends TimeEvent<AbstractTree> {
		public ConstructionEvent(int constructionTime) {
			setDoReset(true);
			setResetAmount(constructionTime / 100);
			reset();
		}

		@Override
		public void action(AbstractTree param) {
			param.decrementConstructionLeft();
			if (param.getConstructionLeft() <= 0) {
				// Changes to the normal events since construction is over
				param.setRegisteredEvents(false);
			}
		}

		@Override
		public TimeEvent<AbstractTree> copy() {
			return null;
		}
	}

	private int constructionLeft = 100;
	private int upgradeLevel = 0;
	private transient Animation animation;

	private static final List<Color> COLOURS = Arrays.asList(Color.YELLOW);
	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity("progress_bar", COLOURS, 60, 1);

	/**
	 * Default constructor for serialization
	 */
	public AbstractTree() {
		// Reseting may not be needed
		resetStats();
	}

	public AbstractTree(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			String texture) {
		super(posX, posY, posZ, xLength, yLength, zLength, texture, 1);
		resetStats();
	}

	@Override
	public void onTick(long time) {
		// Nothing here now
	}

	/**
	 * Adds the tree to the world and reduces the player resources by the build cost
	 * if the player has enough resources
	 * 
	 * @param tree
	 * @return
	 */
	public static boolean constructTree(AbstractTree tree) {
		boolean result = tree.getUpgradeStats().removeConstructionResources();
		if (result) {
			GameManager.get().getWorld().addEntity(tree);
		} else {
			GameManager.get().getManager(EventManager.class).unregisterAll(tree);
		}
		return result;
	}

	/**
	 * Sets the registered events to the construction events or the normal events
	 * 
	 * @param construction
	 *            whether to set the events to construction events or not
	 */
	private void setRegisteredEvents(boolean construction) {
		if (construction) {
			List<TimeEvent<AbstractTree>> constructionEvents = getUpgradeStats().getConstructionEventsCopy();
			constructionEvents.add(new ConstructionEvent(getUpgradeStats().getConstructionTime()));
			registerNewEvents(constructionEvents);
		} else {
			registerNewEvents(getUpgradeStats().getNormalEventsCopy());
		}
	}

	/**
	 * Registers the list of events given with the event manager and unregisters all
	 * other events for this object
	 */
	private void registerNewEvents(List<TimeEvent<AbstractTree>> events) {
		EventManager eventManager = GameManager.get().getManager(EventManager.class);
		eventManager.unregisterAll(this);
		for (TimeEvent<AbstractTree> timeEvent : events) {
			eventManager.registerEvent(this, timeEvent);
		}
	}

	/**
	 * @return the percentage of construction left, from 0 to 100
	 */
	public int getConstructionLeft() {
		return constructionLeft;
	}

	/**
	 * @param constructionLeft
	 *            the percentage of construction to set, from 0 to 100
	 */
	public void setConstructionLeft(int constructionLeft) {
		this.constructionLeft = constructionLeft;
		if (constructionLeft > 0) {
			// Construction starts again
			setRegisteredEvents(true);
		}
	}

	public void decrementConstructionLeft() {
		constructionLeft--;
	}

	/**
	 * Upgrades to the next tree level
	 * 
	 * Not yet implemented
	 */
	public void upgrade() {
		if (upgradeLevel + 1 >= getAllUpgradeStats().size()) {
			return; // Ignores upgrade if at max level
		}
		upgradeLevel++;
		resetStats();
	}

	/**
	 * Resets the stats of this tree to the default for the current upgrade level
	 * and restarts tree construction
	 */
	public void resetStats() {
		this.addMaxHealth(getUpgradeStats().getHp() - this.getMaxHealth());
		this.heal(getMaxHealth());
		setTexture(getUpgradeStats().getTexture());
		setAnimation(new SingleFrameAnimation(getUpgradeStats().getTexture()));
		setRegisteredEvents(true);
	}

	/**
	 * Returns the upgrade stats for the current level of the tree
	 */
	public UpgradeStats getUpgradeStats() {
		return getAllUpgradeStats().get(upgradeLevel);
	}

	@Override
	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	@Override
	public Animation getAnimation() {
		return animation;
	}

	/**
	 * Returns a list of the stats for each upgrade level in order <br>
	 * This is called often, so it is recommend you don't create a new object every
	 * time
	 * 
	 * @return a list of all the upgrade stats for this tree
	 */
	public abstract List<UpgradeStats> getAllUpgradeStats();

	/**
	 * Returns the current progress
	 *
	 * @return
	 */
	@Override
	public int getProgress() {
		if (constructionLeft > 0) {
			return 100 - constructionLeft;
		} else {
			return (int) this.getHealth();

		}
	}

	/**
	 * Set's the progress to the given value.
	 *
	 * @param p
	 */
	@Override
	public void setProgress(int p) {
		constructionLeft = p;
	}

	/**
	 * Should i show the progress
	 *
	 * @return
	 */
	@Override
	public boolean showProgress() {
		if (getProgressRatio() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public float getProgressRatio() {
		if (constructionLeft > 0) {
			return 1 - constructionLeft / 100f;
		} else {
			return this.getHealth() / this.getMaxHealth();
		}
	}

	@Override
	public int getMaxProgress() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public void setMaxProgress(int p) {

	}

	@Override
	public ProgressBarEntity getProgressBar() {
		return constructionLeft > 0 ? PROGRESS_BAR : null;
	}

}
