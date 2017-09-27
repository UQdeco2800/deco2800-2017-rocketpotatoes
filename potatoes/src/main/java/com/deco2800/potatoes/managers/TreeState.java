package com.deco2800.potatoes.managers;

import com.badlogic.gdx.utils.Logger;
import com.deco2800.potatoes.entities.trees.AbstractTree;

/**
 * information for each tree regarding whether the user has unlocked it and how much of
 * each resource would be required to buy it.
 *
 * @author Dion Lao
 */
public class TreeState {
    private boolean unlocked;   // Whether tree has been unlocked by player
    private Inventory cost;     // Amount of resources required to buy tree
    private AbstractTree tree;  // Tree type being bought

    /**
     * Empty constructor for values to be set later.
     */
    public TreeState() {
        unlocked = false;
        cost = new Inventory();
        tree = null;
    }

    /**
     * Instantiates a class to store the costs and unlocked status of trees for the user.
     *
     * @param tree     tree type being used
     * @param cost     amount of resources required to purchase tree
     * @param unlocked whether player has unlocked the tree yet
     */
    public TreeState(AbstractTree tree, Inventory cost, boolean unlocked) {
        if (tree == null || !(tree instanceof AbstractTree)) {
            LOGGER.error("Please supply a valid tree");
        } else if (cost == null) {
            LOGGER.error("Please supply a valid cost");
        } else {
            this.unlocked = unlocked;
            this.tree = tree;
            this.cost = cost;
        }

    }

    /**
     * Returns the tree type.
     */
    public AbstractTree getTree() {
        return this.tree.clone();
    }

    /**
     * Sets the tree entity.
     */
    public void setTree(AbstractTree tree) {
        this.tree = tree.clone();
    }

    /**
     * Uplocks tree.
     */
    public void unlock() {
        this.unlocked = true;
    }

    /**
     * Set whether tree is unlocked.
     */
    public void setUnlock(boolean unlocked) {
        this.unlocked = unlocked;
    }

    /**
     * Whether the user has unlocked this tree.
     */
    public boolean isUnlocked() {
        return unlocked;
    }

    /**
     * Returns the cost of tree.
     */
    public Inventory getCost() {
        Inventory costClone = new Inventory();
        costClone.updateInventory(cost);
        return costClone;
    }

    /**
     * Empties then sets cost to input cost paramater.
     *
     * @param cost new tree cost
     */
    public void setCost(Inventory cost) {
        this.cost = new Inventory();
        this.cost.updateInventory(cost);
    }

    /**
     * Increases all costs by a set amount.
     *
     * @param cost increased cost by amount
     */
    public void updateCost(Inventory cost) {
        this.cost.updateInventory(cost);
    }

    @Override
    public int hashCode() {
        return tree.hashCode();
    }
}
