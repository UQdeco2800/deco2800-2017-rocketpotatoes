package com.deco2800.potatoes.entities.trees;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.animation.Animated;
import com.deco2800.potatoes.entities.animation.Animation;
import com.deco2800.potatoes.entities.health.HasProgress;
import com.deco2800.potatoes.entities.health.HasProgressBar;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;

/**
 * AbstractTree represents an upgradable tree entity. AbstractTree can have
 * registered normal events which are triggered when the tree is not under
 * construction and construction events which are triggered when the tree is
 * being constructed
 */
public abstract class AbstractTree extends MortalEntity implements Tickable, HasProgress, HasProgressBar, Animated {

	private int constructionLeft = 100;
	private int upgradeLevel = 0;
	private transient Animation animation;
	private boolean dying;
	private boolean beingDamaged;

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
	void setRegisteredEvents(boolean construction) {
		if (construction) {
			getUpgradeStats().registerBuildEvents(this);
		} else {
			getUpgradeStats().registerEvents(this);
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
		this.addMaxHealth(getUpgradeStats().getHealth() - this.getMaxHealth());
		this.heal(getMaxHealth());
		setAnimation(getUpgradeStats().getAnimation().apply(this));
		setRegisteredEvents(true);
	}

	/**
	 * Returns the upgrade stats for the current level of the tree
	 */
	public TreeStatistics getUpgradeStats() {
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
	 * @return the dying
	 */
	public boolean isDying() {
		return dying;
	}

	/**
	 * Sets if this tree is currently dying. If this is set to false, the tree dies
	 * @param dying whether this tree is dying
	 */
	public void setDying(boolean dying) {
		this.dying = dying;
		if (!dying) {
			// Animation is finished, so die
			super.deathHandler();
		}
	}

	/**
	 * @return the beingDamaged
	 */
	public boolean isBeingDamaged() {
		return beingDamaged;
	}

	/**
	 * @param beingDamaged the beingDamaged to set
	 */
	public void setBeingDamaged(boolean beingDamaged) {
		this.beingDamaged = beingDamaged;
	}
	
	@Override
	public boolean damage(float amount) {
		beingDamaged = true;
		return super.damage(amount);
	}
	
	@Override
	public void deathHandler() {
		dying = true;
		// Don't kill the entity just yet
		
		// destroy the tree
		//for enemy attacking test
		GameManager.get().getWorld().removeEntity(this);
	}
	
	/**
	 * Returns a list of the stats for each upgrade level in order <br>
	 * This is called often, so it is recommend you don't create a new object every
	 * time
	 * 
	 * @return a list of all the upgrade stats for this tree
	 */
	public abstract List<TreeStatistics> getAllUpgradeStats();

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
	public ProgressBarEntity getProgressBar() {
		return constructionLeft > 0 ? PROGRESS_BAR : null;
	}
}
