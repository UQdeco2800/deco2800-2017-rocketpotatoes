package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.entities.trees.DamageTree;

public class IceTree extends Damage {
    private final static String TEXTURE = "ice_basic_tree";

    public IceTree(){

        this.damageTreeType = "IceTree";
        this.texture = TEXTURE;

    }


}
