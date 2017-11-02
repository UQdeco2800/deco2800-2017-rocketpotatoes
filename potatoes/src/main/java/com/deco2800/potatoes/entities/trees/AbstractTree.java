package com.deco2800.potatoes.entities.trees;

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
import com.deco2800.potatoes.gui.FadingGui;
import com.deco2800.potatoes.gui.TreeShopGui;
import com.deco2800.potatoes.managers.*;

import java.util.List;

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

	private static final ProgressBarEntity BUILD_PROGRESS_BAR = new ProgressBarEntity("healthBarBlue", 1.5f);
	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity();
	private static final int UNLOCK_RANGE = 2;

	private boolean firstTick = true;
	/**
	 * Default constructor for serialization
	 */
	public AbstractTree() {

	}

	/**
	 * Creates this object with the given geometric properties. This tree is set to
	 * start growing and events are registered with the EventManager
	 * 
	 * @see AbstractEntity
	 */
    public AbstractTree(float posX, float posY, float xLength, float yLength) {
        super(new Box2D(posX, posY, xLength, yLength), xLength, yLength, "", 1);
		super.setStatic(true);
		super.setSolid(true);
		super.setShadow(new Circle2D(0,0,0.4f));
		setAnimation(getUpgradeStats().getAnimation().apply(this));
	}

	/**
	 * Creates a copy of this object as it was when it was first created.
	 */
	public abstract AbstractTree createCopy();

	@Override
	public void onTick(long time) {
		if (firstTick) {
			resetStats();
			firstTick = false;
		}
		// Check if player is close enough to unlock it
		PlayerManager playerManager = GameManager.get().getManager(PlayerManager.class);
		if (playerManager != null && playerManager.getPlayer() != null) {
				float distance = playerManager.distanceFromPlayer(this.getPosX(), this
						.getPosY());
				if (distance < UNLOCK_RANGE) {
					TreeShopGui treeShop = GameManager.get().getManager(GuiManager.class)
							.getGui(TreeShopGui.class);
					if (treeShop!= null) {
						TreeState treeState = treeShop.getTreeStateByName(this.getName());

						if (treeState != null && !treeState.isUnlocked()) {
								treeState.unlock();
								showUnlockedMenu(treeState);
								treeShop.refreshTreeStates();
							}
						
					}

				}
			}
			

	}

	private void showUnlockedMenu(TreeState treeState) {
		GuiManager guiManager = GameManager.get().getManager(GuiManager.class);
		guiManager.addFadingGui(new FadingGui(treeState, 2000, guiManager.getStage()));
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
		updateTexture();
		setRegisteredEvents(true);
	}

	/**
	 * Updates the texture based on the upgrade stats
	 */
	public void updateTexture() {
		setAnimation(getUpgradeStats().getAnimation().apply(this));
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
		String originalTexture = super.getTexture();
		return getAnimation() == null ? originalTexture : getAnimation().getFrame();
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
		if (constructionLeft > 0) {
			return 100;
		} else {
			return (int) this.getMaxHealth();
		}
	}

	@Override
	public ProgressBarEntity getProgressBar() {
		return constructionLeft > 0 ? BUILD_PROGRESS_BAR : getHealth() < getMaxHealth() ? PROGRESS_BAR : null;
	}

	public String getName() {
		return "Abstract Tree";
	}

	@Override
	public String toString(){
		return "Tree: " + getUpgradeStats().getAnimation().apply(this).getFrame();
	}

	@Override
	public int hashCode() {
		return this.getName().hashCode() * 33;
	}
}
