package com.deco2800.potatoes.entities.enemies;

import java.util.List;

/**
 * A data class for holding classes of entities that comprise an enemy's targets.
 *
 * Main targets are classes of entities that will attract an enemy regardless of their respective
 * positions in the world. Sight aggro targets are classes of entities that are to only attract an
 * enemy if they are within a certain distance of each other in game.
 *
 * @author craig
 */
public class EnemyTargets {

    private List<Class> mainTargets;
    private List<Class> sightAggroTargets;

    /**
     * Constructor for class, initialize with collections of classes for main targets and sight aggro.
     *
     * @param mainTargets
     * @param sightAggroTargets
     */
    public EnemyTargets(List<Class> mainTargets, List<Class> sightAggroTargets) {
        this.mainTargets = mainTargets;
        this.sightAggroTargets = sightAggroTargets;
    }

    /**
     * @return the contained main target classes
     */
    public List<Class> getMainTargets() { return mainTargets;  }

    /**
     * @return the contained sight aggro target classes
     */
    public List<Class> getSightAggroTargets() { return sightAggroTargets; }

}
