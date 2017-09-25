package com.deco2800.potatoes.entities.trees;

public class IceTreeType extends DamageTreeType {
    private static final String TEXTURE = "ice_basic_tree";

    public IceTreeType(){

        this.damageTreeType = "IceTree";
        this.texture = TEXTURE;
//        TimeAnimation animation = AnimationFactory.createSimpleTimeAnimation(1000, new String[] {"frame1", ""});
//        GameManager.get().getManager(EventManager.class).registerEvent(this, animation);
//        TimeAnimation danimation = AnimationFactory.createSimpleTimeAnimation(1000, new String[] {"damage", ""});
//        GameManager.get().getManager(EventManager.class).registerEvent(this, danimation);
//        Animation actualAnimation = new StateAnimation(getMaxHealth(), 0f, new Animation[] {animation, danimation}, (Supplier<Float>) this::getHealth);
    }


}
