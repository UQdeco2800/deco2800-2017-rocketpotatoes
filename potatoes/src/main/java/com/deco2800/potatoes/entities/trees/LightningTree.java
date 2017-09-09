package com.deco2800.potatoes.entities.trees;

import com.deco2800.potatoes.entities.Damage;

public class LightningTree extends Damage {
    private final static String TEXTURE = "lightning_tree";

    public LightningTree(){
        this.damageTreeType = "LightningTree";
        this.texture = TEXTURE;
    }


}
