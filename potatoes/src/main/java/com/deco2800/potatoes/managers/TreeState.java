package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.entities.resources.SeedResource;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.potatoes.entities.trees.AbstractTree;

/**
 * information for each tree regarding whether the user has unlocked it and how much of
 * each resource would be required to buy it.
 *
 * @author Dion Lao
 */
public class TreeState {
    private static final transient Logger LOGGER = LoggerFactory.getLogger(TreeState
            .class);

    private boolean unlocked;   // Whether tree has been unlocked by player
    private Inventory cost;     // Amount of resources required to buy tree
    private AbstractTree tree;  // Tree type being bought
    private String treeType;    // Resource, damage, or defense

    /**
     * Empty constructor for values to be set later.
     */
    public TreeState() {
        unlocked = false;
        cost = new Inventory();
        tree = new ResourceTree(0,0,new SeedResource(), 1);
        treeType = "resource";
    }

    /**
     * Instantiates a class to store the costs and unlocked status of trees for the user.
     *
     * @param tree     tree type being used
     * @param cost     amount of resources required to purchase tree
     * @param unlocked whether player has unlocked the tree yet
     * @param treeType whether the tree is a resource, damamge or defense type
     */
    public TreeState(AbstractTree tree, Inventory cost, boolean unlocked, String
            treeType) {
        if (tree == null || !(tree instanceof AbstractTree)) {
            LOGGER.warn("Please supply a valid tree");
            tree = new ResourceTree(0,0,new SeedResource(), 1);
            treeType = "resource";
        } else if (cost == null) {
            LOGGER.warn("Please supply a valid cost");
            cost = new Inventory();
        } else {
            this.tree = tree;
            this.cost = cost;
            this.treeType = treeType;
        }
        this.unlocked = unlocked;

    }


    /**
     * Returns the tree type.
     */
    public AbstractTree getTree() {
        return (this.tree == null) ? null : this.tree.createCopy();
    }

    /**
     * Sets the tree entity.
     */
    public void setTree(AbstractTree tree) {
        this.tree = tree.createCopy();
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

    public String getTreeType(){
        return treeType;
    }

    public void setTreeType(String treeType){
        this.treeType = treeType;
    }

    @Override
    public int hashCode() {
        return tree.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        return tree.equals(obj);
    }
}
