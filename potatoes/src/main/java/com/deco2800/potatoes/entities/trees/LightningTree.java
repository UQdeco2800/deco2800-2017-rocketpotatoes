package com.deco2800.potatoes.entities.trees;

import com.deco2800.potatoes.entities.Damage;

public class LightningTree extends Damage {
    private static final String TEXTURE = "lightning_tree1";

    public LightningTree(){
        this.damageTreeType = "LightningTree";
        this.texture = TEXTURE;
    }


}
