package com.deco2800.potatoes.entities.constructables;

import com.deco2800.potatoes.collisions.Box2D;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.animation.Animated;
import com.deco2800.potatoes.entities.animation.Animation;
import com.deco2800.potatoes.entities.health.HasProgress;
import com.deco2800.potatoes.entities.health.HasProgressBar;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;

public abstract class Constructable extends MortalEntity implements Tickable, HasProgress, HasProgressBar, Animated {
    private int constructionLeft = 100;
    private transient Animation animation;

    // Gross
    private static final ProgressBarEntity BUILD_PROGRESS_BAR = new ProgressBarEntity("healthBarBlue", 1.5f);
    private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity();

    public Constructable(float posX, float posY, float xLength, float yLength) {
        super(new Box2D(posX, posY, xLength, yLength), xLength, yLength, "", 1);
        super.setStatic(true);
        super.setSolid(true);
        super.setShadow(new Circle2D(0,0,0.4f));
    }

    @Override
    public void onTick(long time) {

    }

    /**
     * @return the percentage of construction left, from 0 to 100
     */
    public int getConstructionLeft() {
        return constructionLeft;
    }

    /**
     * @param constructionLeft
     *            the percentage of construction to set, from 0 to 100
     */
    public void setConstructionLeft(int constructionLeft) {
        this.constructionLeft = constructionLeft;
    }

    /**
     * Decreases the construction left by 1
     */
    public void decrementConstructionLeft() {
        constructionLeft--;

        GameManager.get().getManager(EventManager.class).fireConstructionTickEvent(this);
    }

    @Override
    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    @Override
    public Animation getAnimation() {
        return animation;
    }

    @Override
    public String getTexture() {
        return getAnimation().getFrame();
    }

    @Override
    public boolean damage(float amount) {
        return super.damage(amount);

        // Play damage animation
    }

    @Override
    public ProgressBarEntity getProgressBar() {
        return constructionLeft > 0 ? BUILD_PROGRESS_BAR : getHealth() < getMaxHealth() ? PROGRESS_BAR : null;
    }
}
