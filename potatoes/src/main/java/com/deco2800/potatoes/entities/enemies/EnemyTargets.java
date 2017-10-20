package com.deco2800.potatoes.entities.enemies;

import java.util.LinkedList;
import java.util.List;

public class EnemyTargets {

    private LinkedList<Class> mainTargets;
    private LinkedList<Class> sightAggroTargets;
    private LinkedList<Class> damageAggroTargets;

    public EnemyTargets(LinkedList<Class> mainTargets, LinkedList<Class> sightAggroTargets) {
        this.mainTargets = mainTargets;
        this.sightAggroTargets = sightAggroTargets;
        this.damageAggroTargets = damageAggroTargets;
    }

    public List<Class> getMainTargets() { return mainTargets;  }

    public List<Class> getSightAggroTargets() { return sightAggroTargets; }

}
