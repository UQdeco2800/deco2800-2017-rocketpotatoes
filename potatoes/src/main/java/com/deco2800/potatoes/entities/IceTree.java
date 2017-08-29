package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.entities.trees.DamageTree;

public class IceTree extends DamageTree {
    private final static String TEXTURE = "ice_basic_tree";
    public IceTree(){

    }

    public IceTree(float posX, float posY, float posZ){
        super(posX, posY, posZ, TEXTURE,100,10);
    }

}
