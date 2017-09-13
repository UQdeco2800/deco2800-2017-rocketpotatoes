package com.deco2800.potatoes.entities.trees;

import com.deco2800.potatoes.entities.Damage;

public class FireTree extends Damage {

    private final static String TEXTURE = "fire_tree";

    public FireTree(){

        this.damageTreeType = "FireTree";
        this.texture = TEXTURE;

    }

}
