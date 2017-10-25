package com.deco2800.potatoes.entities.constructables;

import com.deco2800.potatoes.collisions.Box2D;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.animation.Animation;
import com.deco2800.potatoes.entities.health.HasProgress;
import com.deco2800.potatoes.entities.health.HasProgressBar;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;

public abstract class Constructable extends MortalEntity implements Tickable, HasProgress, HasProgressBar {
    private int constructionLeft;
    private transient Animation animation;

    // Gross
    private static final ProgressBarEntity BUILD_PROGRESS_BAR = new ProgressBarEntity("healthBarBlue", 1.5f);
    private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity();

    public Constructable(float posX, float posY, float xLength, float yLength, boolean constructing) {
        super(new Box2D(posX, posY, xLength, yLength), xLength, yLength, "tree", 1);
        super.setStatic(true);
        super.setSolid(true);
        super.setShadow(new Circle2D(0,0,0.4f));

        // TODO construtable builder from JSON
        if (constructing) {
            constructionLeft = 100;
            GameManager.get().getManager(EventManager.class).fireConstructionStartEvent(this);
        }
        else {
            constructionLeft = 0;
            GameManager.get().getManager(EventManager.class).fireConstructionEndEvent(this);
        }
    }

    @Override
    public void onTick(long time) {
        if (constructionLeft > 0) {
            decrementConstructionLeft();
        }
    }

    /**
     * @return the percentage of construction left, from 0 to 100
     */
    public int getConstructionLeft() {
        return constructionLeft;
    }

    /**
     * Decreases the construction left by 1
     */
    public void decrementConstructionLeft() {
        constructionLeft--;

        GameManager.get().getManager(EventManager.class).fireConstructionTickEvent(this);
        
        if (!isConstructing()) {
            GameManager.get().getManager(EventManager.class).fireConstructionEndEvent(this);
        }
    }

//    @Override
//    public void setAnimation(Animation animation) {
//        this.animation = animation;
//    }
//
//    @Override
//    public Animation getAnimation() {
//        return animation;
//    }
    @Override
    public boolean damage(float amount) {
        return super.damage(amount);

        // Play damage animation
    }

    @Override
    public ProgressBarEntity getProgressBar() {
        return constructionLeft > 0 ? BUILD_PROGRESS_BAR : getHealth() < getMaxHealth() ? PROGRESS_BAR : null;
    }

    public boolean isConstructing() {
        return constructionLeft > 0;
    }
}
