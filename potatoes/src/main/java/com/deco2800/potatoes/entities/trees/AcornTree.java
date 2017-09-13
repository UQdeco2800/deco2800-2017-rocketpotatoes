package com.deco2800.potatoes.entities.trees;

import com.deco2800.potatoes.entities.Damage;

public class AcornTree extends Damage {
    private static final String TEXTURE = "acorn_tree";

    public AcornTree(){
        this.damageTreeType = "AcornTree";
        this.texture = TEXTURE;
    }


}
