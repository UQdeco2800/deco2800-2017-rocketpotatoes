package com.deco2800.potatoes.entities.trees;

import com.deco2800.potatoes.entities.Damage;

public class FireTree extends Damage {

    private static final String TEXTURE = "fire_tree";

    public FireTree(){

        this.damageTreeType = "FireTree";
        this.texture = TEXTURE;

    }

}
