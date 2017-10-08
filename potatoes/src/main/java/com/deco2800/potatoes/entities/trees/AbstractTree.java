package com.deco2800.potatoes.entities.trees;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.collisions.Box2D;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.entities.AbstractEntity;
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

	private static final List<Color> BUILD_COLOURS = Arrays.asList(Color.YELLOW);
	private static final ProgressBarEntity BUILD_PROGRESS_BAR = new ProgressBarEntity("progress_bar", BUILD_COLOURS, 60, 1);
	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity();

	/**
	 * Default constructor for serialization
	 */
	public AbstractTree() {
		// Reseting may not be needed
		resetStats();
	}

	/**
	 * Creates this object with the given geometric properties. This tree is set to
	 * start growing and events are registered with the EventManager
	 * 
	 * @see AbstractEntity
	 */
    public AbstractTree(float posX, float posY, float xLength, float yLength) {
        super(new Box2D(posX, posY, xLength, yLength), xLength, yLength, "", 1);
		resetStats();

		super.setStatic(true);
		super.setSolid(true);
		super.setShadow(new Circle2D(0,0,0.4f));
	}

	/**
	 * Creates a copy of this object as it was when it was first created.
	 */
	@Override
	public abstract AbstractTree clone();

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
		boolean result = tree.getUpgradeStats().removeConstructionResources(tree);
		if (result) {
			GameManager.get().getWorld().addEntity(tree);
			System.out.println("adding tree");
		} else {
			GameManager.get().getManager(EventManager.class).unregisterAll(tree);
			System.out.println("the other thing");
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

	/**
	 * Decreases the construction left by 1
	 */
	public void decrementConstructionLeft() {
		constructionLeft--;
	}

	/**
	 * Upgrades to the next tree level
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
	public TreeProperties getUpgradeStats() {
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

	@Override
	public String getTexture() {
		return getAnimation().getFrame();
	}

	@Override
	public boolean damage(float amount) {
		getUpgradeStats().setDamageAnimation(this);
		return super.damage(amount);
	}

	@Override
	public void dyingHandler() {
		getUpgradeStats().setDeathAnimation(this);
	}

	/**
	 * Returns a list of the stats for each upgrade level in order <br>
	 * This is called often, so it is recommend you don't create a new object every
	 * time
	 * 
	 * @return a list of all the upgrade stats for this tree
	 */
	public abstract List<TreeProperties> getAllUpgradeStats();

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
		return constructionLeft > 0 ? BUILD_PROGRESS_BAR : getHealth() < getMaxHealth() ? PROGRESS_BAR : null;
	}
}
