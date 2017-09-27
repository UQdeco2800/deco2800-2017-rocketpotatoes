package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.entities.trees.AbstractTree;

public class TreeState {
    private boolean unlocked;
    private Inventory cost;
    private AbstractTree tree;

    public TreeState() {
        unlocked = false;
        cost = new Inventory();
        tree = null;
    }

    public TreeState(AbstractTree tree, Inventory cost, boolean unlocked) {
        this.unlocked = unlocked;
        this.tree = tree;
        this.cost = cost;
    }

    public AbstractTree getTree(){
        return this.tree.clone();
    }

    public void setTree(AbstractTree tree){
        this.tree = tree.clone();
    }

    public void unlock(){
        this.unlocked = true;
    }

    public void setUnlock(boolean unlocked){
        this.unlocked = unlocked;
    }

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

    public void updateCost(Inventory cost) {
        this.cost.updateInventory(cost);
    }

}
