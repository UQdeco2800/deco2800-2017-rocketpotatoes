package com.deco2800.potatoes.entities.trees;

public class LightningTreeType extends DamageTreeType {

    public LightningTreeType(){
        this.damageTreeType = "LightningTree";
        this.texture = "lightning_tree1";
    }
    
    public LightningTreeType(String texture) {
    	this.damageTreeType = "LightningTree";
        this.texture = texture;
    }


}
